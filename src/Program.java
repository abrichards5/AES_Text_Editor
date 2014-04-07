import controller.Functions;
import controller.MenuListener;
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
public class Program {

    public static void main(String args[]) {
        Cryptographer crypt = new CBCCryptographer();
        AppFrame app = new AppFrame();
        ModelController mc = new ModelController(app, crypt);
        Functions f = new Functions(app, mc);
        new MenuListener(app, f);

    }

    private static void usage(String error) {
        System.out.println(error);
        System.out.println("usage: AESTextEditor");
        System.out.println("\nGraphical tool to edit and encrypt/decrypt text files\n");
    }

}
