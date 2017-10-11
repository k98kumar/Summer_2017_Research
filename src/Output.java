import java.util.ArrayList;

/*
 * Copyright 2017 Koushhik Kumar. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
           String outputFile, Summarizer summarizer) {
        this.personalPronouns = personalPronouns;
        this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;
        this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;
        this.thirdPersonArray = thirdPersonArray;
        this.outputFile = outputFile;
        this.summarizer = summarizer;
    }


    /**
     * Prints all Pronouns in the array and the number of occurrences.
     * Prints in the form Pronoun:Count.
     */
    void printEverything() {
        String showQuant = "Pronoun\tCount";
        showQuant += concatArrayList(personalArray) + concatArrayList(audienceArray) + concatArrayList(thirdPersonArray);
        Logging.appendIntoFile(outputFile, showQuant);
    }

    /**
     * Creates String representation of PronounCount ArrayList
     * @param arrList  PronounCount ArrayList of occurrences
     * @return  String representation
     */
    private String concatArrayList(ArrayList<PronounCount> arrList) {
        String concat = "";
        for (PronounCount pronounCount : arrList) {
            concat += "\n" + pronounCount.getPronoun() + "\t" + pronounCount.getCount();
        }
        return concat;
    }

}
