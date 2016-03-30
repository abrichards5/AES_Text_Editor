package view;

import view.data.FindParams;
import controller.exception.InputCancelledException;
import say.swing.JFontChooser;

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

    private static final int DEFAULT_INPUT_WIDTH = 34;

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
        int selected;
        try {
            selected = Integer.parseInt(pane.getValue().toString());
        } catch (NullPointerException npe) {
            throw new InputCancelledException();
        }

        if (selected == JOptionPane.OK_OPTION) {
            return new String(pass.getPassword());
        }
        else if (selected == JOptionPane.CANCEL_OPTION) {
            //Cannot differentiate between null input and cancel downstream using only the return value
            throw new InputCancelledException();
        }
        else if (selected == JOptionPane.CLOSED_OPTION) {
            //Handle pressing ESC
            throw new InputCancelledException();
        }
        return null;
    }

    private FindParams lastFindSettings = new FindParams();

    public FindParams advancedHighlightDialog() {
        JCheckBox caseBox = new JCheckBox("Case sensitive");
        caseBox.setSelected(lastFindSettings.caseSensitive);
        JCheckBox regexBox = new JCheckBox("Use regex");
        regexBox.setSelected(lastFindSettings.useRegex);

        JTextField stringBox = new JTextField(lastFindSettings.word);
        stringBox.addAncestorListener( new RequestFocusListener() );

        stringBox.setFont(view.TextArea.DEFAULT_FONT);
        stringBox.setColumns(DEFAULT_INPUT_WIDTH);

        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(2,1));

        JPanel checkPanel = new JPanel();
        checkPanel.add(caseBox);
        checkPanel.add(Box.createHorizontalStrut(15)); // a spacer
        checkPanel.add(regexBox);
        myPanel.add(checkPanel);

        JPanel stringPanel = new JPanel();
        stringPanel.add(stringBox);
        myPanel.add(stringPanel);

        stringBox.selectAll();

        int option = JOptionPane.showConfirmDialog(af, myPanel, "Find", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(option == JOptionPane.OK_OPTION) {
            lastFindSettings = new FindParams(stringBox.getText(), caseBox.isSelected(), regexBox.isSelected());
            return lastFindSettings;
        }
        else {
            return null;
        }

    }
    public Font fontDialog() {
        JFontChooser jfc = new JFontChooser();
        jfc.setSelectedFont(af.getTextArea().getFont());
        int returnValue = jfc.showDialog(af);
        if(returnValue == JFontChooser.OK_OPTION) {
            return jfc.getSelectedFont();
        } else if(returnValue == JFontChooser.DEFAULT_OPTION) {
            return view.TextArea.DEFAULT_FONT;
        }
        return null;
    }


    public void setFileDialogDirectory(File f) {
        fileChooser.setCurrentDirectory(f);
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

    public void errorDialog(String message) {
        JOptionPane.showMessageDialog(af, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
