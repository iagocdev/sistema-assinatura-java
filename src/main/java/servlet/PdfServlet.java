package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PdfService;

// Este servlet vai "escutar" na URL "/download-pdf"
//@WebServlet("/download-pdf")//
public class PdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Usamos o método doGet, pois o usuário vai "pedir" (GET) um arquivo.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Instancia o nosso serviço de geração de PDF.
        PdfService pdfService = new PdfService();
        
        // 2. Chama o serviço para criar o PDF em memória.
        //    (Estamos usando dados fixos por enquanto, mas no futuro viriam do banco)
        ByteArrayOutputStream pdfStream = pdfService.createSimplePdf(
            "Documento Oficial",
            "Este é o conteúdo do documento solicitado para download."
        );
        
        // 3. Configura a Resposta HTTP - ESTA É A PARTE MAIS IMPORTANTE
        
        // Define o tipo de conteúdo como um PDF. Isso diz ao navegador
        // que ele está recebendo um arquivo PDF, e não uma página HTML.
        response.setContentType("application/pdf");
        
        // Define o cabeçalho "Content-Disposition". "attachment" força o navegador
        // a abrir a caixa de diálogo "Salvar como...".
        // "filename=..." é o nome padrão do arquivo que será sugerido.
        response.setHeader("Content-Disposition", "attachment; filename=\"documento_docuflow.pdf\"");
        
        // Define o tamanho da resposta.
        response.setContentLength(pdfStream.size());
        
        // 4. Escreve os bytes do PDF na resposta que será enviada ao navegador.
        try (OutputStream out = response.getOutputStream()) {
            pdfStream.writeTo(out);
            out.flush();
        }
    }
}