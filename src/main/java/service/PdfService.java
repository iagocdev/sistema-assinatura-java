package service;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfService {

    /**
     * Cria um documento PDF simples em memória.
     * @param title O título do documento.
     * @param content O conteúdo do parágrafo principal.
     * @return Um ByteArrayOutputStream contendo os bytes do PDF gerado.
     */
    public ByteArrayOutputStream createSimplePdf(String title, String content) {
        // O ByteArrayOutputStream funciona como um "arquivo em memória".
        // O PDF será escrito nele em vez de em um arquivo físico no disco.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // 1. Cria um objeto de Documento.
        Document document = new Document();
        
        try {
            // 2. Associa o Documento ao nosso "arquivo em memória" (baos).
            PdfWriter.getInstance(document, baos);
            
            // 3. Abre o documento para começar a escrever.
            document.open();
            
            // 4. Adiciona metadados ao PDF (opcional).
            document.addTitle("Documento Gerado pelo DocuFlow");
            document.addAuthor("Sistema DocuFlow");
            
            // 5. Adiciona conteúdo ao documento.
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph(title, titleFont));
            
            document.add(new Paragraph(" ")); // Linha em branco
            
            Font contentFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph(content, contentFont));
            
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            // 6. Fecha o documento. Isso finaliza a escrita no baos.
            if (document.isOpen()) {
                document.close();
            }
        }
        
        return baos;
    }
}