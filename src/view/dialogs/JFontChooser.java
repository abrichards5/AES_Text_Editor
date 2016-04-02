//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/*
 Taken from http://jfontchooser.sourceforge.jp/site/jfontchooser/project-summary.html
 This is a decompiled version as I lost my modified source of the original

 Changes:

 * Added a default button that selects monospaced font
 * All font sizes and dimensions are scaled according to the DPI

*/

package view.dialogs;

import view.DPIController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.MissingResourceException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position.Bias;

public class JFontChooser extends JComponent {
    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    public static final int ERROR_OPTION = -1;
    public static final int DEFAULT_OPTION = 2;
    private static final Font DEFAULT_SELECTED_FONT = new Font("Serif", 0, DPIController.scaleToDPI(12));
    private static final Font DEFAULT_FONT = new Font("Dialog", 0, DPIController.scaleToDPI(10));
    private static final int[] FONT_STYLE_CODES = new int[]{0, 1, 2, 3};
    private static final String[] DEFAULT_FONT_SIZE_STRINGS = new String[]{"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72"};
    protected int dialogResultValue;

    private HashMap<String, String> messageCatalog;
    private String[] fontStyleNames;
    private String[] fontFamilyNames;
    private String[] fontSizeStrings;
    private JTextField fontFamilyTextField;
    private JTextField fontStyleTextField;
    private JTextField fontSizeTextField;
    private JList fontNameList;
    private JList fontStyleList;
    private JList fontSizeList;
    private JPanel fontNamePanel;
    private JPanel fontStylePanel;
    private JPanel fontSizePanel;
    private JPanel samplePanel;
    private JTextField sampleText;

    protected String _(String var1) {
        String var2 = var1;

        try {
            var2 = this.messageCatalog.get(var1);
        } catch (MissingResourceException var4) {
            ;
        }

        return var2;
    }

    public JFontChooser() {
        this(DEFAULT_FONT_SIZE_STRINGS);
    }

    public JFontChooser(String[] var1) {
        this.dialogResultValue = -1;
        this.messageCatalog = new HashMap<>();

        this.messageCatalog.put("Version", "1.0.5b");
        this.messageCatalog.put("SelectFont", "Select Font");
        this.messageCatalog.put("OK", "OK");
        this.messageCatalog.put("Cancel", "Cancel");
        this.messageCatalog.put("FontName", "Font Name");
        this.messageCatalog.put("FontStyle", "Style");
        this.messageCatalog.put("FontSize", "Size");
        this.messageCatalog.put("Sample", "Sample");
        this.messageCatalog.put("SampleString", "AaBbYyZz");
        this.messageCatalog.put("Plain", "Plain");
        this.messageCatalog.put("Bold", "Bold");
        this.messageCatalog.put("Italic", "Italic");
        this.messageCatalog.put("BoldItalic", "Bold Italic");
        this.messageCatalog.put("Default", "Default");



        this.fontStyleNames = null;
        this.fontFamilyNames = null;
        this.fontSizeStrings = null;
        this.fontFamilyTextField = null;
        this.fontStyleTextField = null;
        this.fontSizeTextField = null;
        this.fontNameList = null;
        this.fontStyleList = null;
        this.fontSizeList = null;
        this.fontNamePanel = null;
        this.fontStylePanel = null;
        this.fontSizePanel = null;
        this.samplePanel = null;
        this.sampleText = null;
        if(var1 == null) {
            var1 = DEFAULT_FONT_SIZE_STRINGS;
        }

        this.fontSizeStrings = var1;
        JPanel var2 = new JPanel();
        var2.setLayout(new BoxLayout(var2, 0));
        var2.add(this.getFontFamilyPanel());
        var2.add(this.getFontStylePanel());
        var2.add(this.getFontSizePanel());
        JPanel var3 = new JPanel();
        var3.setLayout(new GridLayout(2, 1));
        var3.add(var2, "North");
        var3.add(this.getSamplePanel(), "Center");
        this.setLayout(new BoxLayout(this, 0));
        this.add(var3);
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setSelectedFont(DEFAULT_SELECTED_FONT);
    }

    public JTextField getFontFamilyTextField() {
        if(this.fontFamilyTextField == null) {
            this.fontFamilyTextField = new JTextField();
            this.fontFamilyTextField.addFocusListener(new JFontChooser.TextFieldFocusHandlerForTextSelection(this.fontFamilyTextField));
            this.fontFamilyTextField.addKeyListener(new JFontChooser.TextFieldKeyHandlerForListSelectionUpDown(this.getFontFamilyList()));
            this.fontFamilyTextField.getDocument().addDocumentListener(new JFontChooser.ListSearchTextFieldDocumentHandler(this.getFontFamilyList()));
            this.fontFamilyTextField.setFont(DEFAULT_FONT);
        }

        return this.fontFamilyTextField;
    }

    public JTextField getFontStyleTextField() {
        if(this.fontStyleTextField == null) {
            this.fontStyleTextField = new JTextField();
            this.fontStyleTextField.addFocusListener(new JFontChooser.TextFieldFocusHandlerForTextSelection(this.fontStyleTextField));
            this.fontStyleTextField.addKeyListener(new JFontChooser.TextFieldKeyHandlerForListSelectionUpDown(this.getFontStyleList()));
            this.fontStyleTextField.getDocument().addDocumentListener(new JFontChooser.ListSearchTextFieldDocumentHandler(this.getFontStyleList()));
            this.fontStyleTextField.setFont(DEFAULT_FONT);
        }

        return this.fontStyleTextField;
    }

    public JTextField getFontSizeTextField() {
        if(this.fontSizeTextField == null) {
            this.fontSizeTextField = new JTextField();
            this.fontSizeTextField.addFocusListener(new JFontChooser.TextFieldFocusHandlerForTextSelection(this.fontSizeTextField));
            this.fontSizeTextField.addKeyListener(new JFontChooser.TextFieldKeyHandlerForListSelectionUpDown(this.getFontSizeList()));
            this.fontSizeTextField.getDocument().addDocumentListener(new JFontChooser.ListSearchTextFieldDocumentHandler(this.getFontSizeList()));
            this.fontSizeTextField.setFont(DEFAULT_FONT);
        }

        return this.fontSizeTextField;
    }

    public JList getFontFamilyList() {
        if(this.fontNameList == null) {
            this.fontNameList = new JList(this.getFontFamilies());
            this.fontNameList.setSelectionMode(0);
            this.fontNameList.addListSelectionListener(new JFontChooser.ListSelectionHandler(this.getFontFamilyTextField()));
            this.fontNameList.setSelectedIndex(0);
            this.fontNameList.setFont(DEFAULT_FONT);
            this.fontNameList.setFocusable(false);
        }

        return this.fontNameList;
    }

    public JList getFontStyleList() {
        if(this.fontStyleList == null) {
            this.fontStyleList = new JList(this.getFontStyleNames());
            this.fontStyleList.setSelectionMode(0);
            this.fontStyleList.addListSelectionListener(new JFontChooser.ListSelectionHandler(this.getFontStyleTextField()));
            this.fontStyleList.setSelectedIndex(0);
            this.fontStyleList.setFont(DEFAULT_FONT);
            this.fontStyleList.setFocusable(false);
        }

        return this.fontStyleList;
    }

    public JList getFontSizeList() {
        if(this.fontSizeList == null) {
            this.fontSizeList = new JList(this.fontSizeStrings);
            this.fontSizeList.setSelectionMode(0);
            this.fontSizeList.addListSelectionListener(new JFontChooser.ListSelectionHandler(this.getFontSizeTextField()));
            this.fontSizeList.setSelectedIndex(0);
            this.fontSizeList.setFont(DEFAULT_FONT);
            this.fontSizeList.setFocusable(false);
        }

        return this.fontSizeList;
    }

    public String getSelectedFontFamily() {
        String var1 = (String)this.getFontFamilyList().getSelectedValue();
        return var1;
    }

    public int getSelectedFontStyle() {
        int var1 = this.getFontStyleList().getSelectedIndex();
        return FONT_STYLE_CODES[var1];
    }

    public int getSelectedFontSize() {
        boolean var1 = true;
        String var2 = this.getFontSizeTextField().getText();

        while(true) {
            try {
                int var5 = Integer.parseInt(var2);
                return var5;
            } catch (NumberFormatException var4) {
                var2 = (String)this.getFontSizeList().getSelectedValue();
                this.getFontSizeTextField().setText(var2);
            }
        }
    }

    public Font getSelectedFont() {
        Font var1 = new Font(this.getSelectedFontFamily(), this.getSelectedFontStyle(), this.getSelectedFontSize());
        return var1;
    }

    public void setSelectedFontFamily(String var1) {
        String[] var2 = this.getFontFamilies();

        for(int var3 = 0; var3 < var2.length; ++var3) {
            if(var2[var3].toLowerCase().equals(var1.toLowerCase())) {
                this.getFontFamilyList().setSelectedIndex(var3);
                break;
            }
        }

        this.updateSampleFont();
    }

    public void setSelectedFontStyle(int var1) {
        for(int var2 = 0; var2 < FONT_STYLE_CODES.length; ++var2) {
            if(FONT_STYLE_CODES[var2] == var1) {
                this.getFontStyleList().setSelectedIndex(var2);
                break;
            }
        }

        this.updateSampleFont();
    }

    public void setSelectedFontSize(int var1) {
        String var2 = String.valueOf(var1);

        for(int var3 = 0; var3 < this.fontSizeStrings.length; ++var3) {
            if(this.fontSizeStrings[var3].equals(var2)) {
                this.getFontSizeList().setSelectedIndex(var3);
                break;
            }
        }

        this.getFontSizeTextField().setText(var2);
        this.updateSampleFont();
    }

    public void setSelectedFont(Font var1) {
        this.setSelectedFontFamily(var1.getFamily());
        this.setSelectedFontStyle(var1.getStyle());
        this.setSelectedFontSize(var1.getSize());
    }

    public String getVersionString() {
        return this._("Version");
    }

    public int showDialog(Component var1) {
        this.dialogResultValue = -1;
        JDialog var2 = this.createDialog(var1);
        var2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
                JFontChooser.this.dialogResultValue = 1;
            }
        });
        var2.setVisible(true);
        var2.dispose();
        var2 = null;
        return this.dialogResultValue;
    }

    protected JDialog createDialog(Component var1) {
        Frame var2 = var1 instanceof Frame?(Frame)var1:(Frame)SwingUtilities.getAncestorOfClass(Frame.class, var1);
        JDialog var3 = new JDialog(var2, this._("SelectFont"), true);
        JFontChooser.DialogOKAction var4 = new JFontChooser.DialogOKAction(var3);
        JFontChooser.DialogCancelAction var5 = new JFontChooser.DialogCancelAction(var3);
        JFontChooser.DialogDefaultAction var6 = new JFontChooser.DialogDefaultAction(var3);
        JButton var7 = new JButton(var4);
        var7.setFont(DEFAULT_FONT);
        JButton var8 = new JButton(var5);
        var8.setFont(DEFAULT_FONT);
        JButton var9 = new JButton(var6);
        var9.setFont(DEFAULT_FONT);
        JPanel var10 = new JPanel();
        var10.setLayout(new GridLayout(3, 1));
        var10.add(var7);
        var10.add(var8);
        var10.add(var9);
        var10.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 10));
        ActionMap var11 = var10.getActionMap();
        var11.put(var5.getValue("Default"), var5);
        var11.put(var4.getValue("Default"), var4);
        InputMap var12 = var10.getInputMap(2);
        var12.put(KeyStroke.getKeyStroke("ESCAPE"), var5.getValue("Default"));
        var12.put(KeyStroke.getKeyStroke("ENTER"), var4.getValue("Default"));
        JPanel var13 = new JPanel();
        var13.setLayout(new BorderLayout());
        var13.add(var10, "North");
        var3.getContentPane().add(this, "Center");
        var3.getContentPane().add(var13, "East");
        var3.pack();
        var3.setLocationRelativeTo(var2);
        return var3;
    }

    protected void updateSampleFont() {
        Font var1 = this.getSelectedFont();
        this.getSampleTextField().setFont(var1);
    }

    protected JPanel getFontFamilyPanel() {
        if(this.fontNamePanel == null) {
            this.fontNamePanel = new JPanel();
            this.fontNamePanel.setLayout(new BorderLayout());
            this.fontNamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            this.fontNamePanel.setPreferredSize(new Dimension(180, 130));
            JScrollPane var1 = new JScrollPane(this.getFontFamilyList());
            var1.getVerticalScrollBar().setFocusable(false);
            var1.setVerticalScrollBarPolicy(22);
            JPanel var2 = new JPanel();
            var2.setLayout(new BorderLayout());
            var2.add(this.getFontFamilyTextField(), "North");
            var2.add(var1, "Center");
            JLabel var3 = new JLabel(this._("FontName"));
            var3.setHorizontalAlignment(2);
            var3.setHorizontalTextPosition(2);
            var3.setLabelFor(this.getFontFamilyTextField());
            var3.setDisplayedMnemonic('F');
            this.fontNamePanel.add(var3, "North");
            this.fontNamePanel.add(var2, "Center");
        }

        return this.fontNamePanel;
    }

    protected JPanel getFontStylePanel() {
        if(this.fontStylePanel == null) {
            this.fontStylePanel = new JPanel();
            this.fontStylePanel.setLayout(new BorderLayout());
            this.fontStylePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            this.fontStylePanel.setPreferredSize(DPIController.scaleToDPI(new Dimension(140, 130)));
            JScrollPane var1 = new JScrollPane(this.getFontStyleList());
            var1.getVerticalScrollBar().setFocusable(false);
            var1.setVerticalScrollBarPolicy(22);
            JPanel var2 = new JPanel();
            var2.setLayout(new BorderLayout());
            var2.add(this.getFontStyleTextField(), "North");
            var2.add(var1, "Center");
            JLabel var3 = new JLabel(this._("FontStyle"));
            var3.setHorizontalAlignment(2);
            var3.setHorizontalTextPosition(2);
            var3.setLabelFor(this.getFontStyleTextField());
            var3.setDisplayedMnemonic('Y');
            this.fontStylePanel.add(var3, "North");
            this.fontStylePanel.add(var2, "Center");
        }

        return this.fontStylePanel;
    }

    protected JPanel getFontSizePanel() {
        if(this.fontSizePanel == null) {
            this.fontSizePanel = new JPanel();
            this.fontSizePanel.setLayout(new BorderLayout());
            this.fontSizePanel.setPreferredSize(DPIController.scaleToDPI(new Dimension(70, 130)));
            this.fontSizePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            JScrollPane var1 = new JScrollPane(this.getFontSizeList());
            var1.getVerticalScrollBar().setFocusable(false);
            var1.setVerticalScrollBarPolicy(22);
            JPanel var2 = new JPanel();
            var2.setLayout(new BorderLayout());
            var2.add(this.getFontSizeTextField(), "North");
            var2.add(var1, "Center");
            JLabel var3 = new JLabel(this._("FontSize"));
            var3.setHorizontalAlignment(2);
            var3.setHorizontalTextPosition(2);
            var3.setLabelFor(this.getFontSizeTextField());
            var3.setDisplayedMnemonic('S');
            this.fontSizePanel.add(var3, "North");
            this.fontSizePanel.add(var2, "Center");
        }

        return this.fontSizePanel;
    }

    protected JPanel getSamplePanel() {
        if(this.samplePanel == null) {
            TitledBorder var1 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), this._("Sample"));
            Border var2 = BorderFactory.createEmptyBorder(5, 10, 10, 10);
            CompoundBorder var3 = BorderFactory.createCompoundBorder(var1, var2);
            this.samplePanel = new JPanel();
            this.samplePanel.setLayout(new BorderLayout());
            this.samplePanel.setBorder(var3);
            this.samplePanel.add(this.getSampleTextField(), "Center");
        }

        return this.samplePanel;
    }

    protected JTextField getSampleTextField() {
        if(this.sampleText == null) {
            Border var1 = BorderFactory.createLoweredBevelBorder();
            this.sampleText = new JTextField(this._("SampleString"));
            this.sampleText.setBorder(var1);
            this.sampleText.setPreferredSize(DPIController.scaleToDPI(new Dimension(300, 100)));
        }

        return this.sampleText;
    }

    protected String[] getFontFamilies() {
        if(this.fontFamilyNames == null) {
            GraphicsEnvironment var1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            this.fontFamilyNames = var1.getAvailableFontFamilyNames();
        }

        return this.fontFamilyNames;
    }

    protected String[] getFontStyleNames() {
        if(this.fontStyleNames == null) {
            byte var1 = 0;
            this.fontStyleNames = new String[4];
            int var2 = var1 + 1;
            this.fontStyleNames[var1] = this._("Plain");
            this.fontStyleNames[var2++] = this._("Bold");
            this.fontStyleNames[var2++] = this._("Italic");
            this.fontStyleNames[var2++] = this._("BoldItalic");
        }

        return this.fontStyleNames;
    }

    protected class DialogDefaultAction extends AbstractAction {
        protected static final String ACTION_NAME = "Default";
        private JDialog dialog;

        protected DialogDefaultAction(JDialog var2) {
            this.dialog = var2;
            this.putValue("Default", "Default");
            this.putValue("ActionCommandKey", "Default");
            this.putValue("Name", JFontChooser.this._("Default"));
        }

        public void actionPerformed(ActionEvent var1) {
            JFontChooser.this.dialogResultValue = 2;
            this.dialog.setVisible(false);
        }
    }

    protected class DialogCancelAction extends AbstractAction {
        protected static final String ACTION_NAME = "Cancel";
        private JDialog dialog;

        protected DialogCancelAction(JDialog var2) {
            this.dialog = var2;
            this.putValue("Default", "Cancel");
            this.putValue("ActionCommandKey", "Cancel");
            this.putValue("Name", JFontChooser.this._("Cancel"));
        }

        public void actionPerformed(ActionEvent var1) {
            JFontChooser.this.dialogResultValue = 1;
            this.dialog.setVisible(false);
        }
    }

    protected class DialogOKAction extends AbstractAction {
        protected static final String ACTION_NAME = "OK";
        private JDialog dialog;

        protected DialogOKAction(JDialog var2) {
            this.dialog = var2;
            this.putValue("Default", "OK");
            this.putValue("ActionCommandKey", "OK");
            this.putValue("Name", JFontChooser.this._("OK"));
        }

        public void actionPerformed(ActionEvent var1) {
            JFontChooser.this.dialogResultValue = 0;
            this.dialog.setVisible(false);
        }
    }

    protected class ListSearchTextFieldDocumentHandler implements DocumentListener {
        JList targetList;

        public ListSearchTextFieldDocumentHandler(JList var2) {
            this.targetList = var2;
        }

        public void insertUpdate(DocumentEvent var1) {
            this.update(var1);
        }

        public void removeUpdate(DocumentEvent var1) {
            this.update(var1);
        }

        public void changedUpdate(DocumentEvent var1) {
            this.update(var1);
        }

        private void update(DocumentEvent var1) {
            String var2 = "";

            try {
                Document var3 = var1.getDocument();
                var2 = var3.getText(0, var3.getLength());
            } catch (BadLocationException var5) {
                var5.printStackTrace();
            }

            if(var2.length() > 0) {
                int var6 = this.targetList.getNextMatch(var2, 0, Bias.Forward);
                if(var6 < 0) {
                    var6 = 0;
                }

                this.targetList.ensureIndexIsVisible(var6);
                String var4 = this.targetList.getModel().getElementAt(var6).toString();
                if(var2.equalsIgnoreCase(var4) && var6 != this.targetList.getSelectedIndex()) {
                    SwingUtilities.invokeLater(new JFontChooser.ListSearchTextFieldDocumentHandler.ListSelector(var6));
                }
            }

        }

        public class ListSelector implements Runnable {
            private int index;

            public ListSelector(int var2) {
                this.index = var2;
            }

            public void run() {
                ListSearchTextFieldDocumentHandler.this.targetList.setSelectedIndex(this.index);
            }
        }
    }

    protected class TextFieldKeyHandlerForListSelectionUpDown extends KeyAdapter {
        private JList targetList;

        public TextFieldKeyHandlerForListSelectionUpDown(JList var2) {
            this.targetList = var2;
        }

        public void keyPressed(KeyEvent var1) {
            int var2 = this.targetList.getSelectedIndex();
            switch(var1.getKeyCode()) {
                case 38:
                    var2 = this.targetList.getSelectedIndex() - 1;
                    if(var2 < 0) {
                        var2 = 0;
                    }

                    this.targetList.setSelectedIndex(var2);
                    break;
                case 40:
                    int var3 = this.targetList.getModel().getSize();
                    var2 = this.targetList.getSelectedIndex() + 1;
                    if(var2 >= var3) {
                        var2 = var3 - 1;
                    }

                    this.targetList.setSelectedIndex(var2);
            }

        }
    }

    protected class TextFieldFocusHandlerForTextSelection extends FocusAdapter {
        private JTextComponent textComponent;

        public TextFieldFocusHandlerForTextSelection(JTextComponent var2) {
            this.textComponent = var2;
        }

        public void focusGained(FocusEvent var1) {
            this.textComponent.selectAll();
        }

        public void focusLost(FocusEvent var1) {
            this.textComponent.select(0, 0);
            JFontChooser.this.updateSampleFont();
        }
    }

    protected class ListSelectionHandler implements ListSelectionListener {
        private JTextComponent textComponent;

        ListSelectionHandler(JTextComponent var2) {
            this.textComponent = var2;
        }

        public void valueChanged(ListSelectionEvent var1) {
            if(!var1.getValueIsAdjusting()) {
                JList var2 = (JList)var1.getSource();
                String var3 = (String)var2.getSelectedValue();
                String var4 = this.textComponent.getText();
                this.textComponent.setText(var3);
                if(!var4.equalsIgnoreCase(var3)) {
                    this.textComponent.selectAll();
                    this.textComponent.requestFocus();
                }

                JFontChooser.this.updateSampleFont();
            }

        }
    }
}
