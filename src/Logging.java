import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kash on 6/14/17.
 */
public class Logging {

    private int numberOfWords;
    private double secondsSpoken;
    int personalPronouns;
    ArrayList<PronounCount> personalArray;
    int audiencePronouns;
    ArrayList<PronounCount> audienceArray;
    int thirdPersonPronouns;
    ArrayList<PronounCount> thirdPersonArray;
    String logFile;

    /**
     * To show what file to insert logs
     * @param logFile log file
     */
    Logging(String logFile) {
        this.logFile = logFile;
    }

    /**
     * Constructor that contains all information
     * Just call like: Logging hello = new Logging(-1, -1)
     *                 hello.numberOfWords = 250;
     *                 hello.secondsSpoken = 60.5
     * @param numberOfWords  Number of words counted in documnet
     * @param secondsSpoken  Length of time speaker spoke
     */
    Logging(int numberOfWords, double secondsSpoken) {
        this.numberOfWords = numberOfWords;
        this.secondsSpoken = secondsSpoken;
    }

    Logging(int personalPronouns, ArrayList<PronounCount> personalArray,
            int audiencePronouns, ArrayList<PronounCount> audienceArray,
            int thirdPersonPronouns, ArrayList<PronounCount> thirdPersonArray,
            String logFile) {
        this.personalPronouns = personalPronouns;
        this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;
        this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;
        this.thirdPersonArray = thirdPersonArray;
        this.logFile = logFile;
    }

    // START Overall Log Methods
    void startProgram() {
        String timeStamp = currentDateTimeString() + "Started Analyzing " + getFileName(logFile) + "\n\n";
        appendIntoFile(logFile, timeStamp);
    }

    void finishedParsingDoc() {
        String timeStamp = currentDateTimeString() + "Finished Parsing " + getFileName(logFile) + "\n\n";
        appendIntoFile(logFile, timeStamp);
    }

    void endProgram() {
        String timeStamp = currentDateTimeString() + "Finished Analyzing " + getFileName(logFile) + "\n\n---\n\n";
        appendIntoFile(logFile, timeStamp);
    }
    // END Overall Log Methods

    // START SpeechRate.java
    void finishedCountingWords() {
        String label = "Number of Words Spoken:\t";
        label += numberOfWords + "\n\n";
        String timeStamp = currentDateTimeString() + "Words Spoken Calculated\n";
        appendIntoFile(logFile, timeStamp + label);
    }

    void calculatedTimeSpoken() {
        String label = "Amount of Time Spoken:\t";
        label += secondsSpoken + "sec\n\n";
        String timeStamp = currentDateTimeString() + "Time Spoken Calculated\n";
        appendIntoFile(logFile, timeStamp + label);
    }

    void calculatedAverageRateOfSpeech() {
        String label = "Average Rate of Speech:\t";
        label += numberOfWords / secondsSpoken + "words/sec\n\n";
        String timeStamp = currentDateTimeString() + "Rate of Speech Calculated\n";
        appendIntoFile(logFile, timeStamp + label);
    }
    // END SpeechRate.java

    // START AnalyzePronouns.java
    void numberOfPersonalPronouns() {
        String label = "Number of Personal Pronouns: ";
        label += personalPronouns + "\n";
        for (PronounCount thing : personalArray) {
            label += "\t" + thing.getPronoun() + ": " + thing.getCount() + "\n";
        }
        label += "\n";
        String timeStamp = currentDateTimeString() + "Personal Pronouns Totalled\n";
        appendIntoFile(logFile, timeStamp + label);
    }

    void numberOfAudiencePronouns() {
        String label = "Number of Audience Pronouns: ";
        label += audiencePronouns + "\n";
        for (PronounCount thing : audienceArray) {
            label += "\t" + thing.getPronoun() + ": " + thing.getCount() + "\n";
        }
        label += "\n";
        String timeStamp = currentDateTimeString() + "Audience Pronouns Totalled\n";
        appendIntoFile(logFile, timeStamp + label);
    }

    void numberOfThirdPersonPronouns() {
        String label = "Number of Third Person Pronouns: ";
        label += thirdPersonPronouns + "\n";
        for (PronounCount thing : thirdPersonArray) {
            label += "\t" + thing.getPronoun() + ": " + thing.getCount() + "\n";
        }
        label += "\n";
        String timeStamp = currentDateTimeString() + "Third Person Pronouns Totalled\n";
        appendIntoFile(logFile, timeStamp + label);
    }
    // END AnalyzePronouns.java

    // START Helper Methods
    private String currentDateTimeString() {
        DateFormat DF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Date date = new Date();
        return DF.format(date) + " | ";
    }

    static void appendIntoFile(String filePath, String text) {
        Path path = Paths.get(filePath);
        File file = new File(filePath);
        try {
            if (file.exists()) {
                Files.write(path, text.getBytes(), StandardOpenOption.APPEND);
            } else {
                Files.write(path, text.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String file) {
        return file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));
    }
    // END Helper Methods

}
