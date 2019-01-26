package util;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.text.DocumentException;
import controller.CreateChapter;
import controller.CreateIntro;
import model.ETL;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class Main {

    private static Path tempDir;
    private static File baseDir;

    public static void main(String[] args) throws IOException {
        tempDir = Paths.get(System.getProperty("java.io.tmpdir")+"/pdf_publisher/");
        LicenseKey.loadLicenseFile(Main.class.getResourceAsStream("/testkey.xml"));

        ArrayList<String> pdfList = new ArrayList<>();
        ETL etl = null;
        System.out.println("*************************************************************");
        System.out.println("********************    PDF Publisher    ********************");
        System.out.println("*************************************************************");
        System.out.println();
        System.out.println("Vollständigen Pfad inkl. Filename und Fileendung angeben");
        System.out.print("ETL: ");

        String etlFileName = fileNameInput();


        String[] files;
        int index = etlFileName.lastIndexOf('\\');
        etlFileName = etlFileName.toUpperCase();

        baseDir = new File(etlFileName.substring(0, index));
        if (baseDir.isDirectory()){
            files = baseDir.list();
            if (files != null) {
                for (String file : files) {
                    String fileFilter = etlFileName.substring(index+1, etlFileName.lastIndexOf('.'));
                    if(file.contains(fileFilter)){
                        FileUtils.moveFile(new File(baseDir + "\\" + file), new File(tempDir + "/" + file));
                    }
                }
            }
        }

        etl = new ETL(etlFileName);

        CreateChapter cc = new CreateChapter();
        ArrayList chapterItems = new ArrayList();
        for(int i=100; i<=9999; i++){
            if(i!=100 && i%100==0){
                try {
                    pdfList.add(cc.createChapter(chapterItems, tempDir));
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
                chapterItems = new ArrayList<>();
            }
            if (etl != null) {
                chapterItems.addAll(etl.getItems(i));
            }
        }
        try {
            if (etl != null) {
                Util.merge(pdfList, tempDir+"/helper.pdf", etl.getContentMap(), tempDir);
            }
        } catch (IOException | DocumentException | ParseException e) {
            e.printStackTrace();
        }
        CreateIntro ci = new CreateIntro();
        pdfList.clear();
        try {
            pdfList.add(ci.createIntro(etl, tempDir));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pdfList.add(tempDir+"/helper.pdf");
        try {
            Util.merge(pdfList,tempDir+"/helper2.pdf", null, tempDir);
        } catch (IOException | DocumentException | ParseException e) {
            e.printStackTrace();
        }
        try {
            if (etl != null) {
                Util.stampPageNo(etl.getEtlNo(), tempDir.toString(), baseDir.toString());
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
        Util.removeTmpFiles(tempDir);

    }

    private static String fileNameInput() {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        String etlFileName = null;
        boolean fileNamePatternCorrect = false;
        while(!fileNamePatternCorrect) {
            try {
                etlFileName = reader.readLine();
                int lastDelimiter = etlFileName.lastIndexOf('\\');
                if (lastDelimiter == -1) {
                    lastDelimiter = etlFileName.lastIndexOf('/');
                    if(lastDelimiter == -1){
                        System.out.println("Keine Pfadangabe - Bitte einen Filepfad angeben:");
                    }
                }
                int fileTypeStart = etlFileName.lastIndexOf('.');
                if (lastDelimiter != -1 && fileTypeStart != -1) {
                    String fileType = etlFileName.substring(fileTypeStart + 1);
                    if (fileType.equalsIgnoreCase("csv") ||
                            fileType.equalsIgnoreCase("txt")) {
                        String filename = etlFileName.substring(lastDelimiter + 1, fileTypeStart);
                        if (filename.substring(1, 4).equalsIgnoreCase("ETL")) {
                            System.out.println("Der Dateiname startet nicht mit 'ETL'! Bitte richtigen Pfad angeben: ");
                        } else fileNamePatternCorrect = true;
                    } else{
                        System.out.println("Falscher Dateityp! Bitte richtigen Pfad angeben: ");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return etlFileName;
    }
}
