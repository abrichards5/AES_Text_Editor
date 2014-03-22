package controller;

import view.AppFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Listens to user input from buttons and the keyboard and calls Functions to perform
 * the appropriate action.
 *
 */
public class MenuListener implements ActionListener, DocumentListener, WindowListener{

    private AppFrame view;
    private Functions model;


    public MenuListener(AppFrame appFrame, Functions model) {

        view = appFrame;
        this.model = model;
        // Adds itself as the listener for the view
        view.setListener(this, this, this);
        //buildKeyListener();
        model.newFile();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.statusBar().setStatus("");
        switch(e.getActionCommand()) {
            case "new":
                model.newFile();
                break;
            case "open":
                model.open();
                break;
            case "save":
                model.save();
                break;
            case "saveas":
                model.saveAs();
                break;
            case "exit":
                model.exit();
                break;
            case "undo":
                model.undo();
                break;
            case "redo":
                model.redo();
                break;
            case "find":
                model.highlightString();
                break;
            case "encrypt":
                model.encrypt();
                break;
            case "decrypt":
                model.decrypt();
                break;
            case "wordwrap":
                model.switchWordWrap();
                break;
            case "switchcrypto":
                model.switchCrypto();
                break;
        }
    }

    @Deprecated // Replaced by JMenuItem accelerators
    private void buildKeyListener() {
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        KeyStroke openKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        KeyStroke newKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        KeyStroke encryptKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
        KeyStroke decryptKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
        KeyStroke saveAsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK);
        KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        KeyStroke wrapKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);

        Action saveAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.save();
            }
        };
        Action openAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.open();
            }
        };
        Action newAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.newFile();
            }
        };
        Action encryptAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.encrypt();
            }
        };
        Action decryptAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.decrypt();
            }
        };
        Action saveAsAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.saveAs();
            }
        };
        Action undoAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.undo();

            }
        };
        Action redoAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.redo();
            }
        };
        Action wrapAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                model.switchWordWrap();
            }
        };

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "control s");
        view.getRootPane().getActionMap().put("control s", saveAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(openKeyStroke, "control o");
        view.getRootPane().getActionMap().put("control o", openAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(newKeyStroke, "control n");
        view.getRootPane().getActionMap().put("control n", newAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(encryptKeyStroke, "control e");
        view.getRootPane().getActionMap().put("control e", encryptAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(decryptKeyStroke, "control d");
        view.getRootPane().getActionMap().put("control d", decryptAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveAsKeyStroke, "control shift s");
        view.getRootPane().getActionMap().put("control shift s", saveAsAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "control z");
        view.getRootPane().getActionMap().put("control z", undoAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "control y");
        view.getRootPane().getActionMap().put("control y", redoAction);

        view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(wrapKeyStroke, "control w");
        view.getRootPane().getActionMap().put("control w", wrapAction);


    }


    // These methods are called when the text area is modified. Set modified is used when checking to save.
    // The other lines clear the status and highlight since if the text is being changed, they are probably
    // no longer relevant
    @Override
    public void insertUpdate(DocumentEvent e) {
        model.setModified();
        view.statusBar().setStatus("");
        view.getTextArea().removeHighlight();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        model.setModified();
        view.statusBar().setStatus("");
        view.getTextArea().removeHighlight();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        model.setModified();
        view.statusBar().setStatus("");
        view.getTextArea().removeHighlight();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // Enables ask to save dialog
        model.exit();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
