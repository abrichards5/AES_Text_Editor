package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


/**
 * Created by alutman on 18/03/14.
 *
 * Simple minimum interface required to encrypt data
 *
 */
public interface Cryptographer {

    public final static String ECB = "ECB";
    public final static String CBC = "CBC";
    public String encrypt (String key, String data);
    public String decrypt (String key, String data) throws Base64DecodingException, BadPaddingException, IllegalBlockSizeException;
}
