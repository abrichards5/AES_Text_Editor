package al.aesencryptor.enums;

/**
 * Created by alutman on 11-Sep-14.
 *
 */
public enum Encoding {

    HEX("HEX"),
    BASE64("BASE64"),
    NONE("NONE");


    private final String name;

    Encoding(String name) {
        this.name = name;
    }

    public boolean equals(Encoding mode) {
        return mode.toString().equals(this.toString());
    }
    @Override
    public String toString() {
        return name;
    }

    public static Encoding toEnum(String name) {
        name = name.toUpperCase();
        if (name.equals(Encoding.HEX.toString())) {
            return HEX;
        }
        else if (name.equals(Encoding.BASE64.toString())) {
            return BASE64;
        }
        else if (name.equals(Encoding.NONE.toString())) {
            return NONE;
        }
        else {
            return null;
        }
    }
}
