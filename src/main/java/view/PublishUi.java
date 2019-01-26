package view;

import util.PublishController;

import javax.swing.*;
import java.awt.*;

/**
 * SemNOTAM Project (User Interface)
 * Created by khoef on 26.01.2019.
 */
public class PublishUi extends JFrame {

    private Container container = getContentPane();
    private JButton startPublishing = new JButton("PDF erstellen");
    private PublishController controller;
    private JPanel centerPane = new JPanel();
    private JPanel bottomPane = new JPanel();
    private JLabel lInputFile = new JLabel("Eingabefile: ");
    private JLabel lBaseDir = new JLabel("Ausgabepfad: ");
    private JFileChooser fInputFile = new JFileChooser();
    private JFileChooser fBaseDir = new JFileChooser();

    public PublishUi(PublishController controller){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.controller = controller;
        container.setLayout(new BorderLayout());
        container.add(new JLabel("Erstellen Sie aus einem Rohfile ein ETL PDF", SwingConstants.CENTER),
                BorderLayout.PAGE_START);

        fBaseDir.set

        centerPane.setLayout(new FlowLayout());
        centerPane.add(lInputFile);
        centerPane.add(fInputFile);
        centerPane.add(lBaseDir);
        centerPane.add(fBaseDir);
        container.add(centerPane, BorderLayout.CENTER);

        bottomPane.setLayout(new FlowLayout());
        bottomPane.add(startPublishing);
        startPublishing.addActionListener(controller);
        container.add(bottomPane, BorderLayout.PAGE_END);

        setTitle("PDF-Publisher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        container.setPreferredSize(new Dimension(400, 200));
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
}
