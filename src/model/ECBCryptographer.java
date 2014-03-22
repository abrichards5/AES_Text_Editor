package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by alutman on 14/03/14.
 *
 * Performs the actual encryption and decryption
 *
 */
public class ECBCryptographer implements Cryptographer{
    private Cipher cipher = null;
    private MessageDigest hash = null;

    private final int KEY_SIZE = 16;

    private final String ENCRYPTION_ALGORITHM = "AES";
    private final String MODE_ALGORITHM = "ECB";
    private final String PADDING_ALGORITHM = "PKCS5Padding";
    private final String HASH_ALGORITHM = "SHA-1";
    private final String CIPHER_ALGORITHM = ENCRYPTION_ALGORITHM+"/"+MODE_ALGORITHM+"/"+PADDING_ALGORITHM;




    public ECBCryptographer() {
        init();
    }
    private void init() {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            hash = MessageDigest.getInstance(HASH_ALGORITHM);
        }
        // These exceptions shouldn't be thrown as the algorithms are hard coded
        catch (NoSuchAlgorithmException nsae) {
            System.err.println("ECBCryptographer NoSuchAlgorithmException: "+nsae.getMessage());
            System.exit(1);
        }
        catch (NoSuchPaddingException nspe) {
            System.err.println("ECBCryptographer NoSuchPaddingException: "+nspe.getMessage());
            System.exit(1);

        }
    }
    // Used to convert a String key to a usable byte[16]
    private byte[] stringToBytes(String s) {
        byte[] fullBytes = s.getBytes();
        //Hash the key and the return only the first 16 bytes
        fullBytes = Arrays.copyOf(hash.digest(fullBytes), KEY_SIZE);
        return fullBytes;
    }

    private SecretKey getSecretKey(String key) {
        return new SecretKeySpec(stringToBytes(key), ENCRYPTION_ALGORITHM);
    }

    public String encrypt(String key, String data) {
        try {
            SecretKey secretKey = getSecretKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
            return Base64.encode(cipher.doFinal(data.getBytes()));
        }
        catch (InvalidKeyException e) {
            System.err.println("ECBCryptographer.encrypt() InvalidKeyException: "+e.getMessage());
            System.exit(1);
        }
        catch (IllegalBlockSizeException e) {
            System.err.println("ECBCryptographer.encrypt() IllegalBlockSizeException: "+e.getMessage());
            System.exit(1);
        }
        catch (BadPaddingException e) {
            System.err.println("ECBCryptographer.encrypt() BadPaddingException: "+e.getMessage());
            System.exit(1);
        }
        return null;

    }

    public String decrypt(String key, String encryptedData) throws Base64DecodingException, BadPaddingException, IllegalBlockSizeException{
        try {
            SecretKey secretKey = getSecretKey(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());
            return new String(cipher.doFinal(Base64.decode(encryptedData)));
        }
        catch (InvalidKeyException e) {
            System.err.println("ECBCryptographer.encrypt() InvalidKeyException: "+e.getMessage());
            System.exit(1);
        }
        return null;
    }

}
