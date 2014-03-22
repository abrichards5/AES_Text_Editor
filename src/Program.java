import controller.Functions;
import controller.MenuListener;
import model.CBCCryptographer;
import model.Cryptographer;
import model.ECBCryptographer;
import model.ModelController;
import view.AppFrame;

/**
 * Created by alutman on 17/03/14.
 * <p/>
 * Start point
 */
public class Program {

    public static void main(String args[]) {
        Cryptographer crypt = parseCommandLine(args);
        AppFrame app = new AppFrame();
        ModelController mc = new ModelController(app, crypt);
        Functions f = new Functions(app, mc);
        new MenuListener(app, f);

    }

    public static Cryptographer parseCommandLine(String[] args) {
        if (args.length == 0) {
            System.out.println("Using default Cryptographer: CBC");
            return new CBCCryptographer();
        } else if (args.length == 1) {
            if (args[0].toUpperCase().equals("CBC")) {
                System.out.println("Using Cryptographer: CBC");
                return new CBCCryptographer();
            } else if (args[0].toUpperCase().equals("ECB")) {
                System.out.println("Using Cryptographer: ECB");
                return new ECBCryptographer();
            } else {
                usage("Invalid encryption mode: " + args[0]);
                System.exit(0);
            }
        } else {
            usage("Invalid number of arguments");
            System.exit(0);
        }
        return null;
    }

    private static void usage(String error) {
        System.out.println(error);
        System.out.println("usage: AESTextEditor [CRYPTO]");
        System.out.println("\nGraphical tool to edit and encrypt/decrypt text files\n");
        System.out.println("optional arguments:");
        System.out.println("  CRYPTO\tSpcifiy encryption mode used, 'ECB' or 'CBC. Defaults to CBC");
    }

}
