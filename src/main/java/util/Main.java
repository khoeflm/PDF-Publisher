package util;

import com.itextpdf.text.DocumentException;
import controller.CreateChapter;
import controller.CreateIntro;
import model.ETL;
import model.ETLRow;

import java.io.IOException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class Main {
    public static void main(String[] args) throws IOException, DocumentException {
        ArrayList<String> pdfList = new ArrayList<>();
        ETL etl = new ETL("C:\\Users\\khoef\\Desktop\\ETL1727.csv");
        CreateIntro ci = new CreateIntro();
        pdfList.add(ci.createIntro(etl));
        CreateChapter cc = new CreateChapter();
        ArrayList<ETLRow> chapterItems = new ArrayList();
        for(int i=100; i<=9999; i++){
            if(i!=100 && i%100==0){
                pdfList.add(cc.createChapter(chapterItems));
                chapterItems = new ArrayList<>();
            }
            chapterItems.addAll(etl.getItems(i));
        }
        Util.merge(pdfList, "tmp/helper.pdf");
    }
}
