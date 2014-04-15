package model;

/**
 * Created by alutman on 7/04/14.
 *
 * Enumerated values for the different modes a file can be used in
 *
 */
public enum FileMode {
    
    TEXT("TEXT"),
    BINARY("BINARY");
    
    private final String name;
    
    FileMode(String name) {
        this.name = name;
    }
    
    public boolean equals(FileMode mode) {
        return mode.toString().equals(this.toString());
    }
    @Override
    public String toString() {
        return name;
    }
    
    public FileMode toEnum(String name) {
        if (name.equals(FileMode.TEXT.toString())) {
            return TEXT;
        }
        else if (name.equals(FileMode.BINARY.toString())) {
            return BINARY;
        }
        else {
            return null;
        }
    }
}
