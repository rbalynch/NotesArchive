package NotesArchive;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.SwingUtilities.invokeLater;

public class AppWindow extends JFrame {
    static String window;
    static NotesArchive notes;
    static JTextField text;
    static JComboBox dropdown;
    static JButton search, addToIndex, settings;
    static FlowLayout layout;
    static JFileChooser fc;
    static JPanel comps, searchButtons, oButton, allButtons;
    static JLabel label;

    static {
        try {
            notes = new NotesArchive(); //Creates new instance of archive
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    //CONSTRUCTOR
    public AppWindow(String s) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        window = s;
        notes.refreshJSONS();
        invokeLater(() -> {
            try {
                createGUI();
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //GUI BUILDING
    public static void addComponentsToFrame(Container pane) {
        //MAKE COMPONENTS
        comps = new JPanel();
        layout = new FlowLayout();
        searchButtons = new JPanel();
        oButton = new JPanel();
        allButtons = new JPanel();
        search = new JButton("Search");
        addToIndex = new JButton("Index");
        text = new JTextField(20);
        fc = new JFileChooser();
        label = new JLabel("Search by: ");

        String[] arr = {"File Text", "File Name", "Serial"};
        dropdown = new JComboBox(arr);

        ImageIcon icon = new ImageIcon("C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\images\\Windows_Settings_app_icon.png");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        settings = new JButton(icon);

        searchButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
        oButton.setLayout(new FlowLayout(FlowLayout.TRAILING));
        oButton.add(settings);

        search.setDefaultCapable(true);

        fc.setMultiSelectionEnabled(true);

        comps.setLayout(layout);//Sets layout for components

        search.addActionListener(_ -> {
            String s = text.getText();
            try {
                notes.iw.close();
                ArrayList<Document> list = notes.search(s, dropdown.getSelectedIndex());
                Results r = new Results(list);
                r.next.addActionListener(_ -> {

                });
            }
            catch (IOException | org.apache.lucene.queryparser.classic.ParseException i) {
                throw new RuntimeException(i);
            }
        }); //SEARCH ACTION
        addToIndex.addActionListener(_ -> {
            try {
                File[] files;
                int r = fc.showSaveDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    files = fc.getSelectedFiles();
                }
                else {
                    return;
                }
                for (File f : files) {
                    String directory = "C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\jsons\\" + notes.getFileWithoutExtension(f) + ".json";
                    if (new File(directory).isFile()) {
                        Object o = new JSONParser().parse(new FileReader(directory));
                        JSONObject jsonObject = (JSONObject) o;
                        String dir = (String) jsonObject.get("directory");

                        ReplacePopup rp = new ReplacePopup();
                        rp.confirm.addActionListener(_ -> {
                            try {
                                notes.iw.deleteDocuments(new Term("directory", dir));
                                notes.createJSON(f);
                                notes.addDoc(notes.iw, directory);
                            } catch (IOException | org.apache.lucene.queryparser.classic.ParseException |
                                     ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                            rp.frame.dispose();
                        });
                        rp.cancel.addActionListener(_ -> rp.frame.dispose());
                    }
                    else {
                        notes.createJSON(f);
                        notes.addDoc(notes.iw, directory);
                    }
                }
            }
            catch (IOException | ParseException | org.apache.lucene.queryparser.classic.ParseException ex) {
                throw new RuntimeException(ex);
            }

        }); //INDEX ACTION

        //COMPONENT ADDS
        comps.add(label);
        comps.add(dropdown);
        comps.add(text);
        searchButtons.add(search);
        searchButtons.add(addToIndex);

        fc.setDialogType(JFileChooser.APPROVE_OPTION); //File chooser filter and config
        FileFilter filter = new FileNameExtensionFilter(".txt Files", "txt");
        fc.setFileFilter(filter);

        pane.add(comps, BorderLayout.PAGE_START);
        pane.add(searchButtons, BorderLayout.LINE_START);
        pane.add(oButton, BorderLayout.AFTER_LINE_ENDS);
    }
    public static void createGUI() throws IOException, ParseException {
        JFrame frame = new JFrame(window);
        addComponentsToFrame(frame.getContentPane());

        JRootPane pane = SwingUtilities.getRootPane(search);
        pane.setDefaultButton(search);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    notes.cleanJSONFolder();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}