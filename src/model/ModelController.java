package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import view.AppFrame;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Handles
 *  - File saving and opening
 *  - Using the ECBCryptographer class
 *
 */
public class ModelController {

    private Cryptographer aes;
    private AppFrame view;

    public ModelController(AppFrame af, Cryptographer aes) {
        view = af;
        this.aes = aes;
        cryptoUpdateGUI();
    }

    private String getCurrentCryptoMode() {
        if(aes.getClass().equals(ECBCryptographer.class)) {
            return Cryptographer.ECB;
        }
        else if(aes.getClass().equals(CBCCryptographer.class)) {
            return Cryptographer.CBC;
        }
        return null;
    }

    private void cryptoUpdateGUI() {
        String curMode = getCurrentCryptoMode();
        if(curMode.equals(Cryptographer.CBC)) {
            view.statusBar().setMode(Cryptographer.CBC);
            view.menuBar().setSwitchCryptoText("Switch to "+Cryptographer.ECB);
        }
        else if (curMode.equals(Cryptographer.ECB)) {
            view.statusBar().setMode(Cryptographer.ECB);
            view.menuBar().setSwitchCryptoText("Switch to " + Cryptographer.CBC);
        }
    }

    public void switchCrypto() {
        String curMode = getCurrentCryptoMode();
        if(curMode.equals(Cryptographer.CBC)) {
            aes = new ECBCryptographer();
        }
        else if (curMode.equals(Cryptographer.ECB)) {
            aes = new CBCCryptographer();
        }
        cryptoUpdateGUI();
    }

    public void newFile() {
        view.getTextArea().setText("");
        view.statusBar().setFilename("");
        view.setTitle("");
    }

    // Checks to see if the file is open able as text
    private boolean checkFile(String data) {
        final long MAX_SIZE = 15000000L;
        char[] chars = data.toCharArray();
        long length = chars.length;
        // Check size
        if(length > MAX_SIZE) {
            return false;
        }
        // Check valid text
        for (char c : chars) {
            if(c <= 0){
                return false;
            }
        }
        return true;
    }

    public void openFile(File file) {
        String dataString;
        try {
            //Read file into a single string
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            fis.read(data);
            fis.close();
            dataString = new String(data);
        }
        catch (FileNotFoundException fnfe) {
            view.statusBar().setStatus("FILE NOT FOUND: "+file.getName());
            return;
        }
        catch (IOException ioe) {
            view.statusBar().setStatus("IO EXCEPTION: "+file.getName());
            return;
        }
        if(checkFile(dataString)) {
            view.getTextArea().setText(dataString);
            view.statusBar().setFilename(file.getAbsolutePath());
            view.setTitle(file.getName());
            view.statusBar().setStatus("OPEN SUCCESSFUL");
        }
        else {
            view.statusBar().setStatus("FILE NOT TEXT OR TOO LARGE");
        }
    }

    public void saveFile(File filename) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename));
            pw.print(view.getTextArea().getText());
            pw.close();
        }
        catch (IOException ioe) {
            view.statusBar().setStatus("IO EXCEPTION: "+filename);
            return;
        }
        view.statusBar().setFilename(filename.getAbsolutePath());
        view.setTitle(filename.getName());
        view.statusBar().setStatus("SAVE SUCCESSFUL");

    }

    public void encrypt(String key) {
        if(!checkKey(key)) return;
        view.getTextArea().setText(aes.encrypt(key, view.getTextArea().getText()));
        view.statusBar().setStatus("ENCRYPT SUCCESSFUL");
    }

    public void decrypt (String key) {
        if(!checkKey(key)) return;
        try {
            view.getTextArea().setText(aes.decrypt(key, view.getTextArea().getText()));
        }
        catch (Base64DecodingException b64de) {
            //Base64DecodingException is a result of trying to decrypt something that isn't encrypted
            view.statusBar().setStatus("NOT A VALID ENCRYPTED FILE");
            return;
        }
        catch (BadPaddingException | IllegalBlockSizeException e) {
            //BadPaddingException is a result of trying to decrypt with a incorrect key
            view.statusBar().setStatus("INVALID KEY/ENCRYPTION");
            return;
        }
        view.statusBar().setStatus("DECRYPT SUCCESSFUL");
    }

    private boolean checkKey(String key) {
        if (key == null ) {
            //JInputPane - Cancel option
            return false;
        }
        else if(key.equals("")) {
            //JInputPane - Yes option
            view.statusBar().setStatus("KEY MUST NOT BE NULL");
            return false;
        }
        return true;
    }



}
