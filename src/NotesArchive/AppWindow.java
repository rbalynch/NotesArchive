/**
 * This class is the outer-most layer in this giant mess of a project.
 * This is what gets called to open the index. It creates the user-interface
 * and, upon request, creates new dialogs (settings, search results, etc.).
 * It also creates the action listeners for the settings menu to give it
 * access to the index, which is especially important for factory resets.
 */


package NotesArchive;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.SchedulerException;

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
    static JButton search, addToIndex, settingsButton;
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
        //Sub-panels
        comps = new JPanel();
        allButtons = new JPanel();
        searchButtons = new JPanel();
        oButton = new JPanel();

        //UI Elements
        search = new JButton("Search");
        addToIndex = new JButton("Index");
        text = new JTextField(20);
        fc = new JFileChooser();
        label = new JLabel("Search by: ");

        //Search by dropdown
        String[] arr = {"File Text", "File Name", "Serial"};
        dropdown = new JComboBox(arr);

        //Creates new settings button and gives it a gear icon
        ImageIcon icon = new ImageIcon("C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\images\\Windows_Settings_app_icon.png");
        Image img = icon.getImage();
        img = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        settingsButton = new JButton(icon);

        //Assigns layout managers
        searchButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
        oButton.setLayout(new FlowLayout(FlowLayout.TRAILING));
        comps.setLayout(new FlowLayout());

        //Assigns action listeners
        search.addActionListener(_ -> {
            String s = text.getText();
            try {
                notes.iw.close();
                ArrayList<Document> list = notes.search(s, dropdown.getSelectedIndex());
                new Results(list);
                 Results.next.addActionListener(_ -> {

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
                boolean success = true;
                for (File f : files) {
                    String directory = "C:\\Users\\rbaly\\IdeaProjects\\NotesArchive_3\\jsons\\" + NotesArchive.getFileWithoutExtension(f) + ".json";
                    if (new File(directory).isFile()) {
                        Object o = new JSONParser().parse(new FileReader(directory));
                        JSONObject jsonObject = (JSONObject) o;
                        String dir = (String) jsonObject.get("directory");

                        new ReplacePopup();
                        ReplacePopup.confirm.addActionListener(_ -> {
                            try {
                                notes.iw.deleteDocuments(new Term("directory", dir));
                                NotesArchive.createJSON(f);
                                notes.addDoc(notes.iw, directory);
                            } catch (IOException | org.apache.lucene.queryparser.classic.ParseException |
                                     ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                            ReplacePopup.frame.dispose();
                        });
                        ReplacePopup.cancel.addActionListener(_ -> ReplacePopup.frame.dispose());
                    }
                    else {
                        NotesArchive.createJSON(f);
                        notes.addDoc(notes.iw, directory);
                        if (!new File(directory).isFile()) {
                            success = false;
                        }
                    }
                }
                if (success) {
                    succuessPopup();
                }
            }
            catch (IOException | ParseException | org.apache.lucene.queryparser.classic.ParseException ex) {
                throw new RuntimeException(ex);
            }

        }); //INDEX ACTION
        settingsButton.addActionListener(_ -> {
            new Settings();
            Settings.frButton.addActionListener(_ -> {
                new ResetPopup();
                ResetPopup.yes.addActionListener(_ -> {
                    try {

                        notes.factoryReset();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    ResetPopup.frame.dispose();
                });
                ResetPopup.no.addActionListener(_ -> {
                    ResetPopup.frame.dispose();
                });
            });
            Settings.confirm.addActionListener(_ -> {
                try {
                    IndexJob job = switch (Settings.auDropdown.getSelectedIndex()) {
                        case 1 -> new IndexJob(notes, 1800);
                        case 2 -> new IndexJob(notes, 3600);
                        case 3 -> new IndexJob(notes, 7200);
                        case 4 -> new IndexJob(notes, 21600);
                        case 5 -> new IndexJob(notes, 43200);
                        case 6 -> new IndexJob(notes, 86400);
                        default -> null;
                    };
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }

                Settings.frame.dispose();
            }); //CONFIRM SETTINGS ACTION
            Settings.cancel.addActionListener(_ -> {
                Settings.frame.dispose();
            }); //CANCEL ACTION
        }); //SETTINGS ACTION

        //Adds components to appropriate panels
        comps.add(label);
        comps.add(dropdown);
        comps.add(text);
        searchButtons.add(search);
        searchButtons.add(addToIndex);
        oButton.add(settingsButton);

        //Sets configuration for file chooser and search button
        fc.setDialogType(JFileChooser.APPROVE_OPTION); //File chooser filter and config
        FileFilter filter = new FileNameExtensionFilter(".txt Files", "txt");
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(true); //Allows for multiple files to be selected
        search.setDefaultCapable(true); //Allows search button to be used as default button (activated by "Enter" key)

        //Adds and aligns panels on frame
        pane.add(comps, BorderLayout.PAGE_START);
        pane.add(searchButtons, BorderLayout.LINE_START);
        pane.add(oButton, BorderLayout.AFTER_LINE_ENDS);
    }
    public static void succuessPopup() {
        JLabel label = new JLabel("Success!");
        JButton button = new JButton("Close");
        JFrame frame = new JFrame();
        JPanel comps = new JPanel(new FlowLayout());
        button.addActionListener(_ -> {
            frame.dispose();
        });

        comps.add(label, BorderLayout.NORTH);
        comps.add(button, BorderLayout.SOUTH);
        frame.getContentPane().add(comps);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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
            public void windowClosed(WindowEvent e) {
                try {
                    notes.cleanJSONFolder();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    } //Creates app GUI
}