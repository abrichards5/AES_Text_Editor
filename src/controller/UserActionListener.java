package controller;

import model.enums.Encoding;
import view.AppFrame;
import main.Program;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Created by alutman on 14/03/14.
 *
 * Listens to user input from buttons and the keyboard and calls Functions to perform
 * the appropriate action.
 *
 */
public class UserActionListener implements ActionListener, DocumentListener, WindowListener, DropTargetListener, MouseWheelListener{

    private final AppFrame view;
    private final ViewFunctions model;


    public UserActionListener(AppFrame appFrame, ViewFunctions model) {
        view = appFrame;
        this.model = model;

        // Adds itself as the listener for the view
        try {
            view.setListener(this, this, this, this, this);
        }
        catch (TooManyListenersException tmle) {
            //Shouldn't ever occur
            tmle.printStackTrace();
            System.exit(1);
        }
        //buildKeyListener();
        model.newFile();
    }

    // ActionListener: Listens to menu buttons
    @Override
    public void actionPerformed(ActionEvent e) {
//        view.statusBar().setStatus("");
        switch(e.getActionCommand()) {
            case "new":
                model.newFile();
                break;
            case "open":
                model.open();
                break;
            case "reopen":
                model.reopen();
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
            case "clearhistory":
                model.clearHistory();
                break;
            case "find":
                model.find();
                break;
            case "findnext":
                model.findNext();
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
            case "encbase64":
                model.setEncoding(Encoding.BASE64);
                break;
            case "enchex":
                model.setEncoding(Encoding.HEX);
                break;
            case "encnone":
                model.setEncoding(Encoding.NONE);
                break;
            case "font":
                model.setFont();
                break;
            case "about":
                JOptionPane.showMessageDialog(view, Program.NAME+"\nVersion: "+ Program.VERSION, "Version", JOptionPane.INFORMATION_MESSAGE, view.ICON);
                break;
        }
    }

    // These methods are called when the text area is modified. Set modified is used when checking to save.
    // The other lines clear the status and highlight since if the text is being changed, they are probably
    // no longer relevant

    // DocumentListener: Used only to detect changes in the text
    @Override
    public void insertUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        model.setModified(true);
    }

    // WindowListener: Only needed for exit method
    @Override
    public void windowClosing(WindowEvent e) {
        // Enables ask to save dialog
        model.exit();
    }
    @Override public void windowOpened(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}

    // DropTargetListener: Adds file drop support
    @Override
    public void drop(DropTargetDropEvent dtde) {
        File dropped = null;
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            Transferable transfer = dtde.getTransferable();
            @SuppressWarnings("unchecked") // DataFlavor.javaFileListFlavor is Guaranteed to be List<File> according to Java 7 API on DataFlavor
            List<File> objects = (List<File>)transfer.getTransferData(DataFlavor.javaFileListFlavor);
            dropped = objects.get(0);
        }
        catch (UnsupportedFlavorException nfe) {
            //System.err.println("UserActionListener.drop() : UnsupportedFlavorException");
            //System.exit(1);
            view.statusBar().setStatus("INVALID FILE", false);

        }
        catch (NullPointerException npe) {
            // Shouldn't happen
            System.err.println("UserActionListener.drop() : NullPointerException");
            System.exit(1);
        }
        catch (IOException ioe) {
            System.err.println("UserActionListener.drop() : IOException");
            System.exit(1);
        }
        dtde.dropComplete(true);
        if(dropped != null) {
            model.openDirect(dropped);
        }

    }
    @Override public void dragEnter(DropTargetDragEvent dtde) {}
    @Override public void dragOver(DropTargetDragEvent dtde) {}
    @Override public void dropActionChanged(DropTargetDragEvent dtde) {}
    @Override public void dragExit(DropTargetEvent dte) {}


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.isControlDown()) {
            if(e.getWheelRotation() < 0) {
                view.getTextArea().increaseFontSize();
            }
            else {
                view.getTextArea().decreaseFontSize();
            }
        }
        else {
            //Pass the event on to be handled normally in the scroll pane
            view.getScrollPane().dispatchEvent(e);
        }
    }
}
