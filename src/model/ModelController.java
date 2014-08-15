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

    private final Cryptographer aes;
    private final AppFrame view;
    private final ByteData data = new ByteData();
    private FileMode mode = FileMode.TEXT;
    private final long MAX_SIZE =  50000000L; //50mb

    public ModelController(AppFrame af, Cryptographer aes) {
        view = af;
        this.aes = aes;
        cryptoUpdateGUI();
    }

    private String getCurrentCryptoMode() {
        return aes.getMode();
    }

    private void cryptoUpdateGUI() {
        String curMode = getCurrentCryptoMode();
        view.statusBar().setMode(curMode);
    }

    public void newFile() {
        mode = FileMode.TEXT;
        view.setTextMode();
        view.getTextArea().setText("");
        view.statusBar().setFilename("");
        view.setTitle("");
    }

    // Loads the file and checks what type it is
    private FileCheck loadFile(File file) throws IOException {
        long length = file.length();
        // Check size
        if(length > MAX_SIZE) {
            return FileCheck.LARGE_FILE;
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] dataIn = new byte[(int)file.length()];
        fis.read(dataIn, 0, dataIn.length);
        data.set(dataIn);
        fis.close();

        for (byte c : dataIn) {
            if(!validByte(c)){
                return FileCheck.BINARY_FILE;
            }
        }
        return FileCheck.TEXT_FILE;
    }

    private boolean validByte(byte b) {
        if(b < 0x7F) {
            return true;
        }
        return false;
    }

    public void openFile(File file) {
        FileCheck fcIn;
        try {
             fcIn = loadFile(file);
        }
        catch (FileNotFoundException fnfe) {
            view.statusBar().setStatus("FILE NOT FOUND: "+file.getName());
            return;
        }
        catch (IOException ioe) {
            view.statusBar().setStatus("IO EXCEPTION: "+file.getName());
            return;
        }

        if(fcIn.equals(FileCheck.TEXT_FILE)) {
            view.setTextMode();
            mode = FileMode.TEXT;
            view.statusBar().setStatus("OPEN SUCCESSFUL");
        }
        else if(fcIn.equals(FileCheck.BINARY_FILE)) {
            view.setBinaryMode();
            mode = FileMode.BINARY;
            view.statusBar().setStatus("BINARY OPEN SUCCESSFUL");
        }
        else if(fcIn.equals(FileCheck.LARGE_FILE)) {
            view.statusBar().setStatus("FILE TOO LARGE");
            return;
        }

        updateView();
        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitle(file.getName());
    }

    public void saveFile(File filename) {
        updateModel();
        try {
            FileOutputStream fos2 = new FileOutputStream(filename);
            fos2.write(data.bytes(), 0, data.length());
            fos2.flush();
            fos2.close();
        }
        catch (IOException ioe) {
            view.statusBar().setStatus("IO EXCEPTION: "+filename);
            return;
        }
        view.statusBar().setFilename(filename.getAbsolutePath());
        view.setTitle(filename.getName());
        view.statusBar().setStatus("SAVE SUCCESSFUL");
    }

    //Push data from model to view
    private void updateView() {
        if(mode.equals(FileMode.TEXT)) {
            view.getTextArea().setText(data.text());
        }
        //Notifies change in underlying data
        else if(mode.equals(FileMode.BINARY)) {
            view.getTextArea().setText(view.getTextArea().getText());
        }
    }
    //Pull data from view to model
    private void updateModel() {
        if(mode.equals(FileMode.TEXT)){
            data.set(view.getTextArea().getText());
        }
    }

    public void encrypt(String key) {
        if(!checkKey(key)) return;

        updateModel();
        data.set(aes.encrypt(key.getBytes(), data.bytes()));

        //Convert text based encryption to base64 for easier copy/paste
        if(mode.equals(FileMode.TEXT)) {
            data.set(data.toBase64());
        }
        updateView();
        view.statusBar().setStatus("ENCRYPT SUCCESSFUL");
    }

    public void decrypt (String key) {
        updateModel();
        if(!checkKey(key)) return;
        try {
            if(mode.equals(FileMode.TEXT)) {
                //Text encryption needs to be decoded from b64 first
                data.set(aes.decrypt(key.getBytes(), data.fromBase64()));
            }
            else if (mode.equals(FileMode.BINARY)) {
                data.set(aes.decrypt(key.getBytes(), data.bytes()));
            }
            updateView();

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
