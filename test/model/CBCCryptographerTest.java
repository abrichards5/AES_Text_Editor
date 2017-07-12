package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import model.exception.DecryptFailedException;
import model.exception.InvalidDataException;
import org.junit.Test;

import java.security.SecureRandom;

/**
 * Created by alutman on 14/03/14.
 *
 * Tests encryption tool by comparing data before and after an encrypt/decrypt cycle
 *
 */
public class CBCCryptographerTest {

    @Test
    public void should_produce_identical_byte_data_after_encrypt_and_decrypt() throws DecryptFailedException, InvalidDataException {
        SecureRandom sr = new SecureRandom();
        byte[] b = new byte[16];
        byte[] b2 = new byte[200];
        sr.nextBytes(b);
        sr.nextBytes(b2);

        byte[] key = b;
        byte[] data = b2;

        Cryptographer aes = new CBCCryptographer();
        byte[] encryptedData = aes.encrypt(key, data);
        byte[] decryptedData = aes.decrypt(key, encryptedData);

        assert compare(data, decryptedData);

    }

    @Test
    public void should_produce_identical_string_data_after_encrypt_and_decrypt() throws Base64DecodingException, DecryptFailedException, InvalidDataException {
        SecureRandom sr = new SecureRandom();
        byte[] b = new byte[16];
        sr.nextBytes(b);

        byte[] key = b;
        byte[] data = this.getClass().toString().getBytes();

        Cryptographer aes = new CBCCryptographer();
        String encryptedData = Base64.encode(aes.encrypt(key, data));
        String decryptedData = new String(aes.decrypt(key, Base64.decode(encryptedData)));

        assert new String(data).equals(decryptedData);
    }

    @Test
    public void should_work_with_empty_arrays() throws DecryptFailedException, InvalidDataException {
        byte[] key = new byte[0];
        byte[] data = new byte[0];

        Cryptographer aes = new CBCCryptographer();
        byte[] encryptedData = aes.encrypt(key, data);
        byte[] decryptedData = aes.decrypt(key, encryptedData);

        assert compare(data, decryptedData);
    }

    private static boolean compare(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        for(int i = 0; i < a.length; i++) {
            if(a[i] != b[i]) return false;
        }
        return true;
    }
}
