package controller;

import model.BaseModel;
import model.Cryptographer;
import model.enums.CryptStatus;
import model.enums.FileCheck;
import model.enums.FileMode;
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
    }

    public void newFile() {
        mode = FileMode.TEXT;
        view.setTextMode();
        view.getTextArea().setText("");
        view.statusBar().setFilename("");
        view.setTitle("");
    }

    public FileCheck openFile(File file) {
        FileCheck fc = null;
        try {
            fc = super.openFile(file);
        } catch(IOException ioe) {
            view.statusBar().setStatus("OPEN FAILED");
            return FileCheck.ERROR;
        }
        String status = null;
        if(fc.equals(FileCheck.TEXT_FILE)) {
            view.setTextMode();
            status = "OPEN SUCCESSFUL";
        }
        else if(fc.equals(FileCheck.BINARY_FILE)) {
            view.setBinaryMode();
            status = "BINARY OPEN SUCCESSFUL";
        }
        else if(fc.equals(FileCheck.LARGE_FILE)) {
            status = "FILE TOO LARGE";
        }
        else if(fc.equals(FileCheck.ERROR)) {
            status = "ERROR IN OPENING";
        }

        updateView();
        view.statusBar().setFilename(file.getAbsolutePath());
        view.setTitle(file.getName());
        view.statusBar().setStatus(status);
        return FileCheck.ERROR;
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
        if(mode.equals(FileMode.TEXT)) {
            view.getTextArea().setText(getData().text());
        }
        //Notifies change in underlying data
        else if(mode.equals(FileMode.BINARY)) {
            view.getTextArea().setText(view.getTextArea().getText());
        }
    }
    //Pull data from view to model
    private void updateModel() {
        if(mode.equals(FileMode.TEXT)){
            getData().set(view.getTextArea().getText());
        }
    }

    public CryptStatus encrypt(String key) {
        updateModel();
        String result = super.encrypt(key).toString();
        updateView();
        view.statusBar().setStatus(result);
        return null;
    }

    public CryptStatus decrypt (String key) {
        updateModel();
        String result = super.decrypt(key).toString();
        updateView();
        view.statusBar().setStatus(result);
        return null;
    }

}
