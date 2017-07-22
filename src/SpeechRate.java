import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kash on 6/11/17.
 */
class SpeechRate {

    private ArrayList<CaptionProp> capList;
    private String logFile;

    public SpeechRate(ArrayList<CaptionProp> capList, String logFile) {
        this.capList = capList;
        this.logFile = logFile;
    }

    private Logging loggerSpeech = new Logging(-1, -1);

    /**
     * Uses an arrayList as a parameter, in constructor.
     * Use Integer.parseInt() to convert to an int.
     * Not completed yet.
     * @return  ArrayList of Doubles indicating the number
     *          of words speaker speaks per second in each
     *          individual caption
     *
     */
    public ArrayList<Double> sepCapWPSec() throws Exception {
        ArrayList<Double> countArray = new ArrayList<>();
        for (CaptionProp thing : capList) {
            countArray.add( wordsInString(thing.getText()) / secondsElapsed(thing.getBegin(), thing.getEnd()) );
        }
        return countArray;
    }

    /**
     * Uses ArrayList of CaptionProp objects, taken from
     * the constructor.
     *
     * @return  Total number of words divided by the
     *          total time of the lecture
     */
    public double wholeWPSec() throws Exception {
        int count = 0;
        for (CaptionProp thing : capList) {
            count += wordsInString(thing.getText());
        }
        return count / secondsElapsed( capList.get(0).getBegin(), capList.get(capList.size() - 1).getEnd() );
    }

    /**
     * Calculates number of words in string by adding 1 to the number of spaces
     * @param str  String to search
     * @return  Number of words
     */
    public int wordsInString(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                count++;
            }
        }
        return count + 1;
    }

    /**
     * Total time of the lecture in seconds
     * @param time1  Beginning time of the lecture
     *               (normally 00:00:00.000)
     *               If not true, still have to find why.
     * @param time2  Ending time of the lecture
     * @return  Lecture length in seconds
     */
    public double secondsElapsed(String time1, String time2) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("CST")); // Offset of 64800000 msec
        Date dateBegin = sdf.parse(time1);
        Date dateEnd = sdf.parse(time2);
        double difference = dateEnd.getTime() - dateBegin.getTime();
        return (difference - 64800000) / 1000;

    }

}
