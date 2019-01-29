package controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import model.ETL;
import model.ETLRow;
import util.Localization;
import util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateIntro {

    public String createIntro(ETL etl, Path tempDir, Localization localization) throws ParseException {
        ArrayList<String> pdfList = new ArrayList<>();
        String dest = tempDir + "/intro.pdf";
        String cover = null;
        for (ETLRow row : etl.getEtl()){
            if (row.getNo()== 10 && row.getItemType() == 'D'){
                int i = row.getDescriptionLine2().lastIndexOf("\\");
                cover = row.getDescriptionLine2().substring(i+1, row.getDescriptionLine2().length());
                int x = 1;
            }
        }
        try {
            pdfList.add(loadCover(cover, tempDir.toString()));
            pdfList.add(loadTOR(etl, tempDir.toString(), localization));
            pdfList.add(loadTOC(etl, tempDir.toString(), localization));
            Util.merge(pdfList, dest, null, tempDir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;

    }

    private String loadCover(String coverName, String tempDir) throws FileNotFoundException, MalformedURLException {
        String fileformat = coverName.substring(coverName.length()-3,coverName.length());
        if(fileformat.equalsIgnoreCase("pdf")) {
            return tempDir +"/"+ coverName;
        } else if(fileformat.equalsIgnoreCase("jpg")){
            String dest = tempDir + "/COVER.pdf";
            Document doc = Util.createPdf(dest);
            String imgFile = tempDir+"/"+coverName;
            ImageData data = ImageDataFactory.create(imgFile);
            Image image = new Image(data);
            image.setFixedPosition(0,0);
            image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());



            doc.add(image);
            doc.close();
            return dest;
        }
        return tempDir + "/" +coverName;
    }

    private String loadTOR(ETL etl, String tempDir, Localization localization) throws FileNotFoundException {
        String dest = tempDir +"/tor.pdf";
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph(localization.getRevision());
        paragraph.setFontSize(14);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {70F, 70F, 70F, 25F, 70F, 310F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list
        table.setFontSize(9);
        table.addHeaderCell(Util.setCell(localization.getChange(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getDate(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getChapter(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getNum(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getPart(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getDescr(), true, TextAlignment.LEFT, true));

        boolean condition = false;


        String oldChangeNo = "";
        Cell c1 = null, c2 = null, c3 = null, c4 = null, c5 = null, c6 = null;
        String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null;
        if(etl.getSortedChangeNoList().size() > 0) {
            for (ETLRow row : etl.getSortedChangeNoList()) {
                if (row.getChangeno() != null && !row.getChangeno().isEmpty() && row.getNo() % 100 != 0 && row.getNo() != 10) {
                    if (!row.getChangeno().equals(oldChangeNo)) {
                        if (oldChangeNo != "") {
                            c1 = Util.setCell(s1, condition, TextAlignment.CENTER, false);
                            c2 = Util.setCell(s2, condition, TextAlignment.CENTER, false);
                            c3 = Util.setCell(s3, condition, TextAlignment.CENTER, false);
                            c4 = Util.setCell(s4, condition, TextAlignment.CENTER, false);
                            c5 = Util.setCell(s5, condition, TextAlignment.CENTER, false);
                            c6 = Util.setCell(s6, condition, TextAlignment.LEFT, false);
                            condition = !condition;
                            oldChangeNo = row.getChangeno();
                            table.addCell(c1);
                            table.addCell(c2);
                            table.addCell(c3);
                            table.addCell(c4);
                            table.addCell(c5);
                            table.addCell(c6);
                        }
                        s1 = String.valueOf(row.getChangeno());
                        s2 = Util.parseDate(row.getChangeno());
                        s3 = row.getChapter().substring(0, 2).replaceFirst("^0+(?!$)", "");
                        int l = String.valueOf(row.getNo()).length();
                        s4 = String.valueOf(row.getNo()).substring(l - 2, l).replaceFirst("^0+(?!$)", "");
                        s5 = row.getPartno();
                        s6 = row.getDescription();
                        oldChangeNo = row.getChangeno();
                    } else {
                        s1 = String.valueOf(row.getChangeno());
                        s2 = Util.parseDate(row.getChangeno());
                        s3 = s3 + "\r\n" + row.getChapter().substring(0, 2).replaceFirst("^0+(?!$)", "");
                        int l = String.valueOf(row.getNo()).length();
                        s4 = s4 + "\r\n" + String.valueOf(row.getNo()).substring(l - 2, l).replaceFirst("^0+(?!$)", "");
                        s5 = s5 + "\r\n" + row.getPartno();
                        s6 = s6 + "\r\n" + row.getDescription();
                        oldChangeNo = row.getChangeno();
                    }
                }
            }

            c1 = Util.setCell(s1, condition, TextAlignment.CENTER, false);
            c2 = Util.setCell(s2, condition, TextAlignment.CENTER, false);
            c3 = Util.setCell(s3, condition, TextAlignment.CENTER, false);
            c4 = Util.setCell(s4, condition, TextAlignment.CENTER, false);
            c5 = Util.setCell(s5, condition, TextAlignment.CENTER, false);
            c6 = Util.setCell(s6, condition, TextAlignment.LEFT, false);
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
            table.addCell(c6);
        }
        document.add(table);
        document.close();
        return dest;
    }


    private String loadTOC(ETL etl, String tempDir, Localization localization) throws FileNotFoundException {
        String dest = tempDir+"/toc.pdf";
        Document document = Util.createPdf(dest);
        // Creating a Paragraph
        Paragraph paragraph = new Paragraph(localization.getInhalt());
        paragraph.setFontSize(14);
        paragraph.setBold();
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);

        // Creating a table
        float[] pointColumnWidths = {35F, 450F, 35F};
        Table table = new Table(pointColumnWidths);        // Add elements to the list
        table.setFontSize(10);
        table.addHeaderCell(Util.setCell(localization.getChapter(), true, TextAlignment.CENTER, true));
        table.addHeaderCell(Util.setCell(localization.getDescr(), true, TextAlignment.LEFT, true).setPaddingLeft(40));
        table.addHeaderCell(Util.setCell(localization.getPage(), true, TextAlignment.RIGHT, true));

        boolean condition = false;
        // Add elements to the list
        for (ETLRow row : etl.getEtl()){
            if (row.getNo()%100 == 0 && row.getItemType() == 'T'){
                int l = String.valueOf(row.getNo()).length();
                table.addCell(Util.setCell(String.valueOf(row.getNo()).substring(0,l-2), condition, TextAlignment.RIGHT, false));
                String descr =  row.getDescriptionLine2();
                descr = descr.replaceAll("\\r", "\r\n");
                table.addCell(Util.setCell(descr, condition, TextAlignment.LEFT, false).setPaddingLeft(40));
                table.addCell(Util.setCell(etl.getContentMap().get(String.valueOf(row.getNo())).toString(), condition, TextAlignment.RIGHT, false));
                condition = !condition;
            }
        }
        document.add(table);
        document.close();
        return dest;

    }
}

