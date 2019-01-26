package util;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.text.DocumentException;
import controller.CreateChapter;
import controller.CreateIntro;
import model.ETL;
import model.ETLRow;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;


/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class Main {
    public static void main(String[] args) throws IOException {


        LicenseKey.loadLicenseFile("licenceKey/testkey.xml");

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
        File file = new File(etlFileName.substring(0,index));
        if (file.isDirectory()){
            files = file.list();
            for (int i = 0; i < files.length; i++){
                FileUtils.copyFile(new File(file+ "\\"+files[i]), new File("raw/"+files[i]));
            }
        }


        try {
            etl = new ETL(etlFileName);
        } catch (FileNotFoundException e) {
            System.out.println("Datei kann nicht geöffnet werden");
        }

        CreateChapter cc = new CreateChapter();
        ArrayList<ETLRow> chapterItems = new ArrayList();
        for(int i=100; i<=9999; i++){
            if(i!=100 && i%100==0){
                try {
                    pdfList.add(cc.createChapter(chapterItems));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                chapterItems = new ArrayList<>();
            }
            chapterItems.addAll(etl.getItems(i));
        }
        try {
            Util.merge(pdfList, "tmp/helper.pdf", etl.getContentMap());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CreateIntro ci = new CreateIntro();
        pdfList.clear();
        try {
            pdfList.add(ci.createIntro(etl));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pdfList.add("tmp/helper.pdf");
        try {
            Util.merge(pdfList,"tmp/helper2.pdf", null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Util.stampPageNo(etl.getEtlNo());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Util.removeTmpFiles();

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
