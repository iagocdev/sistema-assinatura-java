package servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import dao.DocumentTemplateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DocumentTemplate;

public class PreviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String templateIdStr = request.getParameter("id");
        if (templateIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do template não fornecido.");
            return;
        }

        try {
            int templateId = Integer.parseInt(templateIdStr);
            DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
            Optional<DocumentTemplate> templateOpt = templateDAO.findById(templateId);

            if (templateOpt.isPresent()) {
                DocumentTemplate template = templateOpt.get();
                File file = new File(template.getFilePath());

                if (file.exists()) {
                    // Define o tipo de conteúdo (MIME type). O navegador usa isso para decidir como abrir.
                    // Estamos simplificando para application/pdf, mas poderia ser dinâmico.
                    response.setContentType("application/pdf"); 
                    // "inline" instrui o navegador a tentar ABRIR o arquivo, em vez de baixar.
                    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                    response.setContentLength((int) file.length());

                    try (FileInputStream in = new FileInputStream(file);
                         OutputStream out = response.getOutputStream()) {
                        
                        byte[] buffer = new byte[4096];
                        int bytesRead = -1;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Arquivo não encontrado no servidor.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template não encontrado no banco de dados.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do template inválido.");
        }
    }
}