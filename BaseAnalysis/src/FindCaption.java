import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
public class FindCaption {

    private ArrayList<CaptionProp> captions;
    private String time;
    private String logFile;
    private Date date;

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
        for (CaptionProp captionProp : captions) {
            if (checkDatePrecedesOrEqualsCaptionDate(convertToDateTime(time), convertToDateTime(captionProp.getEnd()))) {
                toReturn = captionProp;
                break;
            }
        }
        if (toReturn.getText() == null) return "";
        return toReturn.getText();
    }

    /*
    public void convertToDateTimeOutsideClass() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("CST"));
        date = sdf.parse(time);
    }
    */

    private Date convertToDateTime(String capTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("CST"));
        return sdf.parse(capTime);
    }

    private boolean checkDatePrecedesOrEqualsCaptionDate(Date checkDate, Date captionDate) {
        return ((captionDate.compareTo(checkDate)) >= 0);
    }

}
