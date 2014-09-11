package main;

import controller.GUIModel;
import controller.UserActionListener;
import controller.Functions;
import model.*;
import model.enums.CryptStatus;
import model.enums.Encoding;
import model.exception.InvalidEncodingException;
import org.apache.commons.cli.ParseException;
import view.AppFrame;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.Scanner;

/**
 * Created by alutman on 17/03/14.
 *
 * Start point
 *
 *
 */
public class Program {
    public static final String VERSION = "1.7a";

    public static void main(String args[]) {
        parseCLI(args);
    }
    private static void parseCLI(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "display this help");
        options.addOption(OptionBuilder.withLongOpt("encrypt")
                .withDescription("encrypt a file")
                .hasArg()
                .withArgName("FILENAME")
                .create("e"));
        options.addOption(OptionBuilder.withLongOpt("decrypt")
                .withDescription("decrypt a file")
                .hasArg()
                .withArgName("FILENAME")
                .create("d"));
        options.addOption(OptionBuilder.withLongOpt("outfile")
                .withDescription("ouput to a file")
                .hasArg()
                .withArgName("OUTFILE")
                .create("o"));
        options.addOption(OptionBuilder.withLongOpt("password")
                .withDescription("use specified password")
                .hasArg()
                .withArgName("PASSWORD")
                .create("p"));
        options.addOption(OptionBuilder.withLongOpt("encoding")
                .withDescription("specify input/output encoding. Must be NONE(disabled), HEX or BASE64. Defaults to Base64 for text files, none for binary") //TODO
                .hasArg()
                .withArgName("ENCODING")
                .create("t"));

        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) apacheUsage(options, 0);

            String outfile, infile, encoding = "default", password = null;
            if(cmd.hasOption("password")) password = cmd.getOptionValue("password");
            if(cmd.hasOption("encoding")) encoding = cmd.getOptionValue("encoding");

            if(cmd.hasOption("decrypt")){
                infile = cmd.getOptionValue("decrypt");
                if(cmd.hasOption("outfile"))
                    outfile = cmd.getOptionValue("outfile");
                else {
                    outfile = infile;
                }

                doCrypt(infile, outfile, password, false, encoding);
            }
            else if(cmd.hasOption("encrypt")) {
                infile = cmd.getOptionValue("encrypt");
                if(cmd.hasOption("outfile")) {
                    outfile = cmd.getOptionValue("outfile");
                }
                else {
                    outfile = infile;
                }

                doCrypt(infile, outfile, password, true, encoding);
            }
            else if(cmd.getOptions().length == 0) {
                startGUI();
            }
            else {
                System.out.println("encrypt or decrypt must be specified");
                apacheUsage(options, 1);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            apacheUsage(options, 1);
        } catch (IOException e) {
            System.out.println("Problem opening file: "+e.getMessage());
        } catch (InvalidEncodingException e) {
            System.out.println(e.getMessage());
        }

    }
    private static void apacheUsage(Options options, int exitVal) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("AES Text Editor",
                "v"+VERSION+"\n" +
                        "Graphical tool to edit and encrypt/decrypt text files\n" +
                        "Run with no arguments to start the GUI\n" +
                        "Java 1.7 required",
                options,
                "");
        System.exit(exitVal);
    }


    private static void doCrypt(String in, String out, String pass, boolean encrypt, String encoding) throws IOException, InvalidEncodingException {
        Cryptographer crypt = new CBCCryptographer();
        BaseModel bm = new BaseModel(crypt);
        CryptStatus result = null;

        bm.openFile(new File(in));

        Encoding enc = Encoding.toEnum(encoding);
        if(!encoding.equals("default")) {
            if(enc == null) {
                throw new InvalidEncodingException("Invalid encoding: " + encoding);
            }
            else if(!enc.equals(Encoding.NONE)){ //TODO
                bm.setEncoding(enc);
            }
        }


        if (pass == null) {
            System.out.print("Enter password: ");
            pass = new Scanner(System.in).nextLine();
        }
        if(encrypt) {
            result = bm.encrypt(pass);
        }
        else {
            result = bm.decrypt(pass);
        }
        bm.saveFile(new File(out));
        System.out.println(result.toString());

    }

    private static void startGUI() {
        Cryptographer crypt = new CBCCryptographer();
        AppFrame app = new AppFrame();
        GUIModel mc = new GUIModel(app, crypt);
        Functions f = new Functions(app, mc);
        new UserActionListener(app, f);
    }
}
