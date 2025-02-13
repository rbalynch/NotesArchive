/**
 * This is the class for the settings menu. Each setting has its own panel,
 * description label, and user-interaction field (button, text field, etc.).
 * All panels will go into a master "comps" panel.
 *
 * Action listeners are NOT made in this class. Each user-interaction field
 * is assigned its appropriate action listener after AppWindow.java creates a
 * Settings object.
 */

package NotesArchive;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class Settings extends JDialog {
    static JPanel comps, factoryReset, autoIndexTime, lockdown, buttons;
    static JButton frButton, confirm, cancel;
    static JTextField ldField;
    static JComboBox<String> auDropdown;
    static JLabel frLabel, auLabel, ldLabel, frTitle, auTitle, ldTitle;
    static JFrame frame;

    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;

    public Settings() {
        createGUI();
    }

    public static void addComponentsToFrame(Container pane) {
        //Creates main sub-panel and font for labels
        comps = new JPanel(new GridLayout(0, 1));
        Font font = new Font("Century Gothic", Font.BOLD, 16);

    /* -----FACTORY RESET----- */
        //Create new sub-panel
        factoryReset = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        if (shouldFill) {
            c1.fill = GridBagConstraints.HORIZONTAL;
        }

        if (shouldWeightX) {
            c1.weightx = 0.5;
        }

        //Creates and places description label
        frLabel = new JLabel("Reset the index, deleting all stored information on all text files");
        c1.insets = new Insets(0, 10, 0, 4);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 1;
        c1.gridy = 0;
        factoryReset.add(frLabel, c1);

        //Creates and places factory reset button
        frButton = new JButton("Reset");
        c1.weightx = 0.5;
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 2;
        factoryReset.add(frButton, c1);

        //Creates and places title label
        frTitle = new JLabel("Factory Reset");
        frTitle.setFont(font);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        factoryReset.add(frTitle, c1);


    /* -----AUTO INDEX-----*/
        //Create new sub-panel
        autoIndexTime = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        if (shouldFill) {
            c2.fill = GridBagConstraints.HORIZONTAL;
        }

        if (shouldWeightX) {
            c2.weightx = 0.5;
        }

        //Creates and places description label
        auLabel = new JLabel("Set the program to automatically re-index all stored files to every...");
        c2.gridx = 1;
        c2.gridy = 0;
        autoIndexTime.add(auLabel, c2);

        //Creates and places dropdown
        auDropdown = new JComboBox<>(new String[]{"Never", "30 minutes", "60 minutes", "2 hours", "6 hours", "12 hours", "24 hours"});
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(0, 10, 0, 5);
        c2.gridx = 2;
        autoIndexTime.add(auDropdown, c2);

        //Creates and places title label
        auTitle = new JLabel("Auto-Index Time");
        auTitle.setFont(font);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(0, 10, 0, 18);
        c2.gridx = 0;
        autoIndexTime.add(auTitle, c2);


    /* -----LOCKDOWN-----*/
        //Creates new sub-panel
        lockdown = new JPanel(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        if (shouldFill) {
            c3.fill = GridBagConstraints.HORIZONTAL;
        }

        if (shouldWeightX) {
            c3.weightx = 0.5;
        }

        //Creates and places title label
        ldTitle = new JLabel("Lockdown");
        ldTitle.setFont(font);
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.insets = new Insets(0, 10, 0, 20);
        c3.gridx = 0;
        c3.gridy = 0;
        lockdown.add(ldTitle, c3);

        //Creates and places description label
        ldLabel = new JLabel("Set a password to your personal index.");
        c3.gridx = 1;
        c3.insets = new Insets(0, 0, 0, 30);
        lockdown.add(ldLabel, c3);

        //Createsa and places text field
        ldField = new JTextField(10);
        c3.gridx = 2;
        c3.insets = new Insets(0, 30, 0, 5);
        lockdown.add(ldField, c3);


        //Creates new buttons sub-panel and adds confirm and cancel buttons
        buttons = new JPanel(new FlowLayout());
        confirm = new JButton("Confirm");
        cancel = new JButton("Cancel");
        buttons.add(confirm);
        buttons.add(cancel);

        //Adds all sub-panels to main panel and adds it to the frame
        comps.add(factoryReset);
        comps.add(autoIndexTime);
        comps.add(lockdown);
        comps.add(buttons);
        pane.add(comps);
    }

    public static void createGUI() {
        frame = new JFrame("Test");
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
