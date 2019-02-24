package util;

import com.itextpdf.text.DocumentException;
import controller.CreateChapter;
import controller.CreateIntro;
import model.ETL;
import model.ETLRow;
import org.apache.commons.io.FileUtils;
import view.PublishUi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class PublishController implements ActionListener {

    private PublishUi view;
    private static File baseDir;
    private static File inputFile;
    private Localization localization;
    private ArrayList<String> docFiles;
    int error = 0;
    private int scaleFactor;

    private PublishController(){
        view = new PublishUi(this);
    }

    private void publish() {

        ArrayList<String> pdfList = new ArrayList<>();
        ETL etl = null;
        String etlFileFullPath = inputFile.toString();

        this.docFiles = new ArrayList<>();
        int index = etlFileFullPath.lastIndexOf('\\');
        etlFileFullPath = etlFileFullPath.toUpperCase();

        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir") + "/pdf_publisher/" +
                etlFileFullPath.substring(index + 1, etlFileFullPath.lastIndexOf('.')));

        try {
            etl = new ETL(etlFileFullPath);
        } catch (FileNotFoundException | NumberFormatException e) {
            view.setErrorText("Probleme beim Öffnen des CSV Files. " +
                    e.getMessage());
        }

        for(ETLRow documentRow : etl.getEtl()){
            if((documentRow.getNo() == 10 || documentRow.getNo() % 100 == 0) && documentRow.getItemType() == 'D'){
                String fullDocPath = documentRow.getDescriptionLine2();
                int fileNameIndex = fullDocPath.lastIndexOf('\\');
                String docFileName = documentRow.getDescriptionLine2().substring(fileNameIndex+1);
                docFiles.add(docFileName);
            }
        }

        this.baseDir = new File(etlFileFullPath.substring(0, index));
        if (baseDir.isDirectory()) {
            if (docFiles != null) {
                for (String file : docFiles) {
                    try {
                        FileUtils.moveFile(new File(baseDir + "\\" + file), new File(tempDir + "/" + file));
                    } catch (IOException e) {
                        view.setErrorText("Probleme beim Verschieben der Files ins Working Directory");
                        e.printStackTrace();
                    }
                }
            }
        }

        if (error == 0) {
            CreateChapter cc = new CreateChapter();
            ArrayList chapterItems = new ArrayList();
            for (int i = 100; i <= 9999; i++) {
                if (i != 100 && i % 100 == 0) {
                    try {
                        pdfList.add(cc.createChapter(chapterItems, tempDir, localization, scaleFactor, view));
                    } catch (IOException e) {
                        view.setErrorText("Fehler beim Laden einer Baugruppenzeichnung: " +
                                e.getMessage());
                        e.printStackTrace();
                    }
                    chapterItems = new ArrayList<>();
                }
                chapterItems.addAll(etl.getItems(i));
            }
            try {
                Util.merge(pdfList, tempDir + "/helper.pdf", etl.getContentMap(), tempDir);
            } catch (IOException | DocumentException e) {
                view.setErrorText("Fehler beim Zusammenhängen der Kapitel!");
            }
            CreateIntro ci = new CreateIntro();
            pdfList.clear();
            try {
                pdfList.add(ci.createIntro(etl, tempDir, localization));
            } catch (ParseException e) {
                view.setErrorText("Fehler beim Erstellen der Einleitung");
            }
            pdfList.add(tempDir + "/helper.pdf");
            try {
                Util.merge(pdfList, tempDir + "/helper2.pdf", null, tempDir);
            } catch (IOException | DocumentException e) {
                view.setErrorText("Fehler beim Zusammenbau des PDFs");
            }
            try {
                Util.stampPageNo(etl.getEtlNo(), tempDir.toString(), baseDir.toString(), localization);
                view.setErrorText("PDF erstellt in: " + baseDir.toString());
            } catch (IOException | DocumentException e) {
                view.setErrorText("Fehler beim Andrucken der Fußzeile!");
            }
            Util.removeTmpFiles(tempDir);

        }
    }

    public static void main(String[] args){
        new PublishController();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(view.getStartPublishing() == e.getSource()){
            view.setErrorText("");
            this.inputFile = view.getfInputFile().getSelectedFile();
            this.scaleFactor = view.getnScaleFactor();
            if(fileIsValid(inputFile.getName())) {
                try {
                    this.localization = new Localization(view.getcLang().getSelectedItem().toString());
                    publish();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    private boolean fileIsValid(String filename) {
        boolean fileNamePatternCorrect = false;
        int fileTypeStart = filename.lastIndexOf('.');
        String fileType = filename.substring(fileTypeStart + 1);
        if (fileType.equalsIgnoreCase("csv") ||
                fileType.equalsIgnoreCase("txt")){
            String suffix = filename.substring(0,3);
            if (suffix.equalsIgnoreCase("ETL")){
                return true;
            } else {
                view.setErrorText("Der Dateiname startet nicht mit 'ETL'!");
            }
        } else {
            view.setErrorText("Falscher Dateityp. Bitte ein CSV File auswählen");
        }
        return false;
    }
}
