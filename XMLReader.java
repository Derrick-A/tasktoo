import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import javax.xml.stream.*;

public class XMLReader {
    public static void main(String[] args) {
        try {
            // Open the XML file
            FileInputStream fileInputStream = new FileInputStream(new File("data.xml"));
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(fileInputStream);

            // Get user-selected fields
            List<String> selectedFields = getSelectedFields();

            // Read the XML file and extract selected fields
            Map<String, String> fieldValues = new HashMap<>();
            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = xmlStreamReader.getLocalName();
                        if (selectedFields.contains(elementName)) {
                            xmlStreamReader.next(); // Move to character event
                            String value = xmlStreamReader.getText().trim();
                            fieldValues.put(elementName, value);
                        }
                        break;
                }
            }

            // Print the selected field values
            System.out.println("Selected Field Values:");
            for (String field : selectedFields) {
                String value = fieldValues.get(field);
                System.out.println(field + ": " + value);
            }

            // Close the streams
            xmlStreamReader.close();
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getSelectedFields() {
        Scanner scanner = new Scanner(System.in);
        List<String> selectedFields = new ArrayList<>();
        System.out.println("Enter the fields you want to extract (comma-separated):");
        String input = scanner.nextLine();
        String[] fields = input.split(",");
        for (String field : fields) {
            selectedFields.add(field.trim());
        }
        scanner.close();
        return selectedFields;
    }
}
