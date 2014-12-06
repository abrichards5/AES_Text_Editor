package view.data;

/**
 * Created by Alex on 06/12/14.
 */
public class FindParams {

    public String word;
    public boolean caseSensitive;
    public boolean useRegex;

    public FindParams(String word, boolean caseSensitive, boolean useRegex) {
        this.word = word;
        this.caseSensitive = caseSensitive;
        this.useRegex = useRegex;
    }

    public FindParams() {
        this.word = "";
        this.caseSensitive = false;
        this.useRegex = false;
    }
}
