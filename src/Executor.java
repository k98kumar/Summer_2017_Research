import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by Koushhik Kumar on 5/28/17.
 * Using Xerces DOM by Apache:
 * @see "https://xerces.apache.org/xerces2-j/faq-dom.html"
 * For Word Document instructions:
 * @see "https://www.tutorialspoint.com/apache_poi_word/apache_poi_word_quick_guide.htm"
 * To show a file in Terminal and refresh every second:
 *     while [ 1 ]; do clear; date; cat fileName; sleep 1; done
 */
public class Executor {

    private static String captions = "";
    private static ArrayList<P_Properties> propArray = new ArrayList<>();

    /**
     *
     * @param args  First Argument:  Whether the input xml is in a file or URL
     *              Second Argument: The file or URL (pick which is specified)
     *              Third Argument:  Where output should be written
     *              Fourth Argument: Location of the log file
     * @throws IOException
     * @throws org.xml.sax.SAXException
     * @throws ParseException
     * @throws ParserConfigurationException
     * @see  "https://stackoverflow.com/a/3936452/7211793"
     */
    public static void main(String[] args) throws IOException, org.xml.sax.SAXException, ParseException, ParserConfigurationException {

        // START Arguments to variables
        String fileOrURL = args[0];
        String pathName = args[1];
        String outputFile = args[2];
        String logFile = args[3];
        // END Arguments to variable

        /*Scanner input = new Scanner(System.in);
        if (new File(logFile).exists()) {
            System.out.println("File Exists. Delete this file? (Y or N)");
            if (input.nextLine().equals("Y")) Files.delete(Paths.get(logFile));
        }*/

        // START Delete file if exists
        Files.delete(Paths.get(logFile));
        // END Delete file if exists

        Logging something = new Logging(logFile);
        something.startProgram();

        // START Analyze argument
        String outputFileType = outputFile.substring(outputFile.lastIndexOf('.') + 1);
        String logFileType = logFile.substring(logFile.lastIndexOf('.') + 1);
        // END Analyze argument

        // START Create a path
        Path path = Paths.get(pathName);
        Charset charset = StandardCharsets.UTF_8;
        String fileString = new String(Files.readAllBytes(path), charset);
        fileString = fileString.replaceAll("<br/>", "").replaceAll("&apos;", "'");
        Files.write(path, fileString.getBytes(charset));
        // END Create a path

        // START Create the document
        Document doc;
        switch (fileOrURL.toLowerCase()) {
            case "file":
                if (checkForBOM(new File(pathName)))
                    doc = convertFileToDoc(circumvent(new File(pathName)));
                else
                    doc = convertFileToDoc(pathName);
                break;
            case "url":
                doc = convertURLToDoc(getStringOfURL(pathName));
                break;
            default:
                doc = null;
                break;
        }
        // END Create the document

        // START Create the file in the location specified
        propArray = extract(doc);
        createDoc(outputFile, outputFileType, propArray);
        // END Create the file in the location specified

        something.finishedParsingDoc();

        // START Analyzing Pronouns
        AnalyzePronouns AP = new AnalyzePronouns(captions, logFile);
        AP.compAP();

        // END Analyzing Pronouns

        something.endProgram();

    }

    /**
     * Returns a String representation of the XML code from local copy
     * @param pathToFile
     * @return
     * @throws IOException
     */
    private static String getStringOfFile(String pathToFile) throws IOException {

        File file = new File(pathToFile);
        StringBuilder XMLBuilder = new StringBuilder((int) file.length());
        String lineSep = System.getProperty("line.separator");
        String stringRep;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                XMLBuilder.append(fileScanner.nextLine()).append(lineSep);
            }
            stringRep = XMLBuilder.toString();
        }
        return stringRep.replaceAll("&apos;", "'").replaceAll("<br/>", "");

    }

    /**
     * Returns a String representation of the XML code from URL
     * @param urlToCode
     * @return
     * @throws IOException
     */
    private static String getStringOfURL(String urlToCode) throws IOException {

        URL someURL = new URL(urlToCode);
        StringBuilder xmlBuilder = new StringBuilder();

        BufferedReader xmlReader = new BufferedReader(new InputStreamReader(someURL.openStream()));
        String inputLine;
        while ((inputLine = xmlReader.readLine()) != null) {
            xmlBuilder.append(inputLine);
        }
        xmlReader.close();

        return xmlBuilder.toString().replaceAll("&apos;", "'").replaceAll("<br/>", "");

    }

    /**
     * BOM contains "EF BB BF"
     * Checks if bytes of file start with BOM
     * @param fileToCheck  Input file that may contain BOM
     * @return
     * @throws FileNotFoundException
     */
    private static boolean checkForBOM(File fileToCheck) throws FileNotFoundException {

        FileChannel input = new FileInputStream(fileToCheck).getChannel();
        return input.toString().startsWith("EF BB BF");

    }

    /**
     * Removes the BOM
     * BOM: the first three bytes of a file (in this case, XML file)
     * @param originalFile  File that needs BOM removed before parsing
     * @return  Path of output file without BOM
     * @throws IOException  FileInputStream throws exception
     * @see  "https://stackoverflow.com/a/9737529/7211793"
     */
    private static String circumvent(File originalFile) throws IOException {

        String outputPath = File.separator + "Users" + File.separator + "kash" + File.separator + "Desktop" + File.separator + "Overwrite_Input.xml";

        File output = new File(outputPath);

        FileChannel source = null;
        FileChannel dest = null;

        try {
            source = new FileInputStream(originalFile).getChannel();
            source.position(3);
            dest = new FileOutputStream(output).getChannel();
            dest.transferFrom( source, 0, source.size() - 3 );
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(dest != null) {
                dest.close();
            }
        }

        return outputPath;
    }

    /**
     * Converts the contents of the file to a Document
     * @param pathToFile  File path name as a String
     * @return  Document that contains lines of file
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @see  "https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/"
     */
    private static Document convertFileToDoc(String pathToFile) throws IOException, ParserConfigurationException, SAXException {

        File file = new File(pathToFile);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);

    }

    /**
     * Converts the contents of the file to a Document
     * @param urlToCode  URL path name as a String
     * @return  Document that contains lines of file
     * @throws IOException
     * @throws SAXException
     * @see  "https://stackoverflow.com/a/7902162/7211793"
     */
    private static Document convertURLToDoc(String urlToCode) throws IOException, SAXException {

        DOMParser dp = new DOMParser();
        URL someURL = new URL(urlToCode);
        dp.parse(new org.xml.sax.InputSource(someURL.openStream()));
        return dp.getDocument();
    }

    /**
     * Extracts the specified contents of XML from String
     * @param doc  String that the convert() method returns
     * @return  String of text in certain tags
     * @throws org.xml.sax.SAXException  parse() method throws exception
     * @throws IOException  extract() method throws exception
     * @see  "https://stackoverflow.com/a/7902162"
     */
    private static ArrayList<P_Properties> extract(Document doc) throws org.xml.sax.SAXException, IOException, ParseException {

        ArrayList<P_Properties> arrayList = new ArrayList<>();

        NodeList nl = doc.getElementsByTagName("p");
        for (int i = 0; i < nl.getLength(); i++) {

            Node node = nl.item(i);
            NamedNodeMap nnm = node.getAttributes();

            String begin = nnm.getNamedItem("begin").getTextContent();

            String end = nnm.getNamedItem("end").getTextContent();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("CST")); // No Offset
            Date dateBegin = sdf.parse(begin);
            Date dateEnd = sdf.parse(end);
            long difference = dateEnd.getTime() - dateBegin.getTime();
            Date date = new Date(difference - 64800000);
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
            String dateFormatted = formatter.format(date);

            String region = nnm.getNamedItem("region").getTextContent();

            String text;
            Node desc = node.getFirstChild();
            if (desc != null) {
                text = desc.getNodeValue();
                captions += text + " ";
            } else {
                text = "";
            }

            arrayList.add(new P_Properties(begin, end, dateFormatted, region, text));

        }

        return arrayList;

    }

    /**
     * Uses toStringCSV() to convert the Strings to comma-separated values
     * Uses toString() to convert the Strings to string for text files
     * @param arrayOfObjects  ArrayList of P_Properties objects created from XML
     * @return  ArrayList of Strings outputted from changeToString()
     */
    private static ArrayList<String> changeArray(ArrayList<P_Properties> arrayOfObjects, String fileType) {
        ArrayList<String> concat = new ArrayList<>();
        switch (fileType.toLowerCase()) {
            case "csv" :
                for (P_Properties p : arrayOfObjects) {
                    concat.add(P_Properties.toStringCSV(p));
                }
                break;
            default:
                for (P_Properties p : arrayOfObjects) {
                    concat.add(P_Properties.toString(p));
                }
                break;
        }

        return concat;
    }

    /**
     *
     * @param arrayForFile  ArrayList of P_Properties objects
     * @see  "https://stackoverflow.com/a/2885224/7211793"
     */
    private static void createDoc(String outputPath, String fileType, ArrayList<P_Properties> arrayForFile) {

        String lineSep = System.getProperty("line.separator");
        // Path file = Paths.get(File.separator + "Users" + File.separator + "kash" + File.separator + "Desktop" + File.separator + "Testing.txt");
        Path file = Paths.get(outputPath);
        try {
            ArrayList<String> captionList = changeArray(arrayForFile, fileType);
            // captionList.add(0, captions + lineSep + lineSep);
            if (fileType.equals("docx") || fileType.equals("doc")) {

            } else {
                Files.write(file, captionList, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
    }

}
