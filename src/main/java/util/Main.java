package util;

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
        ArrayList<String> pdfList = new ArrayList<>();
        ETL etl = null;
        System.out.println("************************PDF Publisher************************");
        System.out.println("ETL: ");
        //Enter data using BufferReader
        BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
        String etlFileName = null;

        try {
            etlFileName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            e.printStackTrace();
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
}
