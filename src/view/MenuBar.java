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
class MenuBar extends JMenuBar {
    private final JMenu file = new JMenu("File");
    private final JMenuItem open = new JMenuItem("Open");
    private final JMenuItem newFile = new JMenuItem("New");
    private final JMenuItem save = new JMenuItem("Save");
    private final JMenuItem saveAs = new JMenuItem("Save As");
    private final JMenuItem exit = new JMenuItem("Exit");

    private final JMenu edit = new JMenu("Edit");
    private final JMenuItem undo = new JMenuItem("Undo");
    private final JMenuItem redo = new JMenuItem("Redo");
    private final JMenuItem find = new JMenuItem("Find");

    private final JMenu crypto = new JMenu("Crypto");
    private final JMenuItem encrypt = new JMenuItem("Encrypt");
    private final JMenuItem decrypt = new JMenuItem("Decrypt");

    private final JMenu settings = new JMenu("Settings");
    private final JMenuItem wordWrap = new JMenuItem();

    private final JMenu encoding = new JMenu("Encoding");
    private final JMenuItem encBase64 = new JMenuItem("Base 64");
    private final JMenuItem encHex = new JMenuItem("Hex");
    private final JMenuItem encNone = new JMenuItem("None");

    private final JMenu help = new JMenu("Help");
    private final JMenuItem about = new JMenuItem("About");


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
        this.add(settings);
        settings.add(encoding);
        encoding.add(encBase64);
        encoding.add(encHex);
        encoding.add(encNone);

        this.add(help);
        help.add(about);

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

        encoding.setMnemonic('e');

        encBase64.setMnemonic('6');
        encBase64.setActionCommand("encbase64");
        encBase64.addActionListener(al);

        encHex.setMnemonic('h');
        encHex.setActionCommand("enchex");
        encHex.addActionListener(al);

        encNone.setMnemonic('n');
        encNone.setActionCommand("encnone");
        encNone.addActionListener(al);

        help.setMnemonic('h');

        about.setMnemonic('a');
        about.setActionCommand("about");
        about.addActionListener(al);
    }

    public void setWordWrapText(String s) {
        wordWrap.setText(s);
    }
}
