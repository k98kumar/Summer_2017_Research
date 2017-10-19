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
import java.nio.file.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/*
 * Copyright 2017 Koushhik Kumar. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private static ArrayList<CaptionProp> propArray = new ArrayList<>();
    private static final String TXT = ".txt";
    private static final String TSV = ".tsv";
    private static final String _OUTPUT = "_output";
    private static final String _ORGANIZED = "_organized";

    /**
     * @param args  First Argument:  Whether the input xml is in a file, folder, or URL
     *              Second Argument: The path of the file, folder, or URL
     * @throws IOException                   IOException
     * @throws org.xml.sax.SAXException      SAXException
     * @throws ParseException                ParseException
     * @throws ParserConfigurationException  ParserConfigurationException
     * @see  "https://stackoverflow.com/a/3936452/7211793"
     */
    public static void main(String[] args) throws IOException, org.xml.sax.SAXException, ParseException, ParserConfigurationException {

        // START Arguments to variables
        String fileFolderURL = args[0];
        String path = args[1];
        String listOrSort;
        try {
            listOrSort = args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            listOrSort = "list";
        }
        // END Arguments to variable

        String organizedFile, outputFile, logFile, summaryTSV;
        Path source, output;
        Summarizer summarizer = new Summarizer();

        if (!listOrSort.toLowerCase().equals("list") && !listOrSort.toLowerCase().equals("sort")) listOrSort = "list";

        switch (fileFolderURL.toLowerCase()) {
            case "file" :
                logFile = path.substring(0, path.lastIndexOf('.')) + "_logger" + TXT;
                organizedFile = createOrganizedFilePath(path, true);
                outputFile = createOutputFilePath(path, true, TXT);
                fileActionIndividual(path, organizedFile, logFile, outputFile);
                break;
            case "folder" :
                File folderPath = new File(path);
                for (File fileFolder : folderPath.listFiles()) {
                    try {
                        if (fileFolder.isDirectory()) Files.delete(Paths.get(fileFolder.getPath()));
                    } catch (DirectoryNotEmptyException e) {
                        for (File fileInFolder : fileFolder.listFiles()) {
                            fileInFolder.delete();
                        }
                    }
                }
                for (File allOthers : folderPath.listFiles()) {
                    if (!allOthers.getPath().endsWith(".xml")) {
                        Files.delete(Paths.get(allOthers.getPath()));
                    }
                }

                logFile = (path.endsWith("/")) ? path + "logs" + TXT : path + "/logs" + TXT;
                if (new File(logFile).exists()) Files.delete(Paths.get(logFile));
                summaryTSV = (path.endsWith("/")) ? path + "Summary" + TSV : path + "/Summary" + TSV;
                summarizer.summarizerFile = summaryTSV;
                outputInListOrSort(path, folderPath, logFile, listOrSort, summarizer);
                break;
            case "url" :
                fileActionURL();
                break;
            default :
                break;
        }

    }

    private static void outputInListOrSort(String path, File pathOfFolder, String logFile, String listOrSort, Summarizer summarizer)
            throws SAXException, ParserConfigurationException, ParseException, IOException {
        String fileToString, outputFile, organizedFile;
        switch (listOrSort) {
            case "list" :
                String outputFolder = makeOutputDirectory(path);
                String organizedFolder = makeOrganizedDirectory(path);
                for (File fileEntry : pathOfFolder.listFiles()) {
                    if (fileEntry.isDirectory()) continue;
                    fileToString = fileEntry.getPath();
                    outputFile = outputFolder + getFileName(fileToString) + TSV;
                    organizedFile = organizedFolder + getFileName(fileToString) + TXT;
                    fileActionFolder(fileToString, organizedFile, logFile, outputFile, summarizer);
                }
                break;
            case "sort" :
                for (File fileEntry : pathOfFolder.listFiles()) {
                    if (fileEntry.isDirectory()) continue;
                    fileToString = fileEntry.getPath();
                    makeDirectoryAtParent(fileToString);
                    organizedFile = createOrganizedFilePath(fileToString, false); // Creates filepath in new folder
                    outputFile = createOutputFilePath(fileToString, false, TSV);
                    fileActionFolder(fileToString, organizedFile, logFile, outputFile, summarizer);

                    // + ---------------- Keep this commented ---------------- +
                    // |           source = Paths.get(fileToString);           |
                    // |  output = Paths.get(getMovedFilePath(fileToString));  |
                    // |     Files.move(source, output, REPLACE_EXISTING);     |
                    // + ---------------- Keep this commented ---------------- +
                }
                break;
            default:
                break;
        }
    }

    /**
     * Used when an folder is passed in as an argument
     * @param pathName  File path of the XML file
     * @param organizedFile  File path of organized text file
     * @param logFile File path of log text file
     * @param outputFile  File path of output text file
     * @throws IOException                   IOException
     * @throws ParseException                ParseException
     * @throws SAXException                  SAXException
     * @throws ParserConfigurationException  ParserConfigurationException
     */
    private static void fileActionFolder(String pathName, String organizedFile, String logFile, String outputFile, Summarizer summarizer)
            throws IOException, ParseException, SAXException, ParserConfigurationException {

        Logging programLogs = new Logging(logFile);
        programLogs.startProgram();

        // START Analyze argument
        String organizedFileType = organizedFile.substring(organizedFile.lastIndexOf('.') + 1);
        String logFileType = logFile.substring(logFile.lastIndexOf('.') + 1);
        // END Analyze argument

        // START Create a path
        Path path = Paths.get(pathName);
        Charset charset = StandardCharsets.UTF_8;
        String fileString = new String(Files.readAllBytes(path), charset);
        fileString = fileString.replaceAll("<br/>", "").replaceAll("&apos;", "'").replaceAll("[^\\x20-\\x7e]", "");
        Files.write(path, fileString.getBytes(charset));
        // END Create a path

        // START Create the document
        Document doc;
        if (checkForBOM(new File(pathName))) doc = convertFileToDoc(circumvent(new File(pathName)));
        else doc = convertFileToDoc(pathName);
        // END Create the document

        // START Create the file in the location specified
        propArray = extract(doc);
        createDoc(organizedFile, organizedFileType, propArray);
        // END Create the file in the location specified

        // START Doc parse end log
        programLogs.finishedParsingDoc();
        // END Doc parse end log

        // START Speech Rate
        SpeechRate SR = new SpeechRate(propArray, logFile);
        // END Speech Rate

        // START Analyzing Pronouns
        AnalyzePronouns AP = new AnalyzePronouns(captions, logFile, outputFile);
        AP.compAP();
        // END Analyzing Pronouns

        // START Program end
        programLogs.endProgram();
        // END Program end

        captions = "";
    }

    /**
     * Used when an individual XML file is passed in as an argument
     * @param pathName  File path of the XML file
     * @param organizedFile  File path of organized text file
     * @param logFile File path of log text file
     * @param outputFile  File path of output text file
     * @throws IOException                   IOException
     * @throws ParseException                ParseException
     * @throws SAXException                  SAXException
     * @throws ParserConfigurationException  ParserConfigurationException
     */
    private static void fileActionIndividual(String pathName, String organizedFile, String logFile, String outputFile)
            throws IOException, ParseException, SAXException, ParserConfigurationException {

        // START Delete file if exists
        if (new File(logFile).exists()) Files.delete(Paths.get(logFile));
        // END Delete file if exists

        Logging programLogs = new Logging(logFile);
        programLogs.startProgram();

        // START Analyze argument
        String organizedFileType = organizedFile.substring(organizedFile.lastIndexOf('.') + 1);
        // END Analyze argument

        // START Create a path
        Path path = Paths.get(pathName);
        Charset charset = StandardCharsets.UTF_8;
        String fileString = new String(Files.readAllBytes(path), charset);
        fileString = fileString.replaceAll("<br/>", "").replaceAll("&apos;", "'").replaceAll("[^\\x20-\\x7e]", "");
        Files.write(path, fileString.getBytes(charset));
        // END Create a path

        // START Create the document
        Document doc;
        if (checkForBOM(new File(pathName)))
            doc = convertFileToDoc(circumvent(new File(pathName)));
        else
            doc = convertFileToDoc(pathName);
        // END Create the document

        // START Create the file in the location specified
        propArray = extract(doc);
        createDoc(organizedFile, organizedFileType, propArray);
        // END Create the file in the location specified

        // START Doc parse end log
        programLogs.finishedParsingDoc();
        // END Doc parse end log

        // START Speech Rate
        SpeechRate SR = new SpeechRate(propArray, logFile);
        // END Speech Rate

        // START Analyzing Pronouns
        AnalyzePronouns AP = new AnalyzePronouns(captions, logFile, outputFile);
        AP.compAP();
        // END Analyzing Pronouns

        // START Program end
        programLogs.endProgram();
        // END Program end
    }

    private static void fileActionURL() {
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
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(file);

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

        DOMParser domParser = new DOMParser();
        URL someURL = new URL(urlToCode);
        domParser.parse(new org.xml.sax.InputSource(someURL.openStream()));
        return domParser.getDocument();
    }

    /**
     * Extracts the specified contents of XML from String
     * @param doc  String that the convert() method returns
     * @return  String of text in certain tags
     * @throws org.xml.sax.SAXException  parse() method throws exception
     * @throws IOException  extract() method throws exception
     * @throws ParseException  returnDateDifference() throws exception
     * @see  "https://stackoverflow.com/a/7902162"
     */
    private static ArrayList<CaptionProp> extract(Document doc) throws org.xml.sax.SAXException, IOException, ParseException {

        ArrayList<CaptionProp> arrayList = new ArrayList<>();

        NodeList nl = doc.getElementsByTagName("p");
        for (int i = 0; i < nl.getLength(); i++) {

            Node node = nl.item(i);
            NamedNodeMap namedNodeMap = node.getAttributes();

            String begin = namedNodeMap.getNamedItem("begin").getTextContent();
            String end = namedNodeMap.getNamedItem("end").getTextContent();
            String dateFormatted = returnDateDifference("HH:mm:ss.SSS", begin, end);
            String region = "unspecified";
            String text;

            if (checkAttributeExists(namedNodeMap, "region")) region = namedNodeMap.getNamedItem("region").getTextContent();;

            Node desc = node.getFirstChild();
            if (desc != null) {
                text = desc.getNodeValue();
                captions += text + " ";
            } else {
                text = "";
            }

            arrayList.add(new CaptionProp(begin, end, dateFormatted, region, text));

        }

        return arrayList;

    }

    /**
     * Checks if attribute in a tag of a given name exits
     * @param nodeMap  NamedNodeMap of single "p" tag
     * @param name  Name of attribute
     * @return  Boolean indicating whether specified attribute exists
     */
    private static boolean checkAttributeExists(NamedNodeMap nodeMap, String name) {
        int length = nodeMap.getLength();
        for (int i = 0; i < length; i++) {
            Node n = nodeMap.item(i);
                if (name.equals(n.getNodeName())) return true;
        }
        return false;
    }

    /**
     * Finds the difference in times
     * @param sdfPattern  The pattern SimpleDateFormat requires
     * @param begin  String representation of start time in SDF format
     * @param end String representation of end time in SDF format
     * @return String representation of time difference in SDF format
     * @throws ParseException exception that SimpleDateFormat.parse() throws
     */
    private static String returnDateDifference(String sdfPattern, String begin, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
        sdf.setTimeZone(TimeZone.getTimeZone("CST")); // No Offset
        Date dateBegin = sdf.parse(begin);
        Date dateEnd = sdf.parse(end);
        long difference = dateEnd.getTime() - dateBegin.getTime();
        Date date = new Date(difference - 64800000);
        DateFormat formatter = new SimpleDateFormat(sdfPattern);
        return formatter.format(date);
    }

    /**
     * Uses toString() to convert the Strings to string for text files
     * @param arrayOfObjects  ArrayList of CaptionProp objects created from XML
     * @return  ArrayList of Strings outputted from changeToString()
     */
    private static ArrayList<String> changeArray(ArrayList<CaptionProp> arrayOfObjects, String fileType) {
        ArrayList<String> concat = new ArrayList<>();
            for (CaptionProp c : arrayOfObjects) {
                concat.add(CaptionProp.toString(c));
            }
        return concat;
    }

    /**
     * @param arrayForFile  ArrayList of CaptionProp objects
     * @see  "https://stackoverflow.com/a/2885224/7211793"
     */
    private static void createDoc(String outputPath, String fileType, ArrayList<CaptionProp> arrayForFile) {

        Path file = Paths.get(outputPath);
        try {
            ArrayList<String> captionList = changeArray(arrayForFile, fileType);
            // captionList.add(0, captions + lineSep + lineSep);
            Files.write(file, captionList, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
    }

    private static String getFileName(String file) {
        return file.substring(file.lastIndexOf('/') + 1, file.lastIndexOf('.'));
    }

    /**
     * Used when creating directories for each XML file and its organized and output files.
     * @param file  File path of XML file
     * @param isIndividual  False if folder is passed in as argument
     *                      True if individual XML file is passed in as argument
     * @return  Name of new organized file
     */
    private static String createOrganizedFilePath(String file, boolean isIndividual) {
        if (isIndividual) return file.substring(0, file.lastIndexOf('.')) + _ORGANIZED + TXT;
        return file.substring(0, file.lastIndexOf('/') + 1) + getFileName(file) + "/" + getFileName(file) + _ORGANIZED + TXT;
    }

    /**
     * Used when creating directories for each XML file and its organized and output files.
     * @param file  File path of XML file
     * @param isIndividual  False if folder is passed in as argument
     *                      True if individual XML file is passed in as argument
     * @return  Name of new output file
     */
    private static String createOutputFilePath(String file, boolean isIndividual, String fileExtension) {
        if (isIndividual) return file.substring(0, file.lastIndexOf('.')) + _OUTPUT + fileExtension;
        return file.substring(0, file.lastIndexOf('/') + 1) + getFileName(file) + "/" + getFileName(file) + _OUTPUT + fileExtension;
    }

    /**
     * Make a directory corresponding to each XML file name
     * @param file  File path of an XML file
     */
    private static void makeDirectoryAtParent(String file) {
        new File(file.substring(0, file.lastIndexOf('/') + 1) + getFileName(file) + "/").mkdir();
    }

    /**
     * Used only when files are getting moved to other directories
     * @param file  Current file path
     * @return  String of new file path
     */
    private static String getMovedFilePath(String file) {
        return file.substring(0, file.lastIndexOf('/') + 1) + getFileName(file) + "/" + file.substring(file.lastIndexOf('/') + 1);
    }

    /**
     * Makes output directory for list viewing
     * @param folder  Directory in which output directory is in currently
     * @return  New directory file path
     */
    private static String makeOutputDirectory(String folder) {
        String newFolder = folder.endsWith("/") ? folder + "Output/" : folder + "/Output/";
        /*
        // Used when String fileExtension is a parameter
        switch (fileExtension) {
            case TXT :
                newFolder = folder.endsWith("/") ? folder + "Output/" : folder + "/Output/";
                break;
            case TSV :
                newFolder = folder.endsWith("/") ? folder + "OutputTSV/" : folder + "/OutputTSV/";
                break;
            default :
                newFolder = folder.endsWith("/") ? folder + "OutputOther/" : folder + "/OutputOther/";
                break;
        }
        */
        File dir = new File(newFolder);
        boolean success = dir.mkdir();
        if (success) System.out.println("Successful");
        return newFolder;
    }

    /**
     * Makes organized directory for list viewing
     * @param folder  Directory in which organized directory is in currently
     * @return  New directory file path
     */
    private static String makeOrganizedDirectory(String folder) {
        String newFolder = folder.endsWith("/") ? folder + "Organized/" : folder + "/Organized/";
        File dir = new File(newFolder);
        boolean success = dir.mkdir();
        if (success) System.out.println("Successful");
        return newFolder;
    }

    /*
    private static void storeCodeForUse() {
        // Scanner input = new Scanner(System.in);
        // if (new File(logFile).exists()) {
        //     System.out.println("File Exists. Delete this file? (Y or N)");
        //     if (input.nextLine().equals("Y")) Files.delete(Paths.get(logFile));
        // }

        // START Delete file if exists
        Files.delete(Paths.get(logFile));
        // END Delete file if exists

        Logging programLogs = new Logging(logFile);
        programLogs.startProgram();

        // START Analyze argument
        String organizedFileType = organizedFile.substring(organizedFile.lastIndexOf('.') + 1);
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
        switch (fileFolderURL.toLowerCase()) {
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
        createDoc(organizedFile, organizedFileType, propArray);
        // END Create the file in the location specified

        // START Doc parse end log
        programLogs.finishedParsingDoc();
        // END Doc parse end log

        // START Speech Rate
        SpeechRate SR = new SpeechRate(propArray, logFile);
        // END Speech Rate

        // START Analyzing Pronouns
        AnalyzePronouns AP = new AnalyzePronouns(captions, logFile);
        AP.compAP();
        // END Analyzing Pronouns

        // START Program end
        programLogs.endProgram();
        // END Program end
    }
    */

}
