package al.aesencryptor;

import al.aesencryptor.enums.Encoding;
import al.aesencryptor.enums.FileStatus;
import al.aesencryptor.exception.InvalidEncryptionException;
import al.aesencryptor.enums.CryptStatus;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Base model functionality
 *
 */
public class CryptographerHandler {

    private final Cryptographer aes;
    private final ByteData data = new ByteData();

    public CryptographerHandler(Cryptographer aes) {
        this.aes = aes;
    }
    public String getCryptoMode() {
        return aes.getMode();
    }

    public FileStatus getDataMode() {
        return data.getMode();
    }

    public String getText() {
        return data.text();
    }
    public void setText(String text) {
        data.set(text);
    }
    public void setData(byte[] bytes) {
        data.set(bytes);
    }

    public Encoding getEncoding() {
        return data.encodingMode;

    }
    public void setEncoding(Encoding e) {
        data.encodingMode = e;
    }


    public FileStatus openFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] dataIn = new byte[(int)file.length()];
        fis.read(dataIn, 0, dataIn.length);
        data.set(dataIn);
        data.detectMode();
        fis.close();

        if (data.getMode().equals(FileStatus.BINARY_FILE)) {
            setEncoding(Encoding.NONE);
        } else {
            setEncoding(Encoding.BASE64);
        }

        return data.getMode();
    }

    public void saveFile(File filename) throws IOException {
        FileOutputStream fos2 = new FileOutputStream(filename);
        fos2.write(data.bytes(), 0, data.length());
        fos2.flush();
        fos2.close();
    }

    public CryptStatus encrypt(String key) {
        if(!checkKey(key)) return CryptStatus.NULL_KEY;
        data.set(aes.encrypt(key.getBytes(), data.bytes()));

        //Convert text based encryption to base64 for easier copy/paste
        data.encode();
        data.detectMode();

        return CryptStatus.ENCRYPT_SUCCESS;
    }

    public CryptStatus decrypt (String key) {
        if(!checkKey(key)) return CryptStatus.NULL_KEY;
        try {
            data.decode();
            data.set(aes.decrypt(key.getBytes(), data.bytes()));
            data.detectMode();
        }
        catch (InvalidEncryptionException iee) {
            //InvalidEncryptionException is a result of trying to decrypt something that isn't encrypted
            return CryptStatus.INVALID_FILE;
        }
        catch (BadPaddingException | IllegalBlockSizeException e) {
            //BadPaddingException is a result of trying to decrypt with a incorrect key
            data.encode();
            return CryptStatus.INVALID_KEY_ENC;
        }
        return CryptStatus.DECRYPT_SUCCESS;
    }

    private boolean checkKey(String key) {
        if (key == null ) {
            //JInputPane - Cancel option
            return false;
        }
        else if(key.equals("")) {
            //JInputPane - Yes option
            return false;
        }
        return true;
    }



}
