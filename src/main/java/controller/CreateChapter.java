package controller;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.PageSize;
import model.ETLRow;
import util.Localization;
import util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * PDF Publisher
 * Created by khoef on 21.01.2019.
 */
public class CreateChapter {

    public String createChapter(ArrayList<ETLRow> etl, Path tempDir, Localization localization, int scaleFactor)
            throws IOException {
        String title;
        //lade Baugruppenbild
        if (etl.size() > 0 && etl.get(0) != null) {
            String rawFile = tempDir.toString()+"/"+etl.get(0).getNo()+".pdf";
            Document doc = Util.createPdf(rawFile);

            String titleLine1 = null, titleLine2 = null;
            for (ETLRow row : etl) {
                if (row != null && row.getNo() % 100 == 0 && row.getItemType() == 'T') {
                    title = row.getNo() / 100 + "   " + row.getDescriptionLine2();
                    int linebreak = title.indexOf('\r');
                    if(linebreak != -1) {
                        titleLine1 = title.substring(0, linebreak);
                        titleLine2 = title.substring(linebreak);
                    } else titleLine1 = title;
                }
            }

            Paragraph p1 = new Paragraph();
            Text t1 = new Text(titleLine1);
            t1.setFontSize(14);
            t1.setBold();
            p1.add(t1);
            if(titleLine2 != null) {
                Text t2 = new Text(titleLine2);
                t2.setFontSize(12);
                p1.add(t2);
            }
            doc.add(p1);

            String imgPath = etl.get(0).getDescriptionLine2();
            int index = imgPath.lastIndexOf('\\');
            String imgFileName = tempDir + "" + imgPath.substring(index,imgPath.length());
        //    ImageData data = ImageDataFactory.create(imgFileName);


            // Creating an Image object
        //    Image image = new Image(data);
            Image image = getImage(imgFileName, scaleFactor);


            image.scaleToFit(PageSize.A4.getWidth()-120, PageSize.A4.getHeight()-120);
            float x = (PageSize.A4.getWidth() - image.getImageScaledWidth())/2;
            float y = (PageSize.A4.getHeight() - image.getImageScaledHeight())/2;
            image.setFixedPosition(x, y);
            image.setAutoScaleHeight(true);
            image.setAutoScaleWidth(true);

            doc.add(image);
            doc.add(new AreaBreak());

            // Creating a table
            float[] pointColumnWidths = {25F, 10F, 65F, 310F, 30F, 60F};
            Table table = new Table(pointColumnWidths);
            table.setFontSize(10);

            Cell chapterCell = new Cell(1,6);
            chapterCell.setBorder(Border.NO_BORDER);
            chapterCell.add(p1);
            table.addHeaderCell(chapterCell);

            table.addHeaderCell(Util.setCell(localization.getNum(), true,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell(localization.getNeu(), true,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell(localization.getPart(), true,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell(localization.getDescr(), true,TextAlignment.LEFT, true));
            table.addHeaderCell(Util.setCell(localization.getQty(), true,TextAlignment.CENTER, true));
            table.addHeaderCell(Util.setCell(localization.getChange(), true,TextAlignment.CENTER, true));

            boolean condition = true;
            for (ETLRow row : etl) {
                condition = !condition;
        // Table
                if(row != null && row.getNo() % 100 != 0) {
        // Column 1 - Item Position No.
                    int l = String.valueOf(row.getNo()).length();
                    Cell c1 = Util.setCell(String.valueOf(row.getNo()).substring(l-2,l)
                            .replaceFirst("^0+(?!$)", ""),condition,TextAlignment.CENTER, false);
                    c1.setKeepTogether(true);
                    table.addCell(c1);
        // Column 2 - ReplacementPart New/Existing
                    Cell c2;
                    if(row.getEtkz() == 'N') {
                        c2 = Util.setCell("N",condition,TextAlignment.CENTER, false);
                    } else  c2 = Util.setCell("",condition,TextAlignment.CENTER, false);
                    c2.setKeepTogether(true);
                    table.addCell(c2);
        // Column 3 - Material No
                    if(row.getPartno().isEmpty()) row.setPartno("xxx");
                    Cell c3 = Util.setCell(row.getPartno(),condition,TextAlignment.CENTER, false);
                    c3.setKeepTogether(true);
                    table.addCell(c3);
        // Column 4 - Description
                    String descr = row.getDescription();
                    if(descr.isEmpty()){
                        descr = row.getDescriptionLine2();
                    }else if (!row.getDescriptionLine2().isEmpty()){
                        descr = row.getDescription() + "\r\n" +row.getDescriptionLine2();
                    }
                    Cell c4 = Util.setCell(descr,condition,TextAlignment.LEFT, false);
                    c4.setKeepTogether(true);
                    table.addCell(c4);
        // Column 5 - Quantity
                    Cell c5;
                    if (row.getQty() == (float) row.getQty()) {
                        c5 = Util.setCell(String.format("%.0f",row.getQty()),condition,TextAlignment.CENTER, false);
                    }else{
                        c5 = Util.setCell(String.format("%s", row.getQty()),condition,TextAlignment.CENTER, false);
                    }
                    c5.setKeepTogether(true);
                    table.addCell(c5);
        // Column 6 - ChangeNo
                    Cell c6 = Util.setCell(row.getChangeno(),condition,TextAlignment.CENTER, false);
                    c6.setKeepTogether(true);
                    table.addCell(c6);
                }
            }
            doc.add(table);
            int i = doc.getPdfDocument().getNumberOfPages();
            if(i % 2 != 0){
                doc.add(new AreaBreak());
            }
            doc.close();
        //    String dest = stampChapter(title, rawFile, i);
            return rawFile;
        }
        return null;
    }

    private static Image getImage(String imgFileName, int scaleFactor) throws IOException {
        java.awt.Image awtImage = ImageIO.read(new File(imgFileName));

        // scale image here
        int scaledWidth = awtImage.getWidth(null) * scaleFactor /100;
        int scaledHeight = awtImage.getHeight(null) * scaleFactor / 100;
        BufferedImage scaledAwtImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledAwtImage.createGraphics();
        g.drawImage(awtImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ImageIO.write(scaledAwtImage, "jpeg", bout);
        byte[] imageBytes = bout.toByteArray();

        return new Image(ImageDataFactory.create(imageBytes));
    }
}
