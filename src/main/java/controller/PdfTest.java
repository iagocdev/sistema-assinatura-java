package controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import service.PdfService;

public class PdfTest {

    public static void main(String[] args) {
        // 1. Instancia o nosso serviço de PDF.
        PdfService pdfService = new PdfService();
        
        System.out.println("Gerando PDF de teste...");

        // 2. Chama o método para criar o PDF em memória.
        ByteArrayOutputStream pdfStream = pdfService.createSimplePdf(
            "Meu Primeiro Documento",
            "Este é um documento de teste gerado pelo sistema DocuFlow. " +
            "O conteúdo foi criado dinamicamente usando a biblioteca OpenPDF em Java."
        );
        
        // 3. Salva o conteúdo do "arquivo em memória" em um arquivo físico.
        //    O arquivo será salvo na sua pasta home (ex: /home/iago/documento_teste.pdf).
        String filePath = System.getProperty("user.home") + "/documento_teste.pdf";
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // Escreve os bytes do stream no arquivo de saída.
            fos.write(pdfStream.toByteArray());
            System.out.println("✅ PDF gerado com sucesso em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo PDF.");
            e.printStackTrace();
        }
    }
}