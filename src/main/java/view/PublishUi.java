package view;

import util.PublishController;

import javax.swing.*;
import java.awt.*;

/**
 * PDF Publisher
 * Created by khoef on 26.01.2019.
 */
public class PublishUi extends JFrame {

    private JButton startPublishing = new JButton("PDF erstellen");
    private JFileChooser fInputFile = new JFileChooser();
    private JFileChooser fBaseDir = new JFileChooser();
    private JComboBox cLang = new JComboBox();

    public PublishUi(PublishController controller){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Erstellen Sie aus einem Rohfile ein ETL PDF", SwingConstants.CENTER),
                BorderLayout.PAGE_START);

        fInputFile.setControlButtonsAreShown(false);
        fBaseDir.setControlButtonsAreShown(false);
        cLang.addItem("EN");
        cLang.addItem("DE");

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new GridLayout(2,2));
        JLabel lInputFile = new JLabel("Eingabefile: ");
        centerPane.add(lInputFile);
        centerPane.add(fInputFile);
        JLabel lBaseDir = new JLabel("Ausgabepfad: ");
        centerPane.add(lBaseDir);
        centerPane.add(fBaseDir);
        container.add(centerPane, BorderLayout.CENTER);


        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new FlowLayout());
        bottomPane.add(startPublishing);
        startPublishing.addActionListener(controller);
        container.add(bottomPane, BorderLayout.PAGE_END);

        setTitle("PDF-Publisher");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        container.setPreferredSize(new Dimension(900, 900));
        pack();
        setVisible(true);

    }

    public JButton getStartPublishing() {
        return startPublishing;
    }

    public JFileChooser getfBaseDir() {
        return fBaseDir;
    }

    public JFileChooser getfInputFile() {
        return fInputFile;
    }

    public JComboBox getcLang() {
        return cLang;
    }
}
