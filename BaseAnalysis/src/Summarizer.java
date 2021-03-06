import java.util.ArrayList;
import java.util.Arrays;

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
public class Summarizer {

    /**
     * Will be used for final file with cumulative amounts.
     * Will be used in Output.java as global variable. Keep on concatenating
     * to the variable in this file.
     */

    String summarizerFile;
    String concatOutputDataTSV;
    String fileName;

    Summarizer() {
    }

    Summarizer(String summarizerFile, String concatOutputDataTSV, String fileName) {
        this.summarizerFile = summarizerFile;
        this.concatOutputDataTSV = concatOutputDataTSV;
        this.fileName = fileName;
    }

    private void printConcatData() {
        Logging.appendIntoFile(summarizerFile, concatOutputDataTSV);
    }

    void printLabelLine() {
        AnalyzePronouns analyzePronouns = new AnalyzePronouns();
        ArrayList<String> allPronouns = new ArrayList<>();
        allPronouns.addAll(Arrays.asList(analyzePronouns.getPersArr()));
        allPronouns.addAll(Arrays.asList(analyzePronouns.getAudArr()));
        allPronouns.addAll(Arrays.asList(analyzePronouns.getThirdArr()));
        allPronouns.addAll(Arrays.asList(analyzePronouns.getGeneralArr()));
        String labelLine = "";
        for (String pronoun : allPronouns) {
            labelLine += "\t" + pronoun;
        }
        labelLine += "\n";
        Logging.appendIntoFile(summarizerFile, labelLine);
    }

    void printValueLine() {
        String valueLine = fileName + "\t" + concatOutputDataTSV + "\n";
        Logging.appendIntoFile(summarizerFile, valueLine);
    }

}
