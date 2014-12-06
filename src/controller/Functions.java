package controller;

import controller.exception.InputCancelledException;
import model.enums.Encoding;
import view.AppFrame;
import view.data.FindParams;
import view.TextArea;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by alutman on 17/03/14.
 *
 * Contains all the actions a user can perform.
 *
 */
public class Functions {
    
    private final AppFrame view;
    private final GUIModel model;
    private boolean isModified = false;

    public Functions(AppFrame view, GUIModel model) {
        this.view = view;
        this.model = model;
    }

    public void find() {
        FindParams params = view.dialogs().advancedHighlightDialog();
        if(params == null) { return; }
        int count = view.getTextArea().highlight(params);
        if (count >= 0) {
            view.statusBar().setStatus(count + " matches");
        }
        else if(count == TextArea.INVALID_REGEX) {
            view.statusBar().setStatus("INVALID REGEX");
        }
        else {
            view.statusBar().setStatus(modifiedStatus());
        }

    }

    public void findNext() {
        view.getTextArea().findNext();
    }
    public String modifiedStatus() {
        return isModified ? "MODIFIED" : "";
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
        if(!view.getTextArea().undo()) {
            isModified = false;
            view.statusBar().setStatus("");
        }
    }

    public void redo() {
        view.getTextArea().redo();
    }

    // Checks to see if the text area has been modified recently and if it has, ask to save the file.
    // The return type is used to see if the user wants to continue on (close the current file)
    boolean checkSaved() {
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
        String key = null;
        try {
            key = view.dialogs().keyDialog();
        } catch (InputCancelledException e) {
            return;
        }
        model.encrypt(key);
    }
    public void decrypt() {
        String key = null;
        try {
            key = view.dialogs().keyDialog();
        } catch (InputCancelledException e) {
            return;
        }
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
    public void openDirect(File f) {
        if(checkSaved()) {
            model.openFile(f);
            isModified = false;
        }
    }
    public void setFont() {
        Font f = view.dialogs().fontDialog();
        if(f != null) {
            view.getTextArea().setFont(f);
        }
    }


    public void setEncoding(Encoding e) {
        model.setEncoding(e);
    }
}
