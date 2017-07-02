import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kash on 7/1/17.
 */
public class AnalyzePronouns {

    private String captions;

    public String[] persArr = {"I", "me", "myself"};
    public String[] audArr = {"you", "yourself", "yourselves", "we"};
    public String[] thirdArr = {"his", "her", "himself", "herself", "them", "themselves", "they"};

    public AnalyzePronouns(String captions) {
        this.captions = captions;
    }

    /*
       Search for the words when they are surrounded by spaces or when they are
       preceded by a space and are followed by a period.
       For example, looking for "I":
           |-> Look for " I " and " I."
     */

    /**
     * Only thing in this class that uses the constructor
     * @param input
     * @return
     */
    public ArrayList<PronounCount> countTraverse(String[] input) {
        ArrayList<PronounCount> arrCount = new ArrayList<>();
        for (String thing : input) {
            String spaceWordSpace = " " + thing + " ";
            String spaceWordPeriod = " " + thing + ".";
            int count = countMatches(captions, spaceWordSpace) + countMatches(captions, spaceWordPeriod);
            arrCount.add(new PronounCount(thing, count));
        }
        return arrCount;
    }

    public int totalCount(ArrayList<PronounCount> input) {
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
