import controller.UserActionListener;
import controller.Functions;
import model.CBCCryptographer;
import model.Cryptographer;
import model.ModelController;
import view.AppFrame;

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
        if (args.length > 1) {
            usage("Invalid number of arguments");
        }
        if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                usage();
            }
            else {
                usage("Invalid argument");
            }
        }
        startGUI();
    }

    private static void startGUI() {
        Cryptographer crypt = new CBCCryptographer();
        AppFrame app = new AppFrame();
        ModelController mc = new ModelController(app, crypt);
        Functions f = new Functions(app, mc);
        new UserActionListener(app, f);
    }

    private static void usage() {
        System.out.println("usage: java -jar AESTextEditor.jar [-h]\n");
        System.out.println("Graphical tool to edit and encrypt/decrypt text files");
        System.out.println("Java 1.7 required\n");
        System.out.println("optional arguments:\n");
        System.out.println(" -h, --help\tshow this help message and exit\n");
        System.exit(0);
    }

    private static void usage(String error) {
        System.out.println(error);
        usage();
    }

}
