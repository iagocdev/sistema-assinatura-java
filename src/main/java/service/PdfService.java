package service;

import java.io.ByteArrayOutputStream;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Element; // Adicione este import
import java.time.LocalDateTime; // Adicione este import
import java.time.ZoneId; // Adicione este import
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class PdfService {

    /**
     * Cria um documento PDF simples em memória.
     * @param title O título do documento.
     * @param content O conteúdo do parágrafo principal.
     * @return Um ByteArrayOutputStream contendo os bytes do PDF gerado.
     */
	/**
	 * Cria um documento PDF com uma linha de assinatura.
	 * @param documentTitle O título do documento.
	 * @param signerName O nome da pessoa que está assinando.
	 * @return Um ByteArrayOutputStream contendo os bytes do PDF gerado.
	 */
	public ByteArrayOutputStream createSignedPdf(String documentTitle, String signerName) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Document document = new Document();
	    
	    try {
	        PdfWriter.getInstance(document, baos);
	        document.open();
	        
	        // Conteúdo do documento
	        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
	        document.add(new Paragraph(documentTitle, titleFont));
	        
	        document.add(new Paragraph(" ")); // Espaçamento
	        document.add(new Paragraph("Este documento representa um acordo legal... etc."));
	        document.add(new Paragraph(" "));
	        document.add(new Paragraph(" "));

	        // --- LÓGICA DA ASSINATURA ---
	        // Pega a data e hora atuais
	        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'em' dd/MM/yyyy 'às' HH:mm");
	        String signatureDate = now.format(formatter);
	        
	        // Cria o parágrafo da assinatura
	        String signatureText = "Assinado digitalmente via DocFlow por " + signerName + " " + signatureDate;
	        Font signatureFont = new Font(Font.TIMES_ROMAN, 10, Font.ITALIC);
	        Paragraph signatureParagraph = new Paragraph(signatureText, signatureFont);
	        signatureParagraph.setAlignment(Element.ALIGN_RIGHT); // Alinha à direita
	        
	        document.add(signatureParagraph);
	        // --- FIM DA LÓGICA DA ASSINATURA ---
	        
	    } catch (DocumentException e) {
	        e.printStackTrace();
	    } finally {
	        if (document.isOpen()) {
	            document.close();
	        }
	    }
	    
	    return baos;
	}
	// Dentro da classe PdfService.java

	/**
	 * Lê um PDF existente e adiciona um "carimbo" de assinatura no final.
	 * @param originalFilePath O caminho do arquivo PDF original no servidor.
	 * @param signerName O nome do usuário que está assinando.
	 * @return Um ByteArrayOutputStream contendo os bytes do PDF modificado.
	 */
	public ByteArrayOutputStream stampSignatureOnPdf(String originalFilePath, String signerName) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	        // 1. Cria um "leitor" para o arquivo PDF original que está no disco.
	        PdfReader reader = new PdfReader(originalFilePath);
	        
	        // 2. Cria um "carimbador" (Stamper), que vai escrever as modificações
	        //    no nosso "arquivo em memória" (baos).
	        PdfStamper stamper = new PdfStamper(reader, baos);
	        
	        // 3. Pega a última página do documento para adicionar o conteúdo.
	        int lastPage = reader.getNumberOfPages();
	        PdfContentByte content = stamper.getOverContent(lastPage);
	        
	        // 4. Prepara o texto da assinatura com a data e hora atuais.
	        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'em' dd/MM/yyyy 'às' HH:mm");
	        String signatureDate = now.format(formatter);
	        String signatureText = "Assinado digitalmente via DocuFlow por " + signerName + " " + signatureDate;
	        
	        // 5. Adiciona o texto no rodapé da última página.
	        //    As coordenadas (x, y) definem a posição. (0,0) é o canto inferior esquerdo.
	        Font font = new Font(Font.HELVETICA, 8, Font.ITALIC);
	        ColumnText.showTextAligned(content, 
	                                   Element.ALIGN_RIGHT,      // Alinhamento
	                                   new Phrase(signatureText, font), // Texto e fonte
	                                   550, 30, 0);             // Posição: x=550, y=30, rotação=0

	        // 6. Fecha o stamper e o reader para finalizar a modificação.
	        stamper.close();
	        reader.close();
	        
	    } catch (Exception e) {
	        // Em um app real, teríamos um tratamento de erro melhor aqui.
	        e.printStackTrace();
	    }
	    return baos;
	}
}
	
	
	