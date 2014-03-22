import model.CBCCryptographer;
import model.ECBCryptographer;
import model.Cryptographer;

import java.security.SecureRandom;

/**
 * Created by alutman on 14/03/14.
 *
 * Test both encryption tools by comparing data before and after an encrypt/decrypt cycle
 *
 */
public class EncryptDecryptTest {

    public static void main (String args[]) throws Exception{

        SecureRandom sr = new SecureRandom();
        byte[] b = new byte[16];
        byte[] b2 = new byte[200];
        sr.nextBytes(b);
        sr.nextBytes(b2);

        String key = b.toString();
        String data = b2.toString();

        Cryptographer aes = new CBCCryptographer();
        String encryptedData = aes.encrypt(key, data);
        String decryptedData = aes.decrypt(key, encryptedData);

        if(!data.equals(decryptedData)) {
            System.out.println("CBCCryptographer(): Data decrypt mismatch");
        }
        else {
            System.out.println("CBCCryptographer(): Test successful");
        }

        aes = new ECBCryptographer();
        encryptedData = aes.encrypt(key, data);
        decryptedData = aes.decrypt(key, encryptedData);

        if(!data.equals(decryptedData)) {
            throw new Exception("ECBCryptographer(): Data decrypt mismatch");
        }
        else {
            System.out.println("ECBCryptographer(): Test successful");
        }

    }
}
