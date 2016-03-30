package view;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by alutman on 17/03/14.
 *
 * Bottom status bar view
 */
public class StatusBar extends JPanel implements MouseListener {
    private final JLabel fileName = new JLabel(" ");
    private final JLabel status = new JLabel(" ");
    private final JLabel mode = new JLabel(" ");
    private final JLabel encoding = new JLabel(" ");

    public StatusBar () {
        this.setLayout(new GridLayout(2,2));
        this.add(fileName);
        this.add(encoding);
        encoding.setHorizontalAlignment(SwingConstants.RIGHT);

        this.add(status);
        fileName.addMouseListener(this);

        mode.setHorizontalAlignment(SwingConstants.RIGHT);
        this.add(mode);


    }
    public void setMode(String s) {
        if(s.equals("")) {
            mode.setText(" ");
        }
        else {
            mode.setText(s);
        }
    }
    public void setEncoding(String s) {
        if(s.equals("")) {
            encoding.setText(" ");
        }
        else {
            encoding.setText(s);
        }
    }

    public void setStatus(String s, boolean success) {
        if(success) {
            status.setForeground(Color.BLUE);
        }
        else {
            status.setForeground(Color.RED);
        }
        setStatusText(s);
    }
    public void setStatus(String s) {
        // If the status has be cleared, put in a space. This keeps the status bar's size
        status.setForeground(Color.BLACK);
        setStatusText(s);
    }
    private void setStatusText(String s) {
        if(s.equals("")) {
            status.setText(" ");
        }
        else {
            status.setText(s);
        }
    }
    public void setFilename(String s) {
        fileName.setToolTipText(s);
        if(s.equals("")) {
            fileName.setText(" ");
        }
        else {
            fileName.setText(s);
        }
    }
    public String getFilename() {
        return fileName.getText();
    }
    public boolean hasFilename() {
        return !(fileName.getText().equals(" ") || fileName.getText().equals(""));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(fileName.getText().length() > 1) {
            setStatus("COPIED", true);
            StringSelection s = new StringSelection(getFilename());
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(s, s);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(fileName.getText().length() > 1) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }


    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }
}
