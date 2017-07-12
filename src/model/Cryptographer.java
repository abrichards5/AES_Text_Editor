package model;

import model.exception.DecryptFailedException;
import model.exception.InvalidDataException;
import model.exception.InvalidHMacException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

/**
 * Created by alutman on 18/03/14.
 *
 * Simple minimum interface required to encrypt data
 *
 */
public interface Cryptographer {

    public byte[] encrypt (byte[] key, byte[] data);
    public byte[] decrypt (byte[] key, byte[] data) throws DecryptFailedException, InvalidDataException;

    public String getMode();

}
