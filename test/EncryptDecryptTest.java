import com.sun.org.apache.xml.internal.security.utils.Base64;
import model.CBCCryptographer;
import model.Cryptographer;

import java.security.SecureRandom;

/**
 * Created by alutman on 14/03/14.
 *
 * Tests encryption tool by comparing data before and after an encrypt/decrypt cycle
 *
 */
public class EncryptDecryptTest {

    public static void main (String args[]) throws Exception{

        if(testBytes()) {
            System.out.println("FAILURE: CBCCryptographer.testBytes(): Data decrypt mismatch");
        }
        else {
            System.out.println("SUCCESS: CBCCryptographer.testBytes(): Test successful");
        }

        if(testStrings()) {
            System.out.println("FAILURE: CBCCryptographer.testStrings(): Data decrypt mismatch");
        }
        else {
            System.out.println("SUCCESS: CBCCryptographer.testStrings(): Test successful");
        }

    }

    private static boolean testBytes() throws Exception{
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
        return compare(encryptedData, decryptedData);

    }

    private static boolean testStrings() throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] b = new byte[16];
        byte[] b2 = new byte[200];
        sr.nextBytes(b);
        sr.nextBytes(b2);

        byte[] key = b;
        byte[] data = b2;

        Cryptographer aes = new CBCCryptographer();
        String encryptedData = Base64.encode(aes.encrypt(key, data));
        String decryptedData = new String(aes.decrypt(key, Base64.decode(encryptedData)));
        return encryptedData.equals(decryptedData);
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
