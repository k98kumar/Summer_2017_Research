import java.util.ArrayList;

/**
 * Created by kash on 6/14/17.
 */
public class Logging {

    private String speech;
    private int numberOfWords;
    private double secondsSpoken;
    private int personalPronouns;
    private ArrayList<PronounCount> personalArray;
    private int audiencePronouns;
    private ArrayList<PronounCount> audienceArray;
    private int thirdPersonPronouns;
    private ArrayList<PronounCount> thirdPersonArray;
    private int newWords;

    /**
     * Constructor that contains all information
     * Just call like: Logging hello = new Logging(false, -1, -1)
     *                 hello.parsed = true;
     *                 hello.numberOfWords = 250;
     *                 hello.secondsSpoken = 60.5
     * @param speech  String of all captions merged together
     * @param numberOfWords  Number of words counted in documnet
     * @param secondsSpoken  Length of time speaker spoke
     */
    public Logging(String speech, int numberOfWords, double secondsSpoken) {
        this.speech = speech;
        this.numberOfWords = numberOfWords;
        this.secondsSpoken = secondsSpoken;
    }

    public Logging(int personalPronouns, ArrayList<PronounCount> personalArray,
                   int audiencePronouns, ArrayList<PronounCount> audienceArray,
                   int thirdPersonPronouns, ArrayList<PronounCount> thirdPersonArray, int newWords) {
        this.personalPronouns = personalPronouns;
        this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;
        this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;
        this.thirdPersonArray = thirdPersonArray;
        this.newWords = newWords;
    }

    public void finishedParsingDoc() {
        String label = "Finished Parsing Document\n\n";
    }

    public void finishedCountingWords() {
        String label = "Number of Words Spoken:\t";
        label += numberOfWords + "\n";
        // Now append it into the log file
    }

    public void calculatedTimeSpoken() {
        String label = "Amount of Time Spoken:\t";
        label += secondsSpoken + "sec\n";
        // Now append it into the log file
    }

    public void calculatedAverageRate() {
        String label = "Average Rate of Speech:\t";
        label += numberOfWords / secondsSpoken + "words/sec";
        // Now append it into the log file
    }

    public void numberOfPersonalPronouns() {
        String label = "Number of Personal Pronouns:\t";
        label += personalPronouns + "\n";
        for (PronounCount thing : personalArray) {
            label += "\t" + thing.getPronoun() + ":\t" + thing.getCount() + "\n";
        }
        // Now append it into the log file
    }

    public void numberOfAudiencePronouns() {
        String label = "Number of Audience Pronouns:\t";
        label += audiencePronouns + "\n";
        for (PronounCount thing : audienceArray) {
            label += "\t" + thing.getPronoun() + ":\t" + thing.getCount() + "\n";
        }
        // Now append it into the log file
    }

    public void numberOfThirdPersonPronouns() {
        String label = "Number of Third Person Pronouns:\t";
        label += thirdPersonPronouns + "\n";
        for (PronounCount thing : thirdPersonArray) {
            label += "\t" + thing.getPronoun() + ":\t" + thing.getCount() + "\n";
        }
        // Now append it into the log file
    }

    public void newConceptWords() {

    }
}
