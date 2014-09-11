package model;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import model.enums.Encoding;
import model.enums.FileStatus;
import model.exception.InvalidEncryptionException;

import java.io.UnsupportedEncodingException;

/**
 * Created by alutman on 7/04/14.
 *
 * Represents data stored in bytes within the model.
 *
 */
public class ByteData {

    private byte[] data;
    public Encoding encodingMode = Encoding.BASE64;
    private FileStatus mode = FileStatus.TEXT_FILE;

    public ByteData() {}

    public FileStatus getMode() {
        return mode;
    }
    public void set(byte[] data) {
        this.data = data;
        detectMode();
    }
    public void set(String data) {
        try {
            this.data = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            System.err.println(uee.getMessage());
            System.exit(1);
        }
        detectMode();
    }
    public int length() {
        return data.length;
    }

    public void encode() {
        if(encodingMode.equals(Encoding.BASE64)) {
            set(Base64.encode(data));
        }
        else if (encodingMode.equals(Encoding.HEX)) {
            set(HexBin.encode(data));
        }
        else if (encodingMode.equals(Encoding.NONE)) {
            //Don't bother encoding
        }
    }
    public void decode() throws InvalidEncryptionException {
        if(encodingMode.equals(Encoding.BASE64)) {
            try {
                set(Base64.decode(text()));
            } catch(Base64DecodingException b64de) {
                throw new InvalidEncryptionException();
            }

        }
        else if (encodingMode.equals(Encoding.HEX)) {
            byte[] b = HexBin.decode(text());
            if (b == null) {
                throw new InvalidEncryptionException();
            }
            set(HexBin.decode(text()));
        }
        else if (encodingMode.equals(Encoding.NONE)) {
            //Do nothing.
        }
    }
    private void detectMode() {
        detectMode(false);
    }
    private void detectMode(boolean type) {
        for (byte c : bytes()) {
            byte[] bc = new byte[1];
            bc[0] = c;
            try {
                if(type) System.out.println(c +" "+new String(bc, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(c == 0x00){
                mode = FileStatus.BINARY_FILE;
                return;
            }
        }
        mode = FileStatus.TEXT_FILE;
    }


    public byte[] bytes() {
        return data;
    }

    public String text() {
        try {
            return new String(data, "UTF-8");
        }
        catch(UnsupportedEncodingException uee) {
            System.err.println(uee.getMessage());
            System.exit(1);
        }
        return null;

    }

}
