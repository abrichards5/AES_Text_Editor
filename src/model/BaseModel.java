package model;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import model.enums.CryptStatus;
import model.enums.FileCheck;
import model.enums.FileMode;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;

/**
 * Created by alutman on 14/03/14.
 *
 * Base model functionality
 *
 */
public class BaseModel {

    private final Cryptographer aes;
    private final ByteData data = new ByteData();
    protected FileMode mode = FileMode.TEXT;
    private final long MAX_SIZE =  50000000L; //50mb

    public BaseModel(Cryptographer aes) {
        this.aes = aes;
    }
    protected String getCurrentCryptoMode() {
        return aes.getMode();
    }

    public ByteData getData() {
        return data;
    }

    // Loads the file and checks what type it is
    private FileCheck loadFile(File file) throws IOException {
        long length = file.length();
        // Check size
        if(length > MAX_SIZE) {
            return FileCheck.LARGE_FILE;
        }

        FileInputStream fis = new FileInputStream(file);
        byte[] dataIn = new byte[(int)file.length()];
        fis.read(dataIn, 0, dataIn.length);
        data.set(dataIn);
        fis.close();

        for (byte c : dataIn) {
            if(!validByte(c)){
                return FileCheck.BINARY_FILE;
            }
        }
        return FileCheck.TEXT_FILE;
    }

    private boolean validByte(byte b) {
        if(b != 0x00) {
            return true;
        }
        return false;
    }

    public FileCheck openFile(File file) throws IOException {
        FileCheck fcIn = loadFile(file);
        if(fcIn.equals(FileCheck.TEXT_FILE)) {
            mode = FileMode.TEXT;
        }
        else if(fcIn.equals(FileCheck.BINARY_FILE)) {
            mode = FileMode.BINARY;
        }
        return fcIn;
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
        if(mode.equals(FileMode.TEXT)) {
            data.set(data.toBase64());
        }
        return CryptStatus.ENCRYPT_SUCCESS;
    }

    public CryptStatus decrypt (String key) {
        if(!checkKey(key)) return CryptStatus.NULL_KEY;
        try {
            if(mode.equals(FileMode.TEXT)) {
                //Text encryption needs to be decoded from b64 first
                data.set(aes.decrypt(key.getBytes(), data.fromBase64()));
            }
            else if (mode.equals(FileMode.BINARY)) {
                data.set(aes.decrypt(key.getBytes(), data.bytes()));
            }

        }
        catch (Base64DecodingException b64de) {
            //Base64DecodingException is a result of trying to decrypt something that isn't encrypted
            return CryptStatus.INVALID_FILE;
        }
        catch (BadPaddingException | IllegalBlockSizeException e) {
            //BadPaddingException is a result of trying to decrypt with a incorrect key
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
