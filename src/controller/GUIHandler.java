package controller;

import model.CryptographerHandler;
import model.enums.Encoding;
import model.enums.FileStatus;
import model.Cryptographer;
import model.enums.CryptStatus;
import view.AppFrame;

import java.io.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Extends the BaseModel class for integration with GUI
 *
 */
public class GUIHandler extends CryptographerHandler {

    private final AppFrame view;

    private File currentFile = null;

    public GUIHandler(AppFrame af, Cryptographer aes) {
        super(aes);
        view = af;
        cryptoUpdateGUI();
    }

    private void cryptoUpdateGUI() {
        String curMode = getCryptoMode();
        view.statusBar().setMode(curMode);
        view.statusBar().setEncoding(getEncoding().toString());
    }

    public void newFile() {
        super.setText("");
        view.setTextMode();
        view.getTextArea().setText("");
        view.statusBar().setFilename("");
        view.setTitleFilename("");
        currentFile = null;
    }

    public boolean hasFile() {
        return currentFile != null;
    }

    public FileStatus reopen() {
        if(hasFile()) {
            return openFile(currentFile);
        }
        else {
            return FileStatus.ERROR;
        }

    }

    public FileStatus openFile(File file) {
        FileStatus fc = null;
        try {
            fc = super.openFile(file);
        } catch(IOException ioe) {
            view.statusBar().setStatus("OPEN FAILED", false);
            return FileStatus.ERROR;
        } catch(OutOfMemoryError oome) {
            view.dialogs().errorDialog("Out of Memory! File too large");
            return FileStatus.ERROR;
        }
        boolean success = false;
        String status = null;
        if(fc.equals(FileStatus.TEXT_FILE)) {
            view.setTextMode();
            status = "OPEN SUCCESSFUL";
            currentFile = file;
            success = true;
        }
        else if(fc.equals(FileStatus.BINARY_FILE)) {
            view.setBinaryMode();
            status = "BINARY OPEN SUCCESSFUL";
            currentFile = file;
            success = true;
        }
        else if(fc.equals(FileStatus.ERROR)) {
            status = "ERROR IN OPENING";
            success = false;
        }

        updateView(true);
        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitleFilename(file.getName());
        view.statusBar().setStatus(status, success);
        view.statusBar().setEncoding(getEncoding().toString());
        return fc;
    }

    public void saveFile(File file) {
        updateModel();
        try {
            super.saveFile(file);
        } catch(IOException ioe) {
            view.statusBar().setStatus("SAVE FAILED", false);
            return;
        }

        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitleFilename(file.getName());
        view.statusBar().setStatus("SAVE SUCCESSFUL", true);
        currentFile = file;
    }

    private void updateView() {
        updateView(false);
    }
    //Push data from model to view
    private void updateView(boolean discardEdits) {
        if(getDataMode().equals(FileStatus.TEXT_FILE)) {
            view.getTextArea().setText(getText(), discardEdits);
        }
        //Notifies change in underlying data
        else if(getDataMode().equals(FileStatus.BINARY_FILE)) {
            view.getTextArea().setText(view.getTextArea().getText(), discardEdits);
        }
    }
    //Pull data from view to model
    private void updateModel() {
        if(getDataMode().equals(FileStatus.TEXT_FILE)) {
            setText(view.getTextArea().getText());
        }
    }

    @Override
    public void setEncoding(Encoding e) {
        super.setEncoding(e);
        view.statusBar().setEncoding(getEncoding().toString());
    }


    public CryptStatus encrypt(String key) {
        updateModel();
        CryptStatus result = CryptStatus.ENCRYPT_FAILED;
        try {
            result = super.encrypt(key);
        } catch (OutOfMemoryError oome) {
            view.dialogs().errorDialog("Out of Memory! File too large");
        }
        if(result.equals(CryptStatus.ENCRYPT_SUCCESS)) {
            if (getDataMode().equals(FileStatus.BINARY_FILE)) {
                view.setBinaryMode();
            } else {
                view.setTextMode();
            }
            updateView();
            view.statusBar().setStatus(result.toString(), true);
        }
        else {
            view.statusBar().setStatus(result.toString(), false);
        }

        return null;
    }

    public CryptStatus decrypt (String key) {
        updateModel();
        CryptStatus result = CryptStatus.DECRYPT_FAILED;
        try {
            result = super.decrypt(key);
        } catch (OutOfMemoryError oome) {
            view.dialogs().errorDialog("Out of Memory! File too large");
        }
        if(result.equals(CryptStatus.DECRYPT_SUCCESS)) {
            if(getDataMode().equals(FileStatus.BINARY_FILE)) {
                view.setBinaryMode();
            }
            else {
                view.setTextMode();
            }
            updateView();
            view.statusBar().setStatus(result.toString(), true);
        }
        else {
            view.statusBar().setStatus(result.toString(), false);
        }

        return null;
    }

}
