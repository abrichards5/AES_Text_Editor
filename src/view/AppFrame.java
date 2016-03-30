package view;

import main.Program;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.util.TooManyListenersException;

/**
 * Created by alutman on 14/03/14.
 *
 * Base view of the program.
 *
 */
public class AppFrame extends JFrame {

    private final view.MenuBar menu = new view.MenuBar();
    private final view.TextArea textArea = new view.TextArea();
    private final JScrollPane textScroll = new JScrollPane(textArea);
    private final DialogBuilder dialogs = new DialogBuilder(this);

    private final StatusBar statusBar = new StatusBar();

    public ImageIcon ICON = null;


    public AppFrame() {
        this.setSize(500, 500);
        this.setMinimumSize(new Dimension(250,200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTextWrap(false);
        setTitleFilename("");
        makeLayout();

        ICON = new ImageIcon(getClass().getClassLoader().getResource("image/encrypted-icon-64.png"));

        this.setIconImage(ICON.getImage());
        this.setEnabled(true);
        this.setVisible(true);
    }

    String filename = "untitled";

    public void setTitleFilename(String filename) {
        if(filename == null || filename.length() <= 0) {
            this.filename = "untitled";
        }
        else {
            this.filename = filename;
        }
        super.setTitle(this.filename+" - "+ Program.NAME);
    }
    public void setTitleModified(boolean b) {
        if(b) {
            super.setTitle(filename+" * - "+Program.NAME);
        }
        else {
            super.setTitle(filename+" - "+Program.NAME);
        }

    }
    public DialogBuilder dialogs() {
        return dialogs;
    }
    public void setListener(ActionListener al, DocumentListener dl, WindowListener wl, DropTargetListener dtl, MouseWheelListener mwl) throws TooManyListenersException {
        menu.setListener(al);
        textArea.setListener(dl);
        textArea.addMouseWheelListener(mwl);
        this.addWindowListener(wl);
        DropTarget dt = new DropTarget();
        dt.addDropTargetListener(dtl);
        textArea.setDropTarget(dt);
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
    public view.TextArea getTextArea() {
        return textArea;
    }
    public JScrollPane getScrollPane() { return textScroll; }
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
