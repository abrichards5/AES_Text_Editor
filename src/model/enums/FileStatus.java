package model.enums;

/**
 * Created by alutman on 7/04/14.
 *
 * Enumerated values for return types when checking a file
 *
 */
public enum FileStatus {

    BINARY_FILE("BINARY_FILE"),
    TEXT_FILE("TEXT_FILE"),
    LARGE_FILE("LARGE_FILE"),
    ERROR("ERROR");

    private final String name;

    FileStatus(String name) {
        this.name = name;
    }

    public boolean equals(FileStatus mode) {
        return mode.toString().equals(this.toString());
    }
    @Override
    public String toString() {
        return name;
    }

    public FileStatus toEnum(String name) {
        if (name.equals(FileStatus.BINARY_FILE.toString())) {
            return BINARY_FILE;
        }
        else if (name.equals(FileStatus.TEXT_FILE.toString())) {
            return TEXT_FILE;
        }
        else if (name.equals(FileStatus.LARGE_FILE.toString())) {
            return LARGE_FILE;
        }
        else if (name.equals(FileStatus.ERROR.toString())) {
            return ERROR;
        }
        else {
            return null;
        }
    }
}
