
/**
 * Created by kash on 6/14/17.
 */
public class Logging {

    private String speech;
    private int numberOfWords;
    private double secondsSpoken;

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
        label += numberOfWords/secondsSpoken + "words/sec";
        // Now append it into the log file
    }

}
