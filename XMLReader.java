import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLReader {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("data.xml"));
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(fileInputStream);

            List<String> dataList = new ArrayList<>();
            String data = "";
            while (xmlStreamReader.hasNext()) {
                int event = xmlStreamReader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        System.out.println("Start Element: " + xmlStreamReader.getLocalName());
                        if (xmlStreamReader.getLocalName().equals("data")) {
                            data = "";
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        System.out.println("Characters: " + xmlStreamReader.getText());
                        data += xmlStreamReader.getText().trim();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        System.out.println("End Element: " + xmlStreamReader.getLocalName());
                        if (xmlStreamReader.getLocalName().equals("data")) {
                            dataList.add(data);
                        }
                        break;
                }
            }

            // Print the data read from XML
            for (String item : dataList) {
                System.out.println(item);
            }

            // Close the streams
            xmlStreamReader.close();
            // fileInputStream.close();
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
