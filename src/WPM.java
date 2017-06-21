import java.util.ArrayList;

/**
 * Created by kash on 6/11/17.
 */
public class WPM {

    ArrayList<P_Properties> capList;
    Logging logger = new Logging("", -1, -1);

    // This class should have methods that include
    // the logging methods from the Logging class

    public WPM(ArrayList<P_Properties> capList) {
        this.capList = capList;
    }

    /**
     * Uses an arrayList as a parameter, which
     * is now in the constructor
     * @return
     */
    public ArrayList<Integer> sepCapWPM() {
        return new ArrayList<Integer>();
    }

    public int wholeWPM() {
        return 1;
    }

    public int numOfWords() {

        return 2;

    }

}
