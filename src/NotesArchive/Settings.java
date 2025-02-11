package NotesArchive;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

public class Settings extends JDialog {
    static JPanel comps, factoryReset, autoIndexTime, lockdown;
    static JButton frButton;
    static JTextField ldField;
    static JComboBox<String> auDropdown;
    static JLabel frLabel, auLabel, ldLabel, frTitle, auTitle, ldTitle;

    public Settings() {

    }

    public static void addComponentsToFrame(Container pane) {
        comps = new JPanel(new GridLayout(0, 1));

        factoryReset = new JPanel();
        frButton = new JButton("Reset");
        frLabel = new JLabel("Reset the index, deleting all stored information on all text files");
        frTitle = new JLabel("Factory Reset");
        factoryReset.add(frTitle, BorderLayout.LINE_START);
        factoryReset.add(frLabel, BorderLayout.CENTER);
        factoryReset.add(frButton, BorderLayout.LINE_END);

        autoIndexTime = new JPanel();
        auDropdown = new JComboBox<>(new String[]{"30 minutes", "60 minutes", "2 hours", "6 hours", "12 hours", "24 hours"});
        auLabel = new JLabel("Set the program to automatically re-index all stored files to every...");
        auTitle = new JLabel("Auto-Index Time");
        autoIndexTime.add(auTitle, BorderLayout.LINE_START);
        autoIndexTime.add(auLabel, BorderLayout.CENTER);
        autoIndexTime.add(auDropdown, BorderLayout.LINE_END);

        lockdown = new JPanel();
        ldField = new JTextField(10);
        ldLabel = new JLabel("Set a password to your personal index.");
        ldTitle = new JLabel("Lockdown");
        lockdown.add(ldTitle, BorderLayout.LINE_START);
        lockdown.add(ldLabel, BorderLayout.CENTER);
        lockdown.add(ldField, BorderLayout.LINE_END);

        comps.add(factoryReset);
        comps.add(autoIndexTime);
        comps.add(lockdown);
        pane.add(comps);
    }

    public static void createGUI() {
        JFrame frame = new JFrame("Test");
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        createGUI();
    }
}
