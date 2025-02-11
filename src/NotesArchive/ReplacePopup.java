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
        comps = new JPanel();
        buttons = new JPanel();
        confirm = new JButton("Re-Index");
        cancel = new JButton("Cancel");
        layout = new FlowLayout();
        label = new JLabel("This file has already been indexed. Do you want to re-index it?");
        comps = new JPanel();

        comps.setLayout(layout);
        buttons.add(confirm);
        buttons.add(cancel);
        comps.add(label);

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
