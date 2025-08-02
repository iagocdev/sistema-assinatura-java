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
        
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("user") != null) {
            User loggedInUser = (User) session.getAttribute("user");
            
            DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
            List<DocumentTemplate> templates = templateDAO.findByUserId(loggedInUser.getId());
            
            // Vamos criar uma lista de mapas para ter controle sobre o formato dos dados
            List<Map<String, Object>> templateData = new ArrayList<>();
            
            // Define o formato de data que queremos: dd/MM/yyyy
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                     .withZone(ZoneId.systemDefault());

            for (DocumentTemplate template : templates) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", template.getId());
                data.put("templateName", template.getTemplateName());
                
                // Formata a data 'Instant' para a String 'dd/MM/yyyy' antes de enviar
                data.put("createdAt", formatter.format(template.getCreatedAt()));
                
                templateData.add(data);
            }
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonTemplates = mapper.writeValueAsString(templateData);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonTemplates);
            
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Usuário não autenticado\"}");
        }
    }
}