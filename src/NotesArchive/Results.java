package NotesArchive;

import org.apache.lucene.document.Document;
import org.json.simple.parser.ParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.SwingUtilities.invokeLater;
public class Results extends JDialog{
    static JButton next, prev;
    static ArrayList<Document> hits;
    static JPopupMenu menu;
    static JMenuItem m1, m2, m3;

    public Results(ArrayList<Document> s) {
        hits = s;
        invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createGUI();
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public static void addComponentsToFrame(Container pane) {
        JPanel comps = new JPanel(); //Component panel

        next = new JButton("Next");
        prev = new JButton("Prev");

        JPanel buttons = new JPanel();
        buttons.add(prev);
        buttons.add(next);

        comps.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.setVisible(false); //Every left click anywhere on comp menu -> close right click menu if open
            }
        });

        JPanel links = new JPanel(); //Panel for links
        links.setLayout(new BoxLayout(links, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel(hits.size() + " Result(s) Found"); //Creates new title based on number of hits found
        if (hits.isEmpty()) { //Changes text of title if no results are found
            title.setText("No Results Found!");
        }
        title.setFont(new Font("Courier New", Font.BOLD, 16));
        links.add(title);
        links.add(Box.createRigidArea(new Dimension(0, 10)));

        menu = new JPopupMenu();
        m1 = new JMenuItem("Open");
        m2 = new JMenuItem("View details");
        m3 = new JMenuItem("Create a copy");
        menu.add(m1);
        menu.add(m2);
        menu.add(m3);
        menu.setVisible(false);

        for (Document d : hits) {
            String directory = d.get("directory");
            String fileName = d.get("fileName");

            JLabel link = new JLabel(fileName);
            link.setFont(new Font("Courier New", Font.BOLD, 12));
            link.setForeground(Color.BLUE);
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        try {
                            Desktop.getDesktop().open(new File(directory));
                        }
                        catch (IOException i) {
                            i.printStackTrace();
                        }
                    }
                    else if (SwingUtilities.isRightMouseButton(e)) {
                        m1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Desktop.getDesktop().open(new File(directory));
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                                menu.setVisible(false);
                            }
                        }); //Open file action
                        m2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    FileInfoPopup info = new FileInfoPopup(d);
                                    info.createGUI();
                                } catch (IOException | ParseException ex) {
                                    throw new RuntimeException(ex);
                                }
                                menu.setVisible(false);
                            }
                        }); //File details menu action
                        m3.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //CREATE A COPY OF THE NEW FILE AND AUTOMATICALLY INDEX
                            }
                        }); //Create new copy action

                        PointerInfo a = MouseInfo.getPointerInfo(); //Gets location of mouse cursor
                        Point b = a.getLocation();

                        menu.setLocation(b); //Sets location of menu to same location as mouse cursor
                        menu.setVisible(true); //Displays menu
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    link.setText(fileName);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    link.setText("<html><a href=''>" + fileName + "</a></html>");
                }
            });
            links.add(link);
            links.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        comps.add(links, BorderLayout.CENTER);
        pane.add(comps, BorderLayout.WEST);
        pane.add(buttons, BorderLayout.SOUTH);
    }
    public static void createGUI() throws IOException, ParseException {
        JFrame frame = new JFrame();

        addComponentsToFrame(frame.getContentPane());
        frame.pack();
        frame.addWindowListener(new WindowListener() {
                                    @Override
                                    public void windowOpened(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowClosing(WindowEvent e) {
                                        menu.setVisible(false);
                                    }

                                    @Override
                                    public void windowClosed(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowIconified(WindowEvent e) {
                                        menu.setVisible(false);
                                    }

                                    @Override
                                    public void windowDeiconified(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowActivated(WindowEvent e) {

                                    }

                                    @Override
                                    public void windowDeactivated(WindowEvent e) {
                                        menu.setVisible(false);
                                    }
                                });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
