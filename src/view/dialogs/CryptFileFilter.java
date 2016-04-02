package view.dialogs;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by alutman on 20/03/14.
 * filter for save/open dialog
 */
class CryptFileFilter extends FileFilter {

    public static final String ENC_EXT = "enc";
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            return extension.equals(ENC_EXT);
        }

        return false;
    }
    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    @Override
    public String getDescription() {
        return "Encrypted Files";
    }
}
