package model;

/**
 * Created by alutman on 7/04/14.
 */
public enum FileMode {
    
    TEXT("TEXT"),
    BINARY("BINARY");
    
    String name;
    
    FileMode(String name) {
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
