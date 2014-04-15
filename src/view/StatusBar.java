package view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by alutman on 17/03/14.
 *
 * Bottom status bar view
 */
public class StatusBar extends JPanel {
    private final JLabel fileName = new JLabel(" ");
    private final JLabel status = new JLabel(" ");
    private final JLabel mode = new JLabel(" ");

    public StatusBar () {
        this.setLayout(new GridLayout(2,1));
        this.add(fileName);

        JPanel lowerBar = new JPanel();
        lowerBar.setLayout(new GridLayout(1,2));

        lowerBar.add(status);

        mode.setHorizontalAlignment(SwingConstants.RIGHT);
        lowerBar.add(mode);

        this.add(lowerBar);
    }
    public void setMode(String s) {
        if(s.equals("")) {
            mode.setText(" ");
        }
        else {
            mode.setText(s);
        }
    }

    public void setStatus(String s) {
        // If the status has be cleared, put in a space. This keeps the status bar's size
        if(s.equals("")) {
            status.setText(" ");
        }
        else {
            status.setText(s);
        }
    }
    public void setFilename(String s) {
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

}
