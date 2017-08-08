import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kash on 7/1/17.
 */
class AnalyzePronouns {

    public int personalPronounCount, audiencePronounCount, thirdPersonPronounCount;
    public ArrayList<PronounCount> personalPronounArray, audiencePronounArray, thirdPersonPronounArray = new ArrayList<>();


    private String[] persArr = {"I", "me", "myself"};
    private String[] audArr = {"you", "yourself", "yourselves", "we"};
    private String[] thirdArr = {"his", "her", "himself", "herself", "them", "themselves", "they"};

    private String captions;
    private String logFile;
    private String outputFile;
    private String outputFile_list;

    AnalyzePronouns(String captions, String logFile, String outputFile, String outputFile_list) {
        this.captions = captions;
        this.logFile = logFile;
        this.outputFile = outputFile;
        this.outputFile_list = outputFile_list;
    }

    AnalyzePronouns(String captions, String logFile, String outputFile) {
        this.captions = captions;
        this.logFile = logFile;
        this.outputFile = outputFile;
    }

    private Logging loggerPronouns = new Logging(0, null, 0, null, 0, null, null);
    private Output output = new Output(0, null, 0, null, 0, null, null, null);


    /*
       Search for the words when they are surrounded by spaces or when they are
       preceded by a space and are followed by a period.
       For example, looking for "I":
           |-> Look for " I " and " I."
     */

    void compAP() {
        loggerPronouns.logFile = logFile;
        output.outputFile = outputFile;
        output.outputFile_list = outputFile_list;
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
     * Only thing in this class that uses the constructor
     * @param input
     * @return
     */
    private ArrayList<PronounCount> countTraverse(String[] input) {
        ArrayList<PronounCount> arrCount = new ArrayList<>();
        for (String str : input) {
            String spaceWordSpace = " " + str + " ";
            String spaceWordPeriod = " " + str + ".";
            int count = countMatches(captions, spaceWordSpace) + countMatches(captions, spaceWordPeriod);
            arrCount.add(new PronounCount(str, count));
        }
        return arrCount;
    }

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
     * @return length of a String
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
     * @return boolean indicating if string is empty
     */
    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
