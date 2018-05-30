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
 * Created by Koushhik Kumar.
 * <p>
 * Copyright 2017 Koushhik Kumar. All Rights Reserved. </p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at </p>
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0 </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. </p>
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
    int generalPronouns;
    ArrayList<PronounCount> generalArray;
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
     * @param numberOfWords  Number of words counted in document
     * @param secondsSpoken  Length of time speaker spoke
     */
    Logging(int numberOfWords, double secondsSpoken) {
        this.numberOfWords = numberOfWords;
        this.secondsSpoken = secondsSpoken;
    }

    Logging(Output output, String logFile) {
        this.personalPronouns = output.personalPronouns;        this.personalArray = output.personalArray;
        this.audiencePronouns = output.audiencePronouns;        this.audienceArray = output.audienceArray;
        this.thirdPersonPronouns = output.thirdPersonPronouns;  this.thirdPersonArray = output.thirdPersonArray;
        this.generalPronouns = output.generalPronouns;          this.generalArray = output.generalArray;
        this.logFile = logFile;
    }

    Logging(int personalPronouns, ArrayList<PronounCount> personalArray,
            int audiencePronouns, ArrayList<PronounCount> audienceArray,
            int thirdPersonPronouns, ArrayList<PronounCount> thirdPersonArray,
            int generalPronouns, ArrayList<PronounCount> generalArray,
            String logFile) {
        this.personalPronouns = personalPronouns;        this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;        this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;  this.thirdPersonArray = thirdPersonArray;
        this.generalPronouns = generalPronouns;          this.generalArray = generalArray;
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

    // START SpeechRate.java Methods
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
    // END SpeechRate.java Methods

    // START AnalyzePronouns.java Methods
    void printPronounGroup(String type) {
        String label = "", timeStamp, timeStampSuffix = "";
        ArrayList<PronounCount> totalPronounArray = new ArrayList<>();
        switch (type.toLowerCase()) {
            case "personal" :
                label = "Personal Pronoun Count: " + personalPronouns + "\n";
                totalPronounArray = personalArray;
                timeStampSuffix = "Personal";
                break;
            case "audience" :
                label = "Audience Pronoun Count: " + audiencePronouns + "\n";
                totalPronounArray = audienceArray;
                timeStampSuffix = "Audience";
                break;
            case "third" :
                label = "Third Person Pronoun Count: " + thirdPersonPronouns + "\n";
                totalPronounArray = thirdPersonArray;
                timeStampSuffix = "Third Person";
                break;
            case "general" :
                label = "General Pronoun Count: " + generalPronouns + "\n";
                totalPronounArray = generalArray;
                timeStampSuffix = "General";
                break;
            default :
                break;
        }

        label += traversePronounArray(totalPronounArray);
        timeStamp = currentDateTimeString();
        timeStampSuffix += " Pronouns Totalled\n";

        appendIntoFile(logFile, timeStamp + timeStampSuffix + label);

    }
    // END AnalyzePronouns.java Methods

    // START Helper Methods
    private String traversePronounArray(ArrayList<PronounCount> type) {
        String ret = "";
        for (PronounCount thing : type) {
            ret += "\t" + thing.getPronoun() + ": " + thing.getCount() + "\n";
        }
        ret += "\n";
        return ret;
    }

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
