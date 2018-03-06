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
class AnalyzePronouns {

    private String[] persArr = {"I", "me", "myself"};
    private String[] audArr = {"you", "yourself", "yourselves", "we"};
    private String[] thirdArr = {"he", "his", "her", "himself", "herself", "them", "themselves", "they"};
    private String[] generalArr = {"one", "it"};

    private String captions;
    private String logFile;
    private String outputFile;
    private Summarizer summarizer;

    AnalyzePronouns() {
    }

    AnalyzePronouns(String captions, String logFile, String outputFile, Summarizer summarizer) {
        this.captions = captions;
        this.logFile = logFile;
        this.outputFile = outputFile;
        this.summarizer = summarizer;
    }

    private Output output = new Output(0, null, 0, null, 0, null, 0, null, null, null);
    private Logging loggerPronouns = new Logging(output, null);

    public String[] getPersArr() {
        return persArr;
    }

    public String[] getAudArr() {
        return audArr;
    }

    public String[] getThirdArr() {
        return thirdArr;
    }

    public String[] getGeneralArr() {
        return generalArr;
    }

    /**
     * Assigns values to all variables in Logging and Output objects.
     * Executes printPersonalPronouns(), printAudiencePronouns(),
     * and printThirdPersonPronouns() to show in Logging text files.
     * Executes printEverythingOutput() command to show in Output text files.
     */
    void comprehensiveAP() {
        loggerPronouns.logFile = logFile;
        output.outputFile = outputFile;
        output.personalArray = countTraverse(persArr);
        output.personalPronouns = totalCount(output.personalArray);
        loggerPronouns = new Logging(output, logFile);
        loggerPronouns.printPronounGroup("personal");
        output.audienceArray = countTraverse(audArr);
        output.audiencePronouns = totalCount(output.audienceArray);
        loggerPronouns = new Logging(output, logFile);
        loggerPronouns.printPronounGroup("audience");
        output.thirdPersonArray = countTraverse(thirdArr);
        output.thirdPersonPronouns = totalCount(output.thirdPersonArray);
        loggerPronouns = new Logging(output, logFile);
        loggerPronouns.printPronounGroup("third");
        output.generalArray = countTraverse(generalArr);
        output.generalPronouns = totalCount(output.thirdPersonArray);
        loggerPronouns = new Logging(output, logFile);
        loggerPronouns.printPronounGroup("general");
        output.printEverythingOutput();
        if (summarizer != null) {
            output.summarizer = summarizer;
            output.summarizer.concatOutputDataTSV = "";
            output.printEverythingSummarizer();
        }
    }

    /**
     * Counts number of occurrences of each pronoun surrounded by:
     *     1) Space and Space
     *     2) Space and Period
     *     3) Space and Apostrophe
     * @param input  Each pronoun String array
     * @return  Number of occurrences
     */
    private ArrayList<PronounCount> countTraverse(String[] input) {
        ArrayList<PronounCount> arrCount = new ArrayList<>();
        for (String str : input) {
            String spaceWordSpace = " " + str + " ";
            String spaceWordPeriod = " " + str + ".";
            String spaceWordApostrophe = " " + str + "'";
            int count = countMatches(captions.toLowerCase(), spaceWordSpace)
                    + countMatches(captions.toLowerCase(), spaceWordPeriod) + countMatches(captions.toLowerCase(), spaceWordApostrophe);
            arrCount.add(new PronounCount(str, count));
        }
        return arrCount;
    }

    /**
     * Adds all pronouns of a type (First, Audience, Third)
     * @param input  Each pronoun arrayList
     * @return  Sum of number of pronouns
     */
    private int totalCount(ArrayList<PronounCount> input) {
        int total = 0;
        for (PronounCount thing : input) {
            total += thing.getCount();
        }
        return total;
    }

    /**
     * Apache StringUtils countMatches(String, String) method
     * @param str  Larger string that is to be checked
     * @param sub  String that may be contained in larger string
     * @see  "https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/src-html/org/apache/commons/lang/StringUtils.html"
     *       Starts on Line 5318
     * @return  Length of a String
     */
    private static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }

    /**
     * Apache StringUtils isEmpty(String) method
     * @param str  String being checked if empty
     * @see  "https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/src-html/org/apache/commons/lang/StringUtils.html"
     *       Starts on Line 194
     * @return  Boolean indicating if string is empty
     */
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
