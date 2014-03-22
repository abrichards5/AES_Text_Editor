package controller;

import model.ModelController;
import view.AppFrame;

import javax.swing.*;
import java.io.File;

/**
 * Created by alutman on 17/03/14.
 *
 * Contains all the actions a user can perform.
 *
 */
public class Functions {
    
    private AppFrame view;
    private ModelController model;
    private boolean isModified = false;

    public Functions(AppFrame view, ModelController model) {
        this.view = view;
        this.model = model;
    }

    public void highlightString() {
        String word = view.dialogs().highlightDialog();
        int count = view.getTextArea().highlight(word);
        if(count >= 0) {
            view.statusBar().setStatus(count + " matches");
        }
    }
    public void switchCrypto() {
        model.switchCrypto();
    }

    public void setModified() {
        isModified = true;
    }
    public void switchWordWrap() {
        if(view.getTextArea().getTextWrap()) {
            view.setTextWrap(false);
        }
        else {
            view.setTextWrap(true);
        }
    }
    public void undo() {
        view.getTextArea().undo();
    }

    public void redo() {
        view.getTextArea().redo();
    }

    // Checks to see if the text area has been modified recently and if it has, ask to save the file.
    // The return type is used to see if the user wants to continue on (close the current file)
    public boolean checkSaved() {
        if(isModified) {
            int choice = view.dialogs().confirmCloseFileDialog();
            if(choice  == JOptionPane.YES_OPTION) {
                // Save current file
                return save();
            }
            else if (choice == JOptionPane.CANCEL_OPTION){
                // Keeps current file open
                return false;
            }
            else if (choice == JOptionPane.NO_OPTION) {
                // Don't save current file
                return true;
            }
        }
        return true;
    }
    public void exit() {
        if (checkSaved()) {
            System.exit(0);
        }
    }

    public void encrypt() {
        String key;
        key = view.dialogs().keyDialog();
        model.encrypt(key);
    }
    public void decrypt() {
        String key;
        key = view.dialogs().keyDialog();
        model.decrypt(key);
    }
    public boolean save() {
        // Automatically save as the current filename if it exists, otherwise ask for a filename
        if(view.statusBar().hasFilename()) {
            model.saveFile(new File(view.statusBar().getFilename()));
            isModified = false;
            return true;
        }
        return saveAs();
    }
    public boolean saveAs() {
        File file;
        // Dialog itself asks for overwrite confirm if necessary
        file = view.dialogs().saveFileDialog();
        // Check file has been set
        if(file != null) {
            model.saveFile(file);
            isModified = false;
            return true;
        }
        else {
            return false;
        }
    }
    public void newFile() {
        if(checkSaved()) {
            model.newFile();
            isModified = false;
        }
    }
    public void open() {
        if(checkSaved()) {
            File file;
            file = view.dialogs().openFileDialog();
            if(file != null) {
                model.openFile(file);
                isModified = false;
            }


        }
    }
}
