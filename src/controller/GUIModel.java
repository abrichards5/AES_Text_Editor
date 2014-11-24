package controller;

import model.BaseModel;
import model.Cryptographer;
import model.enums.CryptStatus;
import model.enums.Encoding;
import model.enums.FileStatus;
import view.AppFrame;

import java.io.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Extends the BaseModel class for integration with GUI
 *
 */
public class GUIModel extends BaseModel {

    private final AppFrame view;

    public GUIModel(AppFrame af, Cryptographer aes) {
        super(aes);
        view = af;
        cryptoUpdateGUI();
    }

    private void cryptoUpdateGUI() {
        String curMode = getCurrentCryptoMode();
        view.statusBar().setMode(curMode);
        view.statusBar().setEncoding(getEncoding().toString());
    }

    public void newFile() {
        super.getData().set("");
        view.setTextMode();
        view.getTextArea().setText("");
        view.statusBar().setFilename("");
        view.setTitle("");
    }

    public FileStatus openFile(File file) {
        FileStatus fc = null;
        try {
            fc = super.openFile(file);
        } catch(IOException ioe) {
            view.statusBar().setStatus("OPEN FAILED");
            return FileStatus.ERROR;
        } catch(OutOfMemoryError oome) {
            view.dialogs().errorDialog("Out of Memory! File too large");
            return FileStatus.ERROR;
        }

        String status = null;
        if(fc.equals(FileStatus.TEXT_FILE)) {
            view.setTextMode();
            status = "OPEN SUCCESSFUL";
        }
        else if(fc.equals(FileStatus.BINARY_FILE)) {
            view.setBinaryMode();
            status = "BINARY OPEN SUCCESSFUL";
        }
        else if(fc.equals(FileStatus.ERROR)) {
            status = "ERROR IN OPENING";
        }

        updateView(true);
        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitle(file.getName());
        view.statusBar().setStatus(status);
        view.statusBar().setEncoding(getEncoding().toString());
        return fc;
    }

    public void saveFile(File filename) {
        updateModel();
        try {
            super.saveFile(filename);
        } catch(IOException ioe) {
            view.statusBar().setStatus("SAVE FAILED");
            return;
        }

        view.statusBar().setFilename(filename.getAbsolutePath());
        view.setTitle(filename.getName());
        view.statusBar().setStatus("SAVE SUCCESSFUL");
    }

    private void updateView() {
        updateView(false);
    }
    //Push data from model to view
    private void updateView(boolean discardEdits) {
        if(getData().getMode().equals(FileStatus.TEXT_FILE)) {
            view.getTextArea().setText(getData().text(), discardEdits);
        }
        //Notifies change in underlying data
        else if(getData().getMode().equals(FileStatus.BINARY_FILE)) {
            view.getTextArea().setText(view.getTextArea().getText(), discardEdits);
        }
    }
    //Pull data from view to model
    private void updateModel() {
        if(getData().getMode().equals(FileStatus.TEXT_FILE)) {
            getData().set(view.getTextArea().getText());
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
            if (getData().getMode().equals(FileStatus.BINARY_FILE)) {
                view.setBinaryMode();
            } else {
                view.setTextMode();
            }
            updateView();
        }
        view.statusBar().setStatus(result.toString());
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
            if(getData().getMode().equals(FileStatus.BINARY_FILE)) {
                view.setBinaryMode();
            }
            else {
                view.setTextMode();
            }
            updateView();
        }
        view.statusBar().setStatus(result.toString());
        return null;
    }

}
