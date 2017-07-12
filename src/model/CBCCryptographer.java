package model;

import model.exception.DecryptFailedException;
import model.exception.InvalidDataException;
import model.exception.InvalidHMacException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Created by alutman on 18/03/14.
 *
 * AES cipher using CBC mode. More secure than ECB
 *
 */
public class CBCCryptographer implements Cryptographer{
    private final int ITERATION_COUNT = 100000;
    private final int BLOCK_SIZE = 128;
    private final int KEY_LENGTH = BLOCK_SIZE;
    private final int HMAC_LENGTH = 256;
    private final int SALT_LENGTH = 160;

    private Cipher cipher;
    private final String ENCRYPTION_ALGORITHM = "AES"; //128
    private final String MODE_ALGORITHM = "CBC";
    private final String RANDOM_ALGORITHM = "SHA1PRNG";
    private final String MAC_ALGORITHM = "HmacSHA256";
    private final String PADDING_ALGORITHM = "PKCS5Padding";
    private final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    private final String CIPHER_ALGORITHM = ENCRYPTION_ALGORITHM+"/"+MODE_ALGORITHM+"/"+PADDING_ALGORITHM;

    public CBCCryptographer() {
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            //Hardcoded algorithm shouldn't ever throw this
            throw new RuntimeException(e);
        }
    }

    private byte[] deriveKey(String key, byte[] salt, int length) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec ks = new PBEKeySpec(key.toCharArray(), salt, ITERATION_COUNT, length);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        return skf.generateSecret(ks).getEncoded();
    }

    public String getMode() {
        return CIPHER_ALGORITHM;
    }

    /*
        Structure of encrypted data is

        hmacSalt
        hmac
        encSalt
        iv
        cipherText
     */

    @Override
    public byte[] encrypt(byte[] key, byte[] data) {
        try {
            SecureRandom sr = SecureRandom.getInstance(RANDOM_ALGORITHM);

            //generate key
            byte[] encSalt = new byte[SALT_LENGTH / 8];
            sr.nextBytes(encSalt);
            byte[] encKey = deriveKey(new String(key), encSalt, KEY_LENGTH);

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encKey, ENCRYPTION_ALGORITHM));
            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

            //encrypt data
            byte[] cipherText = cipher.doFinal(data);

            // generate mac
            byte[] hmacSalt = new byte[SALT_LENGTH / 8];
            sr.nextBytes(hmacSalt);
            byte[] hmacKey = deriveKey(new String(key), hmacSalt, SALT_LENGTH);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(new SecretKeySpec(hmacKey, MAC_ALGORITHM));

            // apply hmac
            byte[] encrypted = new byte[iv.length + encSalt.length + cipherText.length];
            System.arraycopy(encSalt, 0, encrypted, 0, encSalt.length);
            System.arraycopy(iv, 0, encrypted, encSalt.length, iv.length);
            System.arraycopy(cipherText, 0, encrypted, encSalt.length+iv.length, cipherText.length);

            byte[] hmac = mac.doFinal(encrypted);

            //Copy everything together
            byte[] output = new byte[hmacSalt.length + encrypted.length + hmac.length];
            System.arraycopy(hmacSalt, 0, output, 0, hmacSalt.length);
            System.arraycopy(hmac, 0, output, hmacSalt.length, hmac.length);
            System.arraycopy(encrypted, 0, output, hmacSalt.length+hmac.length, encrypted.length);

            return output;
        }
        catch (IllegalBlockSizeException | BadPaddingException | InvalidParameterSpecException | InvalidKeyException
                | NoSuchAlgorithmException | InvalidKeySpecException e) {
            //Hardcoded algorithms and sizes shouldn't ever throw these
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] data) throws DecryptFailedException, InvalidDataException {
        if(data.length < SALT_LENGTH/8+HMAC_LENGTH/8+SALT_LENGTH/8+BLOCK_SIZE/8) {
            //Any encrypted data MUST have hmacSalt, hmac, encSalt & iv
            throw new InvalidDataException("data not long enough");
        }
        try {
            int current = 0;
            int encryptedStart = 0;
            byte[] hmacSalt = Arrays.copyOfRange(data, current, SALT_LENGTH/8);
            current += SALT_LENGTH/8;
            byte[] hmac = Arrays.copyOfRange(data, current, current+HMAC_LENGTH/8);
            current += HMAC_LENGTH/8;
            encryptedStart = current;
            byte[] encSalt = Arrays.copyOfRange(data, current, current+SALT_LENGTH/8);
            current += SALT_LENGTH/8;
            byte[] iv = Arrays.copyOfRange(data, current, current+BLOCK_SIZE/8);
            current += BLOCK_SIZE/8;
            byte[] cipherText = Arrays.copyOfRange(data, current, data.length);


            byte[] hmacKey = deriveKey(new String(key), hmacSalt, SALT_LENGTH);
            // Perform HMAC using SHA-256
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(new SecretKeySpec(hmacKey, MAC_ALGORITHM));
            byte[] checkHmac = mac.doFinal(Arrays.copyOfRange(data, encryptedStart, data.length));

            // Compare Computed HMAC vs Recovered HMAC
            if (MessageDigest.isEqual(hmac, checkHmac)) {
                byte[] encKey = deriveKey(new String(key), encSalt, KEY_LENGTH);
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, ENCRYPTION_ALGORITHM), new IvParameterSpec(iv));
                return cipher.doFinal(cipherText);
            }
            else {
                //Likely thrown for all invalid cases
                throw new InvalidHMacException("hmac does not match the ciphertext");
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            //Shouldn't occur
            e.printStackTrace();
            System.exit(1);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            //Shouldn't occur
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (BadPaddingException| IllegalBlockSizeException | InvalidHMacException e) {
            throw new DecryptFailedException(e);
        }
        return null;

    }
}
