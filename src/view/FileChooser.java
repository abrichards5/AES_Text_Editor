package view;

import javax.swing.*;
import java.io.File;

/**
 * Created by alutman on 17/03/14.
 *
 * Customizes the filechooser in DialogBuilder to ask for overwrite
 */

class FileChooser extends JFileChooser {
    private final DialogBuilder dialogs;

    public FileChooser(DialogBuilder dialogs) {
        super("./");
        this.dialogs = dialogs;
        this.addChoosableFileFilter(new CryptFileFilter());
    }

    @Override
    public void approveSelection() {
        // If it's as save dialog, ask to overwrite if the file exists.
        if (this.getDialogType() == JFileChooser.SAVE_DIALOG) {
            validateExtension(getSelectedFile());
            if (getSelectedFile().exists()) {
                int result = dialogs.confirmOverwriteDialog();
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        cancelSelection();
                        return;
                }
            }
        }
        super.approveSelection();
    }
    //Appends .enc if saving as encrypted file
    private void validateExtension(File f) {
        if(this.getFileFilter().getClass().equals(CryptFileFilter.class)) {
            if(!f.getAbsolutePath().endsWith("."+CryptFileFilter.ENC_EXT)) {
                setSelectedFile(new File(f.getAbsolutePath().concat("."+CryptFileFilter.ENC_EXT)));
            }
        }
    }



}
