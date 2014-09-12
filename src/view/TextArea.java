package view;
import com.eaio.stringsearch.BoyerMooreHorspoolRaita;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;
import java.awt.*;

/*
 * Created by alutman on 17/03/14.
 *
 * Centre text area view
 *
 */
public class TextArea extends JTextArea {
    private final UndoManager undoMan = new UndoManager();
    private final Document thisDoc;

    public TextArea() {
        thisDoc = this.getDocument();
        this.setDragEnabled(true);
        thisDoc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoMan.addEdit(e.getEdit());
            }
        });
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        this.setTabSize(4);
    }

    public void undo() {
        if (undoMan.canUndo()) {
            undoMan.undo();
        }
    }
    public void redo() {
        if (undoMan.canRedo()) {
            undoMan.redo();
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        // Set text means a new file is being opened. Don't want to undo into a previous file's data
        undoMan.discardAllEdits();
    }
    public void setListener(DocumentListener dl) {
        thisDoc.addDocumentListener(dl);

    }
    public void setTextWrap(boolean b) {
        this.setLineWrap(b);
        this.setWrapStyleWord(b);
    }
    public boolean getTextWrap() {
        return this.getLineWrap();
    }
    public void removeHighlight() {
        getHighlighter().removeAllHighlights();
    }

    // Uses http://johannburkard.de/software/stringsearch/
    // to search the data. MUCH faster with large files
    public int highlight(String searchWord) {
        removeHighlight();
        if(searchWord == null || searchWord.equals("")) {
            //Trying to highlight "" goes into an infinite loop and ends up using GBs of RAM
            return -1;
        }
        int offset = 0;
        int count = 0;
        String data = getText();


        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        BoyerMooreHorspoolRaita bmhr = new BoyerMooreHorspoolRaita();
        offset = bmhr.searchString(data, offset, searchWord);
        this.setCaretPosition(offset+searchWord.length());
        this.setCaretPosition(offset);
        while(offset != -1) {
            try {
                this.getHighlighter().addHighlight(offset, offset + searchWord.length(), painter);
                offset = bmhr.searchString(data, offset+1, searchWord);
                count++;
            }
            catch (BadLocationException ble) { ble.printStackTrace(); }
        }
        return count;

    }

}
