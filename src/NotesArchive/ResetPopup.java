/**
 * This is the class for the warning popup upon requesting for
 * a complete factory reset, which erases all stored .json files
 * and wipes the index.
 */

package NotesArchive;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;

public class ResetPopup {
    static JLabel warning;
    static JButton yes, no;
    static JPanel buttons, text;
    static JFrame frame;

    public ResetPopup() {
        createGUI();
    }

    public static void addComponentsToFrame(Container pane) {
        //UI Elements
        yes = new JButton("Confirm");
        no = new JButton("Cancel");
        warning = new JLabel("This will delete the entire index. Any files indexed will not be affected. Continue?");

        //Sub-panels
        text = new JPanel(new FlowLayout());
        buttons = new JPanel();

        //Add to sub-panels
        text.add(warning, BorderLayout.CENTER);
        buttons.add(yes);
        buttons.add(no);

        //Add to frame
        pane.add(text, BorderLayout.CENTER);
        pane.add(buttons, BorderLayout.SOUTH);
    }
    public static void createGUI() {
        frame = new JFrame("Warning!");
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    } //Create popup
}
