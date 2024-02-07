package antoniogiovanni.marchese.CapstoneBackend.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfService {

    public void createPdfFromHtml(String htmlContent, String outputPath) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        OutputStream outputStream = null;
        try {
            Document document = new Document();
            outputStream = new FileOutputStream(outputPath);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            document.close();
            outputStream.close();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
