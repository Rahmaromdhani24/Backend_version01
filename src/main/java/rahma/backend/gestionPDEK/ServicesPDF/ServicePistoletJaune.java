package rahma.backend.gestionPDEK.ServicesPDF;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ServicePistoletJaune {
    public byte[] generatePdf() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Création du PdfWriter et du PdfDocument
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        try {
            // Chargement des polices
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // Titre
            Paragraph title = new Paragraph("Carte d'enregistrement des données de processus")
                    .setFont(boldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Informations générales
            document.add(new Paragraph("Type d'opération : Mesure de vissage de la force de serrage").setFont(normalFont));
            document.add(new Paragraph("Instrument de mesure : Machine de traction M/V avec Outil KBP-G528").setFont(normalFont));
            document.add(new Paragraph("Spécification de mesure : 40 N (Jaune)").setFont(normalFont));
            document.add(new Paragraph("Limite d'intervention : min. 34 N / max. 46 N").setFont(normalFont));
            document.add(new Paragraph("Numéro du pistolet : 212").setFont(normalFont));
            document.add(new Paragraph("Segment / Projet : 64 - 5608").setFont(normalFont));
            document.add(new Paragraph("\n"));

            // Tableau
            float[] columnWidths = {1, 2, 2, 2, 2};
            Table table = new Table(columnWidths);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setWidth(500); // Largeur fixe ou utiliser setWidthPercent(100)

            // En-têtes
            table.addHeaderCell(createCell("N°", boldFont, true));
            table.addHeaderCell(createCell("N° de collier", boldFont, true));
            table.addHeaderCell(createCell("Valeur mesurée", boldFont, true));
            table.addHeaderCell(createCell("Semaine", boldFont, true));
            table.addHeaderCell(createCell("Décision", boldFont, true));

            // Remplissage avec des données fictives
            for (int i = 1; i <= 10; i++) {
                table.addCell(createCell(String.valueOf(i), normalFont, false));
                table.addCell(createCell("1X3" + i, normalFont, false));
                table.addCell(createCell((35 + i) + " N", normalFont, false));
                table.addCell(createCell("14", normalFont, false));
                table.addCell(createCell("OK", normalFont, false));
            }

            document.add(table);
            
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    private Cell createCell(String content, PdfFont font, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content).setFont(font));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBold();
        }
        return cell;
    }
}