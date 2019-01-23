package controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.PageSize;
import model.ETLRow;
import util.Util;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateChapter {

    public String createChapter(ArrayList<ETLRow> etl) throws FileNotFoundException, MalformedURLException {
        //lade Baugruppenbild
        if (etl.size() > 0 && etl.get(0) != null) {
            String dest = "tmp/"+etl.get(0).getNo()+".pdf";
            Util.setTmpFiles(dest);
            Document doc = Util.createPdf(dest);

            int index = etl.get(0).getPartno().indexOf('-');
            String imgFile = "tmp/"+ etl.get(0).getPartno().substring(0, index)+".jpg";
            ImageData data = ImageDataFactory.create(imgFile);

            // Creating an Image object
            Image image = new Image(data);

            image.scaleToFit(PageSize.A4.getWidth()-120, PageSize.A4.getHeight()-120);
            float x = (PageSize.A4.getWidth() - image.getImageScaledWidth())/2;
            float y = (PageSize.A4.getHeight() - image.getImageScaledHeight())/2;
            image.setFixedPosition(x, y);

            // Creating a table
            float[] pointColumnWidths = {25F, 10F, 65F, 310F, 30F, 60F};
            Table table = new Table(pointColumnWidths);
            // Adding cells to the table

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
                    String title = row.getNo()/100 + "   " + row.getPartno();
                    Paragraph p1 = new Paragraph(title);
                    p1.setBold();
                    p1.setFontSize(14);
                    doc.add(p1);
                    doc.add(image);
                    // Adding area break to the PDF
                    doc.add(new AreaBreak());
                    doc.add(p1);
                }
        // Table
                if(row != null && row.getNo() % 100 != 0) {
        // Column 1 - Item Position No.
                    int l = String.valueOf(row.getNo()).length();
                    Cell c1 = Util.setCell(String.valueOf(row.getNo()).substring(l-2,l),condition,TextAlignment.CENTER, false);
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
                    Cell c4 = Util.setCell(row.getDescription(),condition,TextAlignment.LEFT, false);
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

            // Closing the document
            doc.close();
            return dest;
        }
        return null;
    }
}
