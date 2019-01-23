package util;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * PDF Pubisher
 * Created by khoef on 21.01.2019.
 */
public class Util {

    static Color gray = new DeviceRgb(215, 215, 215);
    static Color white = new DeviceRgb(255, 255, 255);
    static BaseFont baseFont;
    static ArrayList<String> tmpFiles;

    static {
        try {
            baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setTmpFiles(String tmpFiles) {
        if(Util.tmpFiles == null) Util.tmpFiles = new ArrayList<>();
        Util.tmpFiles.add(tmpFiles);
    }


    public static ArrayList<String> getTmpFiles() {
        if(Util.tmpFiles == null) Util.tmpFiles = new ArrayList<>();
        return Util.tmpFiles;
    }

    public static void merge(ArrayList<String> files, String dest, HashMap contentMap)
            throws IOException, DocumentException, ParseException {

        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
        document.open();
        int pageNum = 4;
        for (String file : files) {
            if (file != null) {
                PdfReader reader = new PdfReader(file);
                PdfReader.unethicalreading = true;
                if(contentMap != null) {
                    int indexEnd = file.indexOf(".");
                    int indexStart = file.indexOf("/");
                    contentMap.put(file.substring(indexStart+1, indexEnd), pageNum);
                }
                pageNum = pageNum + reader.getNumberOfPages();
                copy.addDocument(reader);
                copy.freeReader(reader);
                reader.close();
 //               File f = new File(file);
                // f.delete();
            }
        }
        document.close();
        if(contentMap != null) {
            PdfReader readerFinal = new PdfReader("tmp/helper.pdf");
            int totalPages = readerFinal.getNumberOfPages();
            PdfStamper stamp = new PdfStamper(readerFinal, new FileOutputStream("tmp/helper2.pdf"));
            PdfContentByte over;
            for (int i = 1; i <= totalPages; i++) {
                over = stamp.getOverContent(i);
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
                ColumnText.showTextAligned(over, Element.ALIGN_LEFT, new Phrase("ETL"), 35, 35, 0);
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER,
                        new Phrase("Copyright 2018 \u00a9 ROTAX"), PageSize.A4.getWidth() / 2, 35, 0);
                ColumnText.showTextAligned(over, Element.ALIGN_RIGHT,
                        new Phrase("Page: " + String.valueOf(i+3)), PageSize.A4.getWidth() - 35, 35, 0);
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER,
                        new Phrase("This printed Version is not subject to an updating service - " + df.format(new Date()),
                                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8)),
                        PageSize.A4.getWidth() / 2, 25, 0);
            }
            stamp.close();
        }
    }

    public static void removeTmpFiles(){
        for (String f : getTmpFiles()){
            File file = new File(f);
            file.delete();
        }
    }

    public static com.itextpdf.layout.Document createPdf(String dest) throws FileNotFoundException {
        com.itextpdf.kernel.pdf.PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
        return document;
    }

    public static Cell setCell(String s, boolean condition, TextAlignment alignment, boolean bold) {
        Cell c = new Cell();
        Paragraph p = new Paragraph(s);
        c.add(p);
        c.setBackgroundColor(condition ? gray : white);
        c.setTextAlignment(alignment);
        c.setBorder(Border.NO_BORDER);
        if (bold) c.setBold();
        return c;
    }

    public static String parseDate(String changeno) {
        return changeno.substring(5, 7) + "." + changeno.substring(3, 5) + "." + changeno.substring(1, 3);
    }

}