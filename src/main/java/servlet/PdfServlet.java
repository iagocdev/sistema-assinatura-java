package servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import dao.DocumentTemplateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DocumentTemplate;
import model.User;
import service.PdfService;

public class PdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String templateIdStr = request.getParameter("id");

        if (session == null || session.getAttribute("user") == null || templateIdStr == null) {
            response.sendRedirect("login.html");
            return;
        }

        User loggedInUser = (User) session.getAttribute("user");
        int templateId = Integer.parseInt(templateIdStr);

        DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
        Optional<DocumentTemplate> templateOpt = templateDAO.findById(templateId);

        if (templateOpt.isPresent()) {
            DocumentTemplate template = templateOpt.get();
            
            PdfService pdfService = new PdfService();
            
            // ### A MUDANÇA ESTÁ AQUI ###
            // Chamamos o novo método, passando o caminho do arquivo original e o nome do usuário.
            ByteArrayOutputStream pdfStream = pdfService.stampSignatureOnPdf(template.getFilePath(), loggedInUser.getName());
            
            // Configura a resposta HTTP para o download (o resto continua igual)
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + template.getTemplateName() + "_assinado.pdf\"");
            response.setContentLength(pdfStream.size());
            
            try (OutputStream out = response.getOutputStream()) {
                pdfStream.writeTo(out);
                out.flush();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template não encontrado.");
        }
    }
 }