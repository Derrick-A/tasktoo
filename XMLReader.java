import org.json.JSONObject; // Importing the JSONObject class from the org.json package
import org.xml.sax.Attributes; // Importing the Attributes interface from the org.xml.sax package
import org.xml.sax.SAXException; // Importing the SAXException class from the org.xml.sax package
import org.xml.sax.helpers.DefaultHandler; // Importing the DefaultHandler class from the org.xml.sax.helpers package

import javax.xml.parsers.SAXParser; // Importing the SAXParser interface from the javax.xml.parsers package
import javax.xml.parsers.SAXParserFactory; // Importing the SAXParserFactory class from the javax.xml.parsers package
import java.io.File; // Importing the File class from the java.io package
import java.io.FileInputStream; // Importing the FileInputStream class from the java.io package
import java.io.FileNotFoundException; // Importing the FileNotFoundException class from the java.io package
import java.io.IOException; // Importing the IOException class from the java.io package
import java.util.ArrayList; // Importing the ArrayList class from the java.util package
import java.util.List; // Importing the List interface from the java.util package
import java.util.Scanner; // Importing the Scanner class from the java.util package

// Class declaration for the XMLReader
public class XMLReader {
    // Main method
    public static void main(String[] args) {
        try {
            // Open the XML file
            FileInputStream fileInputStream = new FileInputStream(new File("data.xml")); // Creating a FileInputStream
                                                                                         // object with the specified
                                                                                         // file name

            // Get user-selected fields
            List<String> selectedFields = getSelectedFields(); // Invoking the getSelectedFields method to retrieve
                                                               // user-selected fields

            // Create SAX parser
            SAXParserFactory factory = SAXParserFactory.newInstance(); // Getting a new instance of the SAXParserFactory
            SAXParser saxParser = factory.newSAXParser(); // Creating a new SAXParser object

            // Handler to process XML elements
            XMLHandler handler = new XMLHandler(selectedFields); // Creating a new XMLHandler object with the
                                                                 // user-selected fields

            // Parse XML file using the handler
            saxParser.parse(fileInputStream, handler); // Parsing the XML file using the SAX parser and the handler

            // Get the field values from the handler
            JSONObject jsonObject = handler.getFieldValues(); // Getting the field values as a JSONObject from the
                                                              // handler

            // Print the selected field values in JSON format
            System.out.println("Selected Field Values (JSON):");
            System.out.println(jsonObject.toString(4)); // Printing the JSON string with an indentation of 4 spaces

            // Close the input stream
            fileInputStream.close(); // Closing the FileInputStream
        } catch (FileNotFoundException e) { // Handling FileNotFoundException
            System.err.println("File not found: " + e.getMessage()); // Printing an error message
        } catch (Exception e) { // Handling other exceptions
            e.printStackTrace(); // Printing the stack trace of the exception
        }
    }

    // Method to get user-selected fields
    private static List<String> getSelectedFields() {
        Scanner scanner = new Scanner(System.in); // Creating a new Scanner object to read user input
        List<String> selectedFields = new ArrayList<>(); // Creating a new ArrayList to store selected fields
        System.out.println("Enter the fields you want to extract (comma-separated):"); // Prompting the user to enter
                                                                                       // selected fields
        String input = scanner.nextLine(); // Reading the user input
        String[] fields = input.split(","); // Splitting the input string by comma
        for (String field : fields) { // Iterating over the fields
            String trimmedField = field.trim(); // Trimming whitespace from the field
            if (!trimmedField.isEmpty()) { // Checking if the trimmed field is not empty
                selectedFields.add(trimmedField); // Adding the trimmed field to the list of selected fields
            }
        }
        scanner.close(); // Closing the scanner
        return selectedFields; // Returning the list of selected fields
    }

    // Handler class to process XML elements
    static class XMLHandler extends DefaultHandler {
        private JSONObject jsonObject = new JSONObject(); // Creating a JSONObject to store field values
        private List<String> selectedFields; // List to store user-selected fields
        private String currentElement; // String to store the current XML element
        private StringBuilder currentValue; // StringBuilder to accumulate character data

        // Constructor to initialize the handler with selected fields
        public XMLHandler(List<String> selectedFields) {
            this.selectedFields = selectedFields; // Assigning the selected fields to the instance variable
        }

        // Method called at the start of an XML element
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            currentElement = qName; // Assigning the current XML element
            currentValue = new StringBuilder(); // Initializing the StringBuilder for character data
        }

        // Method called when character data is encountered
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            currentValue.append(ch, start, length); // Appending character data to the StringBuilder
        }

        // Method called at the end of an XML element
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (selectedFields.contains(qName)) { // Checking if the element is a selected field
                jsonObject.put(qName, currentValue.toString().trim()); // Adding the field and its value to the
                                                                       // JSONObject
            }
        }

        // Method to get the field values stored in the JSONObject
        public JSONObject getFieldValues() {
            return jsonObject; // Returning the JSONObject containing field values
        }
    }
}
