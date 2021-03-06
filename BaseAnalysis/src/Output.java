import java.util.ArrayList;

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
public class Output {

    int numberOfWords;
    double secondsSpoken;
    int personalPronouns;
    ArrayList<PronounCount> personalArray;
    int audiencePronouns;
    ArrayList<PronounCount> audienceArray;
    int thirdPersonPronouns;
    ArrayList<PronounCount> thirdPersonArray;
    int generalPronouns;
    ArrayList<PronounCount> generalArray;
    String outputFile;
    Summarizer summarizer;

    Output() {
    }

    Output(int numberOfWords, double secondsSpoken) {
        this.numberOfWords = numberOfWords;
        this.secondsSpoken = secondsSpoken;
    }

    Output(int personalPronouns, ArrayList<PronounCount> personalArray,
           int audiencePronouns, ArrayList<PronounCount> audienceArray,
           int thirdPersonPronouns, ArrayList<PronounCount> thirdPersonArray,
           int generalPronouns, ArrayList<PronounCount> generalArray,
           String outputFile, Summarizer summarizer) {
        this.personalPronouns = personalPronouns;           this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;           this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;     this.thirdPersonArray = thirdPersonArray;
        this.generalPronouns = generalPronouns;             this.generalArray = generalArray;
        this.outputFile = outputFile;                       this.summarizer = summarizer;
    }


    /**
     * Prints all Pronouns in the array and the number of occurrences.
     * Prints in the form Pronoun:Count.
     */
    void printEverythingOutput() {
        String showQuant = "Pronoun\tCount";
        showQuant += convertPrintOutput(personalArray) + convertPrintOutput(audienceArray) + convertPrintOutput(thirdPersonArray) + convertPrintOutput(generalArray);
        Logging.appendIntoFile(outputFile, showQuant);
    }

    void printEverythingSummarizer() {
        convertPrintSummarizer(personalArray); convertPrintSummarizer(audienceArray); convertPrintSummarizer(thirdPersonArray); convertPrintSummarizer(generalArray);
        String valueLine = summarizer.fileName + summarizer.concatOutputDataTSV + "\n";
        Logging.appendIntoFile(summarizer.summarizerFile, valueLine);
    }

    /**
     * Creates String representation of PronounCount ArrayList
     * @param pronounList  PronounCount ArrayList of occurrences
     * @return  String representation
     */
    private String convertPrintOutput(ArrayList<PronounCount> pronounList) {
        String print = "";
        for (PronounCount pronounCount : pronounList) {
            print += "\n" + pronounCount.getPronoun() + "\t" + pronounCount.getCount();
        }
        return print;
    }

    private void convertPrintSummarizer(ArrayList<PronounCount> pronounList) {
        for (PronounCount pronounCount : pronounList) {
            summarizer.concatOutputDataTSV += "\t" + pronounCount.getCount();
        }
    }
}
