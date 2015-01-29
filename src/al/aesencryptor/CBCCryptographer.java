package al.aesencryptor;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

/**
 * Created by alutman on 18/03/14.
 *
 * AES cipher using CBC mode. More secure than ECB
 *
 */
public class CBCCryptographer implements Cryptographer{
    private final byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    private final int ITERATION_COUNT = 65536;
    private final int KEY_LENGTH = 128;
    private final int IV_LENGTH = 16;
    private Cipher cipher;
    private final String ENCRYPTION_ALGORITHM = "AES";
    private final String MODE_ALGORITHM = "CBC";
    private final String PADDING_ALGORITHM = "PKCS5Padding";
    private final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    private final String CIPHER_ALGORITHM = ENCRYPTION_ALGORITHM+"/"+MODE_ALGORITHM+"/"+PADDING_ALGORITHM;

    public CBCCryptographer() {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            //Hardcoded algorithm shouldn't ever throw this
            e.printStackTrace();
            System.exit(1);
        }
    }


    private SecretKey generateSecretKey(String key) {
        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(key.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            return new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public String getMode() {
        return CIPHER_ALGORITHM;
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] data) {
        try {
            SecretKey secretKey = generateSecretKey(new String(key));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

            //encrypt data
            byte[] encrypted = cipher.doFinal(data);

            //put iv in start of new array
            byte[] cipherText = new byte[encrypted.length + iv.length];
            System.arraycopy(iv, 0, cipherText, 0, iv.length);

            //append encrypted data to new array, after iv
            System.arraycopy(encrypted, 0, cipherText, iv.length, encrypted.length);

            //Encode to B64
            return cipherText;
        }
        catch (IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException | InvalidKeyException e) {
            //Shouldn't occur
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] data) throws BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = generateSecretKey(new String(key));
        try {
            //pull out the iv from the encrypted data
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(data,0,iv,0,IV_LENGTH);

            //Initialize with extracted IV
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            //Cut out data after IV
            byte[] ciphertext = new byte[data.length - IV_LENGTH];
            System.arraycopy(data, IV_LENGTH, ciphertext, 0, ciphertext.length);

            //Decrypt
            return cipher.doFinal(ciphertext);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            //Shouldn't occur
            e.printStackTrace();
            System.exit(1);
        }
        return null;

    }
}
