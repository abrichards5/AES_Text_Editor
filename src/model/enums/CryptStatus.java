package model.enums;

/**
 * Created by alutman on 7/04/14.
 *
 * Enumerated values for return types when checking a file
 *
 */
public enum CryptStatus {

    NULL_KEY("KEY MUST NOT BE NULL"),
    ENCRYPT_SUCCESS("ENCRYPT SUCCESSFUL"),
    DECRYPT_SUCCESS("DECRYPT SUCCESSFUL"),
    INVALID_KEY_ENC("INVALID KEY/ENCRYPTION"),
    INVALID_FILE("NOT A VALID ENCRYPTED FILE"),
    ENCRYPT_FAILED("ENCRYPT FAILED"),
    DECRYPT_FAILED("DECRYPT FAILED");

    private final String name;

    CryptStatus(String name) {
        this.name = name;
    }

    public boolean equals(CryptStatus mode) {
        return mode.toString().equals(this.toString());
    }
    @Override
    public String toString() {
        return name;
    }
}
