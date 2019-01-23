package util;

import com.itextpdf.text.DocumentException;
import controller.CreateChapter;
import controller.CreateIntro;
import model.ETL;
import model.ETLRow;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class Main {
    public static void main(String[] args) throws IOException, DocumentException, ParseException {
        ArrayList<String> pdfList = new ArrayList<>();
        ETL etl = new ETL("C:\\Users\\khoef\\Desktop\\ETL1727.csv");
        CreateChapter cc = new CreateChapter();
        ArrayList<ETLRow> chapterItems = new ArrayList();
        for(int i=100; i<=9999; i++){
            if(i!=100 && i%100==0){
                pdfList.add(cc.createChapter(chapterItems));
                chapterItems = new ArrayList<>();
            }
            chapterItems.addAll(etl.getItems(i));
        }
        Util.merge(pdfList, "tmp/helper.pdf", etl.getContentMap());
        Util.setTmpFiles("tmp/helper.pdf");
        CreateIntro ci = new CreateIntro();
        pdfList.clear();
        pdfList.add(ci.createIntro(etl));
        pdfList.add("tmp/helper2.pdf");
        Util.setTmpFiles("tmp/helper2.pdf");
        Util.merge(pdfList,"tmp/final.pdf", null);
        Util.removeTmpFiles();
    }
}
