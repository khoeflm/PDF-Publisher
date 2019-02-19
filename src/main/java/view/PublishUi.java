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
    private JComboBox cLang = new JComboBox();
    private JSpinner nScaleFactor = new JSpinner();
    private JTextArea tErrorBox = new JTextArea();

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

        nScaleFactor.setModel(new SpinnerNumberModel(100, 0, 100, 1));
        fInputFile.setControlButtonsAreShown(false);
        cLang.addItem("EN");
        cLang.addItem("DE");
        tErrorBox.setRows(5);
        tErrorBox.setSize(600,60);

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.PAGE_AXIS));
        JLabel lInputFile = new JLabel("Eingabefile: ");
        centerPane.add(lInputFile);
        centerPane.add(fInputFile);
        JLabel lBaseDir = new JLabel("Ausgabesprache: ");
        centerPane.add(lBaseDir);
        centerPane.add(cLang);
        centerPane.add(nScaleFactor);
        centerPane.add(tErrorBox);
        container.add(centerPane, BorderLayout.CENTER);

        JPanel bottomPane = new JPanel();
        bottomPane.add(startPublishing);
        startPublishing.addActionListener(controller);
        container.add(bottomPane, BorderLayout.PAGE_END);

        setTitle("PDF-Publisher");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        container.setPreferredSize(new Dimension(700, 500));
        pack();
        setVisible(true);

    }

    public JButton getStartPublishing() {
        return startPublishing;
    }

    public JFileChooser getfInputFile() {
        return fInputFile;
    }

    public JComboBox getcLang() {
        return cLang;
    }

    public JTextArea gettErrorBox() {
        return tErrorBox;
    }

    public int getnScaleFactor(){ return (int) nScaleFactor.getValue(); }

    public void setErrorText(String error){
        this.tErrorBox.append(error);
        this.tErrorBox.append("\n");
    }
}
