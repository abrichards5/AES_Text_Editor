package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Created by alutman on 7/04/14.
 *
 * Represents data stored in bytes within the model.
 *
 */
class ByteData {

    private byte[] data;

    public ByteData() {}

    public void set(byte[] data) {
        this.data = data;
    }
    public void set(String data) {
        this.data = data.getBytes();
    }
    public int length() {
        return data.length;
    }

    public String toBase64() {
        return Base64.encode(data);
    }

    public byte[] fromBase64() throws Base64DecodingException {
        return Base64.decode(text());
    }

    public byte[] bytes() {
        return data;
    }

    public String text() {
        return new String(data);
    }


}
