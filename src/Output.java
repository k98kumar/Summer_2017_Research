import java.util.ArrayList;

/**
 * Created by kash on 7/23/17.
 */
public class Output {

    int numberOfWords;
    double secondsSpoken;
    int personalPronouns;
    ArrayList<PronounCount> personalArray;
    int audiencePronouns;
    ArrayList<PronounCount> audienceArray;
    int thirdPersonPronouns;
    ArrayList<PronounCount> thirdPersonArray;
    String outputFile;


    public Output(int numberOfWords, double secondsSpoken) {
        this.numberOfWords = numberOfWords;
        this.secondsSpoken = secondsSpoken;
    }

    Output(int personalPronouns, ArrayList<PronounCount> personalArray,
           int audiencePronouns, ArrayList<PronounCount> audienceArray,
           int thirdPersonPronouns, ArrayList<PronounCount> thirdPersonArray,
           String outputFile) {
        this.personalPronouns = personalPronouns;
        this.personalArray = personalArray;
        this.audiencePronouns = audiencePronouns;
        this.audienceArray = audienceArray;
        this.thirdPersonPronouns = thirdPersonPronouns;
        this.thirdPersonArray = thirdPersonArray;
        this.outputFile = outputFile;
    }

    /**
     * Prints all Pronouns in the array and the number of occurrences.
     * Prints in the form Pronoun:Count.
     */
    void printEverything() {
        String everything = "Pronount:Count";
        everything += concatArrayList(personalArray) + concatArrayList(audienceArray) + concatArrayList(thirdPersonArray);
        Logging.appendIntoFile(outputFile, everything);
    }

    /**
     * Creates String representation of PronounCount ArrayList
     * @param arrList  PronounCount ArrayList of occurrences
     * @return  String representation
     */
    private String concatArrayList(ArrayList<PronounCount> arrList) {
        String concat = "";
        for (PronounCount pronounCount : arrList) {
            concat += "\n" + pronounCount.getPronoun() + ":" + pronounCount.getCount();
        }
        return concat;
    }

}
