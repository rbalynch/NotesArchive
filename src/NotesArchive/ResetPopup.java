package NotesArchive;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;

public class ResetPopup {
    static JLabel warning, cont;
    static JButton yes, no;
    static JPanel comps, buttons, text;
    static JFrame frame;

    public static void addComponentsToFrame(Container pane) {
        text = new JPanel();
        warning = new JLabel("This will delete the entire index. Any files indexed will not be affected.");
        cont = new JLabel("Continue?");
        text.add(warning);
        text.add(cont);

        yes = new JButton("Confirm");
        no = new JButton("Cancel");
        buttons = new JPanel();
        buttons.add(yes);
        buttons.add(no);

        comps = new JPanel();
        comps.add(text, BorderLayout.PAGE_START);
        comps.add(buttons, BorderLayout.PAGE_END);
        pane.add(comps);
    }

    public static void createGUI() {
        frame = new JFrame("Warning!");
        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        createGUI();
    }
}
