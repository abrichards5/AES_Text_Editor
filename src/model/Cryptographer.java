package model;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by alutman on 18/03/14.
 *
 * Simple minimum interface required to encrypt data
 *
 */
public interface Cryptographer {

    public byte[] encrypt (byte[] key, byte[] data);
    public byte[] decrypt (byte[] key, byte[] data) throws BadPaddingException, IllegalBlockSizeException;

    public String getMode();

}
