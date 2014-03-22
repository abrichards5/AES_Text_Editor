package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by alutman on 17/03/14.
 *
 * Top menu bar view.
 *
 */
public class MenuBar extends JMenuBar {
    private JMenu file = new JMenu("File");
    private JMenuItem open = new JMenuItem("Open");
    private JMenuItem newFile = new JMenuItem("New");
    private JMenuItem save = new JMenuItem("Save");
    private JMenuItem saveAs = new JMenuItem("Save As");
    private JMenuItem exit = new JMenuItem("Exit");

    private JMenu edit = new JMenu("Edit");
    private JMenuItem undo = new JMenuItem("Undo");
    private JMenuItem redo = new JMenuItem("Redo");
    private JMenuItem find = new JMenuItem("Find");

    private JMenu crypto = new JMenu("Crypto");
    private JMenuItem encrypt = new JMenuItem("Encrypt");
    private JMenuItem decrypt = new JMenuItem("Decrypt");

    private JMenu settings = new JMenu("Settings");
    private JMenuItem wordWrap = new JMenuItem();
    private JMenuItem switchCrypto = new JMenuItem();

    public MenuBar() {
        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(exit);
        this.add(file);

        edit.add(undo);
        edit.add(redo);
        edit.add(find);
        this.add(edit);

        crypto.add(encrypt);
        crypto.add(decrypt);
        this.add(crypto);

        settings.add(wordWrap);
        settings.add(switchCrypto);
        this.add(settings);

        this.setVisible(true);
        this.setEnabled(true);
    }
    public void setListener(ActionListener al) {
        //FILE
        file.setMnemonic('f');

        newFile.setMnemonic('n');
        newFile.setActionCommand("new");
        newFile.addActionListener(al);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

        open.setMnemonic('o');
        open.setActionCommand("open");
        open.addActionListener(al);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

        save.setMnemonic('s');
        save.setActionCommand("save");
        save.addActionListener(al);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        saveAs.setMnemonic('a');
        saveAs.setActionCommand("saveas");
        saveAs.addActionListener(al);
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));

        exit.setMnemonic('x');
        exit.setActionCommand("exit");
        exit.addActionListener(al);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));

        //EDIT
        edit.setMnemonic('e');

        undo.setMnemonic('u');
        undo.setActionCommand("undo");
        undo.addActionListener(al);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        redo.setMnemonic('r');
        redo.setActionCommand("redo");
        redo.addActionListener(al);
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        find.setMnemonic('f');
        find.setActionCommand("find");
        find.addActionListener(al);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));

        //CRYPTO
        crypto.setMnemonic('c');

        encrypt.setMnemonic('e');
        encrypt.setActionCommand("encrypt");
        encrypt.addActionListener(al);
        encrypt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

        decrypt.setMnemonic('d');
        decrypt.setActionCommand("decrypt");
        decrypt.addActionListener(al);
        decrypt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));

        //SETTINGS
        settings.setMnemonic('s');

        wordWrap.setMnemonic('w');
        wordWrap.setActionCommand("wordwrap");
        wordWrap.addActionListener(al);
        wordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));

        switchCrypto.setMnemonic('y');
        switchCrypto.setActionCommand("switchcrypto");
        switchCrypto.addActionListener(al);
        switchCrypto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));
    }

    public void setWordWrapText(String s) {
        wordWrap.setText(s);
    }
    public void setSwitchCryptoText(String s) {
        switchCrypto.setText(s);
    }
}
