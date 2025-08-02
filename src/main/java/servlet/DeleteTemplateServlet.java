package servlet;

import java.io.IOException;
import dao.DocumentTemplateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DeleteTemplateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Pega o ID do template que veio do formulário/requisição.
            int templateId = Integer.parseInt(request.getParameter("templateId"));
            
            // Instancia o DAO e chama o método para deletar.
            DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
            templateDAO.delete(templateId);
            
            // Envia uma resposta de sucesso.
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Template deletado com sucesso\"}");
            
        } catch (NumberFormatException e) {
            // Se o ID não for um número válido, retorna um erro.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"ID do template inválido\"}");
        }
    }
}