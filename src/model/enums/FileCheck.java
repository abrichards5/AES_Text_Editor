package model.enums;

/**
 * Created by alutman on 7/04/14.
 *
 * Enumerated values for return types when checking a file
 *
 */
public enum FileCheck {

    BINARY_FILE("BINARY_FILE"),
    TEXT_FILE("TEXT_FILE"),
    LARGE_FILE("LARGE_FILE"),
    ERROR("ERROR");

    private final String name;

    FileCheck(String name) {
        this.name = name;
    }

    public boolean equals(FileMode mode) {
        return mode.toString().equals(this.toString());
    }
    @Override
    public String toString() {
        return name;
    }

    public FileCheck toEnum(String name) {
        if (name.equals(FileCheck.BINARY_FILE.toString())) {
            return BINARY_FILE;
        }
        else if (name.equals(FileCheck.TEXT_FILE.toString())) {
            return TEXT_FILE;
        }
        else if (name.equals(FileCheck.LARGE_FILE.toString())) {
            return LARGE_FILE;
        }
        else if (name.equals(FileCheck.ERROR.toString())) {
            return ERROR;
        }
        else {
            return null;
        }
    }
}
