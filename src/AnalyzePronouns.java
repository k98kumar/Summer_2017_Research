import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kash on 7/1/17.
 */
class AnalyzePronouns {

    public int personalPronounCount, audiencePronounCount, thirdPersonPronounCount;
    public ArrayList<PronounCount> personalPronounArray = new ArrayList<>(), audiencePronounArray = new ArrayList<>(), thirdPersonPronounArray = new ArrayList<>();

    private String[] persArr = {"I", "me", "myself"};
    private String[] audArr = {"you", "yourself", "yourselves", "we"};
    private String[] thirdArr = {"he", "his", "her", "himself", "herself", "them", "themselves", "they"};

    private String captions;
    private String logFile;
    private String outputFile;

    AnalyzePronouns(String captions, String logFile, String outputFile) {
        this.captions = captions;
        this.logFile = logFile;
        this.outputFile = outputFile;
    }

    private Logging loggerPronouns = new Logging(0, null, 0, null, 0, null, null);
    private Output output = new Output(0, null, 0, null, 0, null, null);


    /*
       Search for the words when they are surrounded by spaces or when they are
       preceded by a space and are followed by a period.
       For example, looking for "I":
           |-> Look for " I ", " I.", and " I'"
     */

    /**
     * Assigns values to all variables in Logging and Output objects.
     * Executes numberOfPersonalPronouns(), numberOfAudiencePronouns(),
     * and numberOfThirdPersonPronouns() to show in Logging text files.
     * Executes printEverything() command to show in Output text files.
     */
    void compAP() {
        personalPronounArray.clear(); audiencePronounArray.clear(); thirdPersonPronounArray.clear();
        loggerPronouns.logFile = logFile;
        output.outputFile = outputFile;
        personalPronounArray = loggerPronouns.personalArray = output.personalArray = countTraverse(persArr);
        personalPronounCount = loggerPronouns.personalPronouns = output.personalPronouns = totalCount(loggerPronouns.personalArray);
        loggerPronouns.numberOfPersonalPronouns();
        audiencePronounArray = loggerPronouns.audienceArray = output.audienceArray = countTraverse(audArr);
        audiencePronounCount = loggerPronouns.audiencePronouns = output.audiencePronouns = totalCount(loggerPronouns.audienceArray);
        loggerPronouns.numberOfAudiencePronouns();
        thirdPersonPronounArray = loggerPronouns.thirdPersonArray = output.thirdPersonArray = countTraverse(thirdArr);
        thirdPersonPronounCount = loggerPronouns.thirdPersonPronouns = output.thirdPersonPronouns = totalCount(loggerPronouns.thirdPersonArray);
        loggerPronouns.numberOfThirdPersonPronouns();
        output.printEverything();
    }

    void compAPNoLog() {

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
            int count = countMatches(captions.toLowerCase(), spaceWordSpace) + countMatches(captions.toLowerCase(), spaceWordPeriod) + countMatches(captions.toLowerCase(), spaceWordApostrophe);
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
     * @see  "https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/src-html/org/apache/commons/lang/StringUtils.html#line.5318"
     *       Starts on Line 194
     * @return  Boolean indicating if string is empty
     */
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
