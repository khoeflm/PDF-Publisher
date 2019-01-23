package controller;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.DocumentException;
import model.ETL;
import model.ETLRow;
import util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateIntro {

    public String createIntro(ETL etl){
        ArrayList<String> pdfList = new ArrayList<>();
        String dest = "tmp/intro.pdf";
        String cover = null;
        for (ETLRow row : etl.getEtl()){
            if (row.getNo()== 10 && row.getItemType() == 'D'){
                int i = row.getPartno().indexOf(" "); 
                cover = row.getPartno().substring(0, i-1);
                int x = 1;
            }
        }
        try {
            pdfList.add(loadCover(cover));
            pdfList.add(loadTOR(etl));
            pdfList.add(loadTOC(etl));
            Util.merge(pdfList, dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;

    }

    private String loadCover(String cover) {

        return "tmp/ETL1727_00-COVER.pdf";
    }

    private String loadTOR(ETL etl) throws FileNotFoundException {
        String dest = "tmp/tor.pdf";
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph("Table of Revision");
        paragraph.setFontSize(14);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {70F, 70F, 70F, 310F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list

        table.addHeaderCell(Util.setCell("CHANGE", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DATE", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("CHAPTER", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DESCRIPTION", true, TextAlignment.LEFT, true));

        boolean condition = false;
        for (ETLRow row : etl.getEtl()){
            if (row.getChangeno() != null && !row.getChangeno().isEmpty()){
                table.addCell(Util.setCell(String.valueOf(row.getChangeno()), condition, TextAlignment.CENTER, false));
                String d = Util.parseDate(row.getChangeno());
                table.addCell(Util.setCell(d, condition, TextAlignment.CENTER, false));
                table.addCell(Util.setCell(row.getChapter().substring(0,2), condition, TextAlignment.CENTER, false));
                int l = String.valueOf(row.getNo()).length();
                String pos = String.valueOf(row.getNo()).substring(l-2,l);
                table.addCell(Util.setCell(pos + " - "+row.getPartno()+" - "+row.getDescription(),
                        condition, TextAlignment.LEFT, false));
                condition = !condition;
            }
        }

        document.add(table);
        // Closing the document
        document.close();
        return dest;
    }


    private String loadTOC(ETL etl) throws FileNotFoundException {
        String dest = "tmp/toc.pdf";
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph("Table of Content");
        paragraph.setBold();
        paragraph.setFontSize(14);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {35F, 380F, 35F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list

        table.addHeaderCell(Util.setCell("CHAPTER", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DESCRIPTION", true, TextAlignment.LEFT, true)).setPaddingLeft(40);
        table.addHeaderCell(Util.setCell("PAGE", true, TextAlignment.RIGHT, true));

        boolean condition = false;
        // Add elements to the list
        for (ETLRow row : etl.getEtl()){
            if (row.getNo()%100 == 0 && row.getItemType() == 'T'){
                int l = String.valueOf(row.getNo()).length();
                table.addCell(Util.setCell(String.valueOf(row.getNo()).substring(0,l-2), condition, TextAlignment.RIGHT, false));
                table.addCell(Util.setCell(row.getPartno(), condition, TextAlignment.LEFT, false)).setPaddingLeft(40);
                table.addCell(Util.setCell("", condition, TextAlignment.RIGHT, false));
            }
        }

        // Adding paragraph to the document
        // Adding list to the document
        document.add(table);
        // Closing the document
        document.close();
        return dest;

    }
}

