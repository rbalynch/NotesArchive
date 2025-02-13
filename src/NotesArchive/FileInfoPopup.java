/**
 * This is a utility dialog for the file's information.
 * It will only display necessary information for the user
 * to have, and not every attribute on the .json file.
 */

package NotesArchive;
import org.apache.lucene.document.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FileInfoPopup extends JDialog {
    static JLabel lastEdited, fileMade, fileSize, directory;
    static Path file;
    static BasicFileAttributes attr;
    static Document doc;
    static JPanel comps;
    static final JLabel TITLE = new JLabel("File Info");

    public FileInfoPopup(Document doc) throws IOException {
        FileInfoPopup.doc = doc;
        FileInfoPopup.file = Paths.get(doc.get("directory"));
        attr = Files.readAttributes(FileInfoPopup.file, BasicFileAttributes.class);
    }

    public static String convertToUTC(long l) {
        Date date = new Date(l);
        return date.toString();
    } //Converts timestamp in milliseconds to UTC for display

    public static void addComponentsToFrame(Container pane) throws IOException, ParseException {
        //Parses the .json file from the passed document
        Object o = new JSONParser().parse(new FileReader(doc.get("jsonDir")));
        JSONObject jsonObject = (JSONObject) o;

        //Creates sub-panel
        comps = new JPanel();

        //Gathers all necessary information and puts them into labels for display
        lastEdited = new JLabel("Last edited: " + convertToUTC((long) jsonObject.get("lastEdited")));
        fileSize = new JLabel("File size: " + jsonObject.get("fileSize"));
        fileMade = new JLabel("File created: " + convertToUTC((long) jsonObject.get("fileMade")));
        directory = new JLabel("File path: " + jsonObject.get("directory"));
        JLabel[] labels = {lastEdited, fileMade, fileSize, directory};
        comps.setLayout(new BoxLayout(comps, BoxLayout.PAGE_AXIS));

        TITLE.setFont(new Font("Courier New", Font.BOLD, 16));
        for (JLabel j : labels) {
            j.setFont(new Font("Courier New", Font.BOLD, 16));
            comps.add(j);
        }
        pane.add(comps);
    }
    public void createGUI() throws IOException, ParseException {
        JFrame frame = new JFrame("Info");
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
