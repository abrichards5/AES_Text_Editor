package model;

/**
 * Created by alutman on 7/04/14.
 */
public enum FileCheck {

    BINARY_FILE("BINARY_FILE"),
    TEXT_FILE("TEXT_FILE"),
    LARGE_FILE("LARGE_FILE");

    String name;

    FileCheck(String name) {
        this.name = name;
    }

    public boolean equals(FileMode mode) {
        if (mode.toString().equals(this.toString())) {
            return true;
        }
        return false;
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
        else {
            return null;
        }
    }
}
