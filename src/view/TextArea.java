package view;
import view.data.FindParams;
import com.eaio.stringsearch.BNDMCI;
import com.eaio.stringsearch.BoyerMooreHorspoolRaita;
import view.data.Match;

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
import java.util.ArrayList;
import java.util.regex.*;

/*
 * Created by alutman on 17/03/14.
 *
 * Centre text area view
 *
 */
public class TextArea extends JTextArea {
    private final UndoManager undoMan = new UndoManager();
    private final Document thisDoc;

    public static final Font DEFAULT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final int[] DEFAULT_FONT_SIZES =
            {
                    8, 9, 10, 11, 12, 14, 16, 18, 20,
                    22, 24, 26, 28, 36, 48, 72,
            };
    public static final int DEFAULT_TAB_SIZE = 4;
    private static final Color HIGHLIGHT_COLOUR = Color.YELLOW;

    public static final int NULL_WORD = -2;
    public static final int INVALID_REGEX = -3;

    private ArrayList<Match> matches = new ArrayList<>();

    public TextArea() {
        thisDoc = this.getDocument();
        this.setDragEnabled(true);
        thisDoc.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoMan.addEdit(e.getEdit());
            }
        });
        this.setFont(DEFAULT_FONT);
        this.setTabSize(DEFAULT_TAB_SIZE);
    }
    

    public void increaseFontSize() {
        for(int i = 0; i < DEFAULT_FONT_SIZES.length; i++){
            if(DEFAULT_FONT_SIZES[i] > this.getFont().getSize()) {
                this.setFont(new Font(this.getFont().getFamily(), this.getFont().getStyle(), DEFAULT_FONT_SIZES[i]));
                return;
            }
        }
    }
    public void decreaseFontSize() {
        for(int i = DEFAULT_FONT_SIZES.length -1; i >= 0; i--){
            if(DEFAULT_FONT_SIZES[i] < this.getFont().getSize()) {
                this.setFont(new Font(this.getFont().getFamily(), this.getFont().getStyle(), DEFAULT_FONT_SIZES[i]));
                return;
            }
        }
    }

    public boolean undo() {
        if (undoMan.canUndo()) {
            undoMan.undo();
            return true;
        }
        return false;
    }
    public void redo() {
        if (undoMan.canRedo()) {
            undoMan.redo();
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.setCaretPosition(0);

    }
    public void setText(String text, boolean discardEdits) {
        this.setText(text);
        // Set text means a new file is being opened. Don't want to undo into a previous file's data
        if(discardEdits) undoMan.discardAllEdits();
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
        matches.clear();
    }

    // Uses http://johannburkard.de/software/stringsearch/
    // to search the data. MUCH faster with large files
    private int highlightRegex(String regex, boolean caseSensitive) {
        //Set up
        //Find match; highlight
        //while offset != end; find match; highlight

        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(HIGHLIGHT_COLOUR);
        Pattern p;
        try {
            p = Pattern.compile(regex, caseSensitive ? Pattern.UNICODE_CASE : Pattern.CASE_INSENSITIVE);
        } catch(PatternSyntaxException pse) {
            return INVALID_REGEX;
        }

        Matcher m = p.matcher(getText());

        boolean first = true;
        while (m.find()) {
            try {
                if(first) {
                    this.setCaretPosition(m.start() + (m.end()-m.start()));
                    this.setCaretPosition(m.start());
                    first = false;
                }
                this.getHighlighter().addHighlight(m.start(), m.end(), painter);
                matches.add(new Match(m.start(), m.end()));
            }
            catch(BadLocationException ble){ble.printStackTrace();}
        }
        return matches.size();
    }
    private int highlightNormal(String word, boolean caseSensitive) {
        String data = this.getText();
        int offset = 0;
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(HIGHLIGHT_COLOUR);
        BoyerMooreHorspoolRaita bmhr = new BoyerMooreHorspoolRaita();
        BNDMCI bndmci = new BNDMCI();

        boolean first = true;
        offset = caseSensitive ? bmhr.searchString(data, offset, word) : bndmci.searchString(data, offset, word);
        while(offset != -1) {
            try {
                if(first) {
                    this.setCaretPosition(offset + word.length());
                    this.setCaretPosition(offset);
                    first = false;
                }
                matches.add(new Match(offset, offset+word.length()));
                this.getHighlighter().addHighlight(offset, offset + word.length(), painter);
                offset = caseSensitive ? bmhr.searchString(data, offset+1, word) : bndmci.searchString(data, offset+1, word);

            }
            catch (BadLocationException ble) { ble.printStackTrace(); }
        }
        return matches.size();
    }

    public int highlight(FindParams params) {
        removeHighlight();
        if(params.word == null || params.word.length() == 0) {
            //Trying to highlight "" goes into an infinite loop and ends up using GBs of RAM
            return NULL_WORD;
        }
        return params.useRegex ? highlightRegex(params.word, params.caseSensitive) : highlightNormal(params.word, params.caseSensitive);
    }

    public int findNext() {
        for(int i = 0; i < matches.size(); i++) {
            Match p = matches.get(i);
            if(p.start > this.getCaretPosition()) {
                this.setCaretPosition(p.start);
                return p.start;
            }
        }
        if(matches.size() > 0) {
            this.setCaretPosition(matches.get(0).start);
            return matches.get(0).start;
        }
        return -1;
    }

}
