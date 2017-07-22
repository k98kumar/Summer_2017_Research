/**
 * Created by kash on 5/28/17.
 */
public class CaptionProp {

    // -------------------------------------- //
    //     Time format is in HH:mm:ss.SSS     //
    // -------------------------------------- //

    String begin;
    String end;
    String timeDiff;
    String region;
    String text;
    static final String DELIMITOR = ",";

    public CaptionProp(String begin, String end, String timeDiff, String region, String text) {
        this.begin = begin;
        this.end = end;
        this.timeDiff = timeDiff;
        this.region = region;
        this.text = text;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getTimeDiff() {
        return timeDiff;
    }

    public String getRegion() {
        return region;
    }

    public String getText() {
        return text;
    }

    public static String toString(CaptionProp singleObject) {
        String begin = "Begin: " + singleObject.begin + "\n";
        String end = "End: " + singleObject.end + "\n";
        String timeDiff = "Time Difference: " + singleObject.timeDiff + "\n";
        String region = "Region: " + singleObject.region + "\n";
        String text = "Text: " + singleObject.text + "\n\n";

        return begin + end + timeDiff + region + text;
    }

    public static String toStringCSV(CaptionProp singleObject) {
        String begin = "Begin:" + DELIMITOR + singleObject.begin + DELIMITOR;
        String end = "End: " + DELIMITOR + singleObject.end + DELIMITOR;
        String timeDiff = "Diff: " + DELIMITOR + singleObject.timeDiff + DELIMITOR;
        String region = "Reg:" + DELIMITOR + singleObject.region + "\n";
        String text = "" + DELIMITOR + singleObject.text + "\n\n";

        return  begin + end + timeDiff + region + text;
    }

}
