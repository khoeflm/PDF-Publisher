package util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * SemNOTAM Project (User Interface)
 * Created by khoef on 23.01.2019.
 */
public class PdfFooter extends PdfPageEventHelper {
    Phrase[] header = new Phrase[2];
    int pageNum;

    public PdfFooter() {
        super();
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        switch (writer.getPageNumber() % 2) {
            case 0:
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(writer.getPageNumber()), rect.getRight(), rect.getTop(), 0);
                break;
            case 1:
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(writer.getPageNumber()), rect.getLeft(), rect.getTop(), 0);
                break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("Page %d", pageNum)), (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);

        PdfContentByte content = writer.getDirectContent();
    }
}
