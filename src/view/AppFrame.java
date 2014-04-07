package view;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Base view of the program.
 *
 */
public class AppFrame extends JFrame {

    private MenuBar menu = new MenuBar();

    private TextArea textArea = new TextArea();
    private JScrollPane textScroll = new JScrollPane(textArea);
    private DialogBuilder dialogs = new DialogBuilder(this);

    private StatusBar statusBar = new StatusBar();


    public AppFrame() {
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTextWrap(false);
        setTitle("");
        makeLayout();

        this.setEnabled(true);
        this.setVisible(true);
    }
    public void setTitle(String t) {
        super.setTitle("AES Text Editor - " + t);
    }
    public DialogBuilder dialogs() {
        return dialogs;
    }
    public void setListener(ActionListener al, DocumentListener dl, WindowListener wl) {
        menu.setListener(al);
        textArea.setListener(dl);
        this.addWindowListener(wl);
    }

    private void makeLayout() {
        this.setLayout(new BorderLayout());
        this.add(menu, BorderLayout.NORTH);
        this.add(statusBar, BorderLayout.SOUTH);
        this.add(textScroll, BorderLayout.CENTER);
    }

    public void setTextWrap(boolean b) {
        // Change the button text to represent the current word wrap mode.
        if(b) {
            menu.setWordWrapText("Disable text wrap");
        }
        else {
            menu.setWordWrapText("Enable text wrap");
        }
        textArea.setTextWrap(b);
    }
    public TextArea getTextArea() {
        return textArea;
    }
    public StatusBar statusBar() {
        return statusBar;
    }

    public void setBinaryMode() {
        textArea.setText(" --- Binary file detected: Editing disabled ---");
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);
    }
    public void setTextMode() {
        textArea.setEditable(true);
        textArea.setBackground(Color.WHITE);
    }




}
