package util;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * PDF Pubisher
 * Created by khoef on 21.01.2019.
 */
public class Util {

    static Color gray = new DeviceRgb(215, 215, 215);
    static Color white = new DeviceRgb(255, 255, 255);

    public static void merge(ArrayList<String> files, String dest) throws IOException, DocumentException {

        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));

        document.open();
        for (String file : files) {
            if (file != null) {
                PdfReader reader = new PdfReader(file);
                PdfReader.unethicalreading = true;
                copy.addDocument(reader);
                copy.freeReader(reader);
                reader.close();

                File f = new File(file);
                // f.delete();
            }
        }
        document.close();

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