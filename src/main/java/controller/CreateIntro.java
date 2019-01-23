package controller;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.DocumentException;
import model.ETL;
import model.ETLRow;
import util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateIntro {

    public String createIntro(ETL etl) throws ParseException {
        ArrayList<String> pdfList = new ArrayList<>();
        String dest = "tmp/intro.pdf";
        Util.setTmpFiles(dest);
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
            Util.merge(pdfList, dest, null);
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

        return "raw/ETL1727_00-COVER.pdf";
    }

    private String loadTOR(ETL etl) throws FileNotFoundException {
        String dest = "tmp/tor.pdf";
        Util.setTmpFiles(dest);
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph("Table of Revision");
        paragraph.setFontSize(14);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {70F, 70F, 70F, 25F, 70F, 310F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list
        table.setFontSize(10);
        table.addHeaderCell(Util.setCell("CHANGE", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DATE", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("CHAPTER", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("#", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("PART#", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DESCRIPTION", true, TextAlignment.LEFT, true));

        boolean condition = false;


        String oldChangeNo = "";
        Cell c1 = null, c2 = null, c3 = null, c4 = null, c5 = null, c6 = null;
        for (ETLRow row : etl.getSortedChangeNoList()){
            if (row.getChangeno() != null && !row.getChangeno().isEmpty()){
                if(!row.getChangeno().equals(oldChangeNo)) {
                    if(oldChangeNo != "") {
                        table.addCell(c1);
                        table.addCell(c2);
                        table.addCell(c3);
                        table.addCell(c4);
                        table.addCell(c5);
                        table.addCell(c6);
                    }
                    c1 = Util.setCell(String.valueOf(row.getChangeno()), condition, TextAlignment.CENTER, false);
                    String d = Util.parseDate(row.getChangeno());
                    c2 = Util.setCell(d, condition, TextAlignment.CENTER, false);
                    c3 = Util.setCell(row.getChapter().substring(0, 2).replaceFirst("^0+(?!$)", ""),
                            condition, TextAlignment.CENTER, false);
                    int l = String.valueOf(row.getNo()).length();
                    String pos = String.valueOf(row.getNo()).substring(l - 2, l);
                    c4 = Util.setCell(pos.replaceFirst("^0+(?!$)", ""),
                            condition, TextAlignment.CENTER, false);
                    c5 = Util.setCell(row.getPartno(), condition, TextAlignment.CENTER, false);
                    c6 = Util.setCell(row.getDescription(), condition, TextAlignment.LEFT, false);
                    condition = !condition;
                    oldChangeNo = row.getChangeno();
                }else{


                }
            }

        }

        document.add(table);
        // Closing the document
        document.close();
        return dest;
    }


    private String loadTOC(ETL etl) throws FileNotFoundException {
        String dest = "tmp/toc.pdf";
        Util.setTmpFiles(dest);
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph("Table of Content");
        paragraph.setFontSize(14);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {35F, 450F, 35F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list
        table.setFontSize(10);
        table.addHeaderCell(Util.setCell("CHAPTER", true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell("DESCRIPTION", true, TextAlignment.LEFT, true).setPaddingLeft(40));
        table.addHeaderCell(Util.setCell("PAGE", true, TextAlignment.RIGHT, true));

        boolean condition = false;
        // Add elements to the list
        for (ETLRow row : etl.getEtl()){
            if (row.getNo()%100 == 0 && row.getItemType() == 'T'){
                int l = String.valueOf(row.getNo()).length();
                table.addCell(Util.setCell(String.valueOf(row.getNo()).substring(0,l-2), condition, TextAlignment.RIGHT, false));
                table.addCell(Util.setCell(row.getPartno(), condition, TextAlignment.LEFT, false).setPaddingLeft(40));
                table.addCell(Util.setCell(etl.getContentMap().get(String.valueOf(row.getNo())).toString(), condition, TextAlignment.RIGHT, false));
                condition = !condition;
            }
        }
        document.add(table);
        document.close();
        return dest;

    }
}

