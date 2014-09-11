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
        else if(fc.equals(FileStatus.LARGE_FILE)) {
            status = "FILE TOO LARGE";
        }
        else if(fc.equals(FileStatus.ERROR)) {
            status = "ERROR IN OPENING";
        }

        updateView();
        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitle(file.getName());
        view.statusBar().setStatus(status);
        view.statusBar().setEncoding(getEncoding().toString());
        return FileStatus.ERROR;
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

    //Push data from model to view
    private void updateView() {
        if(getData().getMode().equals(FileStatus.TEXT_FILE)) {
            view.getTextArea().setText(getData().text());
        }
        //Notifies change in underlying data
        else if(getData().getMode().equals(FileStatus.BINARY_FILE)) {
            view.getTextArea().setText(view.getTextArea().getText());
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
        String result = super.encrypt(key).toString();
        if(getData().getMode().equals(FileStatus.BINARY_FILE)) {
            view.setBinaryMode();
        }
        else {
            view.setTextMode();
        }
        updateView();
        view.statusBar().setStatus(result);
        return null;
    }

    public CryptStatus decrypt (String key) {
        updateModel();
        String result = super.decrypt(key).toString();
        if(getData().getMode().equals(FileStatus.BINARY_FILE)) {
            view.setBinaryMode();
        }
        else {
            view.setTextMode();
        }
        updateView();
        view.statusBar().setStatus(result);
        return null;
    }

}
