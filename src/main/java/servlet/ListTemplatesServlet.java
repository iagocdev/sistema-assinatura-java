package servlet;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DocumentTemplateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DocumentTemplate;
import model.User;

public class ListTemplatesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("--- DENTRO DO ListTemplatesServlet ---");
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("user") != null) {
            User loggedInUser = (User) session.getAttribute("user");
            System.out.println("Usuário da sessão encontrado: " + loggedInUser.getName());
            System.out.println(">>> ID DO USUÁRIO SENDO USADO NA BUSCA: " + loggedInUser.getId());
            
            DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
            List<DocumentTemplate> templates = templateDAO.findByUserId(loggedInUser.getId());
            System.out.println(">>> Templates encontrados no banco para este ID: " + templates.size());
            
            List<Map<String, Object>> templateData = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                     .withZone(ZoneId.systemDefault());

            for (DocumentTemplate template : templates) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", template.getId());
                data.put("templateName", template.getTemplateName());
                data.put("createdAt", formatter.format(template.getCreatedAt()));
                templateData.add(data);
            }
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonTemplates = mapper.writeValueAsString(templateData);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonTemplates);
            
        } else {
            System.out.println("ERRO: Sessão ou usuário não encontrado no ListTemplatesServlet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Usuário não autenticado\"}");
        }
    }
}