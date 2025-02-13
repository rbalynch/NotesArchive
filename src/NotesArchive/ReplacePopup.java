/**
 * This dialog will display when the user selects a file
 * that was already foudn in the index. It will ask the user
 * if they mean to re-index the file, and takes action
 * based on the response selected.
 */

package NotesArchive;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

public class ReplacePopup extends JFrame {
    static JLabel label;
    static JButton confirm;
    static JButton cancel;
    static JPanel comps;
    static JPanel buttons;
    static JFrame frame;
    static FlowLayout layout;

    public ReplacePopup() {
        createGUI();
    }
    public static void addComponentsToFrame(Container pane) {
        //Sub-panels and layout
        comps = new JPanel();
        buttons = new JPanel();
        layout = new FlowLayout();

        //UI Elements
        confirm = new JButton("Re-Index");
        cancel = new JButton("Cancel");
        label = new JLabel("This file has already been indexed. Do you want to re-index it?");

        //Add components
        comps.setLayout(layout);
        buttons.add(confirm);
        buttons.add(cancel);
        comps.add(label);

        //Add sub-panels to frame
        pane.add(comps, BorderLayout.CENTER);
        pane.add(buttons, BorderLayout.SOUTH);
    }
    public static void createGUI() {
        frame = new JFrame();
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
