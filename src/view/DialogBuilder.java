package view;

import controller.exception.InputCancelledException;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by alutman on 17/03/14.
 *
 * Contains all the dialogs used by the program.
 *
 */
public class DialogBuilder {
    
    private final AppFrame af;
    private final FileChooser fileChooser = new FileChooser(this);
    
    public DialogBuilder(AppFrame af) {
        this.af = af;
    }

    public int confirmOverwriteDialog() {
        return JOptionPane.showConfirmDialog(af, "This will overwrite the existing file, are you sure?", "Confirm save", JOptionPane.YES_NO_OPTION);

    }
    public int confirmCloseFileDialog() {
        return JOptionPane.showConfirmDialog(af, "Save", "Save current file?", JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public String keyDialog() throws InputCancelledException {
        return hiddenInputDialog("Enter KEY", "Enter encryption/decryption KEY");
    }

    String hiddenInputDialog(String title, String message) throws InputCancelledException {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(message);
        final JPasswordField pass = new JPasswordField();
        panel.setLayout(new GridLayout(2,1));
        panel.add(label);
        panel.add(pass);

        // Use the customized JPanel panel
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
            @Override // Sets focus to the input field when the dialog displays
            public void selectInitialValue() {
                pass.requestFocusInWindow();
            }
        };

        pane.createDialog(af, title).setVisible(true);
        int selected = Integer.parseInt(pane.getValue().toString());

        if (selected == JOptionPane.OK_OPTION) {
            return new String(pass.getPassword());
        }
        else if (selected == JOptionPane.CANCEL_OPTION) {
            //Cannot differentiate between null input and cancel downstream using only the return value
            throw new InputCancelledException();
        }
        return null;
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
