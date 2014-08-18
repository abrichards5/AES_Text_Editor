import controller.GUIModel;
import controller.UserActionListener;
import controller.Functions;
import model.*;
import model.enums.CryptStatus;
import view.AppFrame;

import java.io.*;

/**
 * Created by alutman on 17/03/14.
 *
 * Start point
 * TODO : Info button
 * TODO : Choose encoding (Base64, Hex, Oct)
 *
 *
 */
class Program {

    public static void main(String args[]) {
        if(args.length > 0) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                usage("", 0);
            }
            else if(args[0].equals("-e") || args[0].equals("--encrypt")) {
                parseArgsToCrypt(args, true);

            }
            else if(args[0].equals("-d") || args[0].equals("--decrypt")) {
                parseArgsToCrypt(args, false);
            }
            else {
                usage("Invalid argument", 1);
            }
        }
        if (args.length == 0) {
            startGUI();
        }
    }

    private static void parseArgsToCrypt(String[] args, boolean encrypt) {
        String outfile = "";
        if(args.length == 3)  {
            outfile = args[1];
        }
        else if (args.length == 4) {
            outfile = args[3];
        }
        else {
            usage("Incorrect number of arguments", 1);
        }
        String infile = args[1];
        String password = args[2];

        doCrypt(infile, outfile, password, encrypt);
    }


    private static void doCrypt(String in, String out, String pass, boolean encrypt) {
        Cryptographer crypt = new CBCCryptographer();
        BaseModel bm = new BaseModel(crypt);
        CryptStatus result = null;
        try {
            bm.openFile(new File(in));
            if(encrypt) {
                result = bm.encrypt(pass);
            }
            else {
                result = bm.decrypt(pass);
            }

            bm.saveFile(new File(out));
        } catch (IOException ioe) {
            System.err.println("File "+in+" not found. Exiting...");
            System.exit(1);
        }
        System.out.println(result.toString());

    }

    private static void startGUI() {
        Cryptographer crypt = new CBCCryptographer();
        AppFrame app = new AppFrame();
        GUIModel mc = new GUIModel(app, crypt);
        Functions f = new Functions(app, mc);
        new UserActionListener(app, f);
    }

    private static void usage(String error, int code) {
        System.out.println(error);
        System.out.println("usage: java -jar AESTextEditor.jar [OPTION] INFILE PASSWORD [OUTFILE]\n");
        System.out.println("Graphical tool to edit and encrypt/decrypt text files");
        System.out.println("Java 1.7 required\n");
        System.out.println("optional arguments:\n");
        System.out.println(" -h, --help\t\tshow this help message and exit\n");
        System.out.println(" -e, --encrypt\tencrypt INFILE with PASSWORD, output to INFILE unless OUTFILE is specified. No GUI\n");
        System.out.println(" -d, --decrypt\tdecrypt INFILE with PASSWORD, output to INFILE unless OUTFILE is specified. No GUI\n");
        System.exit(code);
    }

}
