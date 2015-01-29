package al.aesencryptor;

import al.aesencryptor.enums.FileStatus;
import org.junit.Test;

public class CryptographerHandlerTest {

    @Test
    public void should_be_set_to_text_mode_after_loading_string() {
        CryptographerHandler ch = new CryptographerHandler(new CBCCryptographer());

        ch.setText("Hello world");

        assert ch.getDataMode().equals(FileStatus.TEXT_FILE);
    }

    @Test
    public void should_produce_input_after_encryption_and_decryption() {
        CryptographerHandler ch = new CryptographerHandler(new CBCCryptographer());
        String input = "Blah blah blah";
        String key = "badpassword";

        ch.setText(input);
        ch.encrypt(key);
        assert !ch.getText().equals(input);
        ch.decrypt(key);
        assert ch.getText().equals(input);
    }

}