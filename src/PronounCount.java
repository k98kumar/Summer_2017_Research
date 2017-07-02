/**
 * Created by kash on 7/1/17.
 */
public class PronounCount {
    private String pronoun;
    private int count;

    public PronounCount (String pronoun, int count) {
        this.pronoun = pronoun;
        this.count = count;
    }

    public String getPronoun() {
        return pronoun;
    }

    public int getCount() {
        return count;
    }
}
