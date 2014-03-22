package view;

import javax.swing.*;
import java.io.File;

/**
 * Created by alutman on 17/03/14.
 *
 * Contains all the dialogs used by the program.
 *
 */
public class DialogBuilder {
    
    private AppFrame af;
    FileChooser fileChooser = new FileChooser(this);
    
    public DialogBuilder(AppFrame af) {
        this.af = af;
    }

    public int confirmOverwriteDialog() {
        return JOptionPane.showConfirmDialog(af, "This will overwrite the existing file, are you sure?", "Confirm save", JOptionPane.YES_NO_OPTION);

    }
    public int confirmCloseFileDialog() {
        return JOptionPane.showConfirmDialog(af, "Save", "Save current file?", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public String keyDialog() {
        return JOptionPane.showInputDialog(af,"Enter encryption/decryption KEY",
                "Enter KEY", JOptionPane.PLAIN_MESSAGE);
    }

    public String highlightDialog() {
        return JOptionPane.showInputDialog(af,"Enter string to search for",
                "Enter string", JOptionPane.PLAIN_MESSAGE);
    }


    public File openFileDialog() {
        int status = fileChooser.showOpenDialog(af);
        File selected = fileChooser.getSelectedFile();
        if(fileChooser.getSelectedFile() == null) {
            return null;
        }
        if(status == JFileChooser.APPROVE_OPTION) {
            return selected;
        }
        return null;
    }
    public File saveFileDialog() {
        int status = fileChooser.showSaveDialog(af);
        File selected = fileChooser.getSelectedFile();
        if(fileChooser.getSelectedFile() == null) {
            return null;
        }
        if(status == JFileChooser.APPROVE_OPTION) {
            return selected;
        }
        return null;
    }
}
