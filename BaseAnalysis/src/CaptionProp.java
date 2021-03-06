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
public class CaptionProp {

    // -------------------------------------- //
    //     Time format is in HH:mm:ss.SSS     //
    // -------------------------------------- //

    private String begin;
    private String end;
    private String timeDiff;
    private String region;
    private String text;
    private static final String DELIMITOR = ",";

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
