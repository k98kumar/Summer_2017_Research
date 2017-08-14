/**
 * Created by kash on 8/12/17.
 * Will be used for final file with cumulative amounts.
 * Will be used in Output.java as global variable. Keep on concatenating
 * to the variable in this file.
 */
public class Summarizer {

    String concatOutputDataTSV;
    String summarizerFile;

    Summarizer() {
    }

    Summarizer(String summarizerFile, String concatOutputDataTSV) {
        this.summarizerFile = summarizerFile;
        this.concatOutputDataTSV = concatOutputDataTSV;
    }

    private void printConcatData() {
        Logging.appendIntoFile(summarizerFile, concatOutputDataTSV);
    }

}
