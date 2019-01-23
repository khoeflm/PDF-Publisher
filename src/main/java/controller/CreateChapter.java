package controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.ETLRow;
import util.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateChapter {

    public String createChapter(ArrayList<ETLRow> etl) throws IOException, DocumentException {
        String title = null;
        //lade Baugruppenbild
        if (etl.size() > 0 && etl.get(0) != null) {
            String rawFile = "tmp/"+etl.get(0).getNo()+"raw.pdf";
            Util.setTmpFiles(rawFile);
            Document doc = Util.createPdf(rawFile);

            int index = etl.get(0).getPartno().indexOf('-');
            String imgFile = "raw/"+ etl.get(0).getPartno().substring(0, index)+".jpg";
            ImageData data = ImageDataFactory.create(imgFile);

            // Creating an Image object
            Image image = new Image(data);


            image.scaleToFit(PageSize.A4.getWidth()-120, PageSize.A4.getHeight()-120);
            float x = (PageSize.A4.getWidth() - image.getImageScaledWidth())/2;
            float y = (PageSize.A4.getHeight() - image.getImageScaledHeight())/2;
            image.setFixedPosition(x, y);

            doc.add(image);
            doc.add(new AreaBreak());

            // Creating a table
            float[] pointColumnWidths = {25F, 10F, 65F, 310F, 30F, 60F};
            Table table = new Table(pointColumnWidths);
            table.setFontSize(10);

            boolean condition = true;

            table.addHeaderCell(Util.setCell("#",condition,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell("NEW",condition,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell("PART#",condition,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell("DESCRIPTION",condition,TextAlignment.LEFT, true));
            table.addHeaderCell(Util.setCell("QTY",condition,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell("CHANGE",condition,TextAlignment.CENTER, true));

            for (ETLRow row : etl) {
                condition = !condition;
                // Page Title
                if(row != null && row.getNo() % 100 == 0 && row.getItemType() == 'T') {
                    title = row.getNo()/100 + "   " + row.getPartno();
                    /* Paragraph p1 = new Paragraph(title);
                    p1.setBold();
                    p1.setFontSize(14);
                    doc.add(p1);
                    // Adding area break to the PDF
                    doc.add(p1); */
                }
        // Table
                if(row != null && row.getNo() % 100 != 0) {
        // Column 1 - Item Position No.
                    int l = String.valueOf(row.getNo()).length();
                    Cell c1 = Util.setCell(String.valueOf(row.getNo()).substring(l-2,l)
                            .replaceFirst("^0+(?!$)", ""),condition,TextAlignment.CENTER, false);
                    table.addCell(c1);
        // Column 2 - ReplacementPart New/Existing
                    Cell c2 = null;
                    if(row.getEtkz() == 'N') {
                        c2 = Util.setCell("N",condition,TextAlignment.CENTER, false);
                    } else  c2 = Util.setCell("",condition,TextAlignment.CENTER, false);
                    table.addCell(c2);
        // Column 3 - Material No
                    Cell c3 = Util.setCell(row.getPartno(),condition,TextAlignment.CENTER, false);
                    table.addCell(c3);
        // Column 4 - Description
                    String descr = row.getDescription();
                    if (!row.getDescriptionLine2().isEmpty()){
                        descr = row.getDescription() + "\r\n" +row.getDescriptionLine2();
                    }
                    if (!row.getDescriptionLine3().isEmpty()){
                        descr = row.getDescription() + "\r\n" +row.getDescriptionLine3();
                    }
                    Cell c4 = Util.setCell(descr,condition,TextAlignment.LEFT, false);
                    table.addCell(c4);
        // Column 5 - Quantity
                    Cell c5 = null;
                    if (row.getQty() == (float) row.getQty()) {
                        c5 = Util.setCell(String.format("%.0f",row.getQty()),condition,TextAlignment.LEFT, false);
                    }else{
                        c5 = Util.setCell(String.format("%s", row.getQty()),condition,TextAlignment.LEFT, false);
                    }
                    table.addCell(c5);
        // Column 6 - ChangeNo
                    Cell c6 = Util.setCell(row.getChangeno(),condition,TextAlignment.LEFT, false);
                    table.addCell(c6);
                }
            }
            boolean b = true;
            int i = doc.getPdfDocument().getNumberOfPages();

            // Adding Table to document
            doc.add(table);

            if(doc.getPdfDocument().getNumberOfPages() % 2 != 0){
                doc.add(new AreaBreak());
            }
            doc.close();
            String dest = stampChapter(title, rawFile);
            // Closing the document
            return dest;
        }
        return null;
    }

    private String stampChapter(String chapter, String rawFile) throws IOException, DocumentException {
        PdfReader readerFinal = new PdfReader(rawFile);
        String dest = rawFile.replaceAll("raw", "");
        Util.setTmpFiles(dest);
        PdfStamper stamp = new PdfStamper(readerFinal, new FileOutputStream(dest));
        PdfContentByte over;

        int totalPages = readerFinal.getNumberOfPages();
        for (int i = 1; i <= totalPages; i++) {
            over = stamp.getOverContent(i);
            ColumnText.showTextAligned(over, Element.ALIGN_LEFT,
                    new Phrase(chapter, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)),
                    55, PageSize.A4.getHeight() -40, 0);
        }
        stamp.close();
        return dest;
    }
}
