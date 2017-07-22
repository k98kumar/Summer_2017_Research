import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kash on 7/9/17.
 */
public class FindCaption {

    ArrayList<CaptionProp> captions;
    String time;
    String logFile;
    Date date;

    public FindCaption(ArrayList<CaptionProp> captions, String time, String logFile, Date date) {
        this.captions = captions;
        this.time = time;
        this.logFile = logFile;
        this.date = date;
    }

    // To find the caption without Date date in class:
    //     FindCaption FC = new FindCaption(captions, time, logFile);
    //     CaptionProp CP = FC.findCaptionFromTime(FC.convertToDateTime());
    // To find the caption with Date date in class:
    //     FindCaption FC = new FindCaption(captions, time, logFile, null);
    //     CaptionProp CP = FC.convertToDateTime().findCaptionFromTime();

    public String findCaptionTextFromTime() throws ParseException, NullPointerException {
        CaptionProp toReturn = null;
        for (CaptionProp c : captions) {
            if (checkDatePrecedesOrEqualsCaptionDate(convertToDateTimeInsideClass(time), convertToDateTimeInsideClass(c.getEnd()))) {
                toReturn = c;
                break;
            }
        }
        if (toReturn.getText() == null) return "";
        return toReturn.getText();
    }

    public void convertToDateTimeOutsideClass() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("CST"));
        date = sdf.parse(time);
    }

    private Date convertToDateTimeInsideClass(String capTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("CST"));
        return sdf.parse(capTime);
    }

    private boolean checkDatePrecedesOrEqualsCaptionDate(Date checkDate, Date captionDate) {
        return ((captionDate.compareTo(checkDate)) >= 0);
    }

}
