package servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import dao.DocumentTemplateDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.DocumentTemplate;

// A anotação @WebServlet mapeia esta classe para a URL "/upload-template"
//@WebServlet("/upload-template")//
// @MultipartConfig prepara o servlet para receber dados de formulário com arquivos (multipart/form-data)
@MultipartConfig
public class TemplateUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // O caminho para a pasta que criamos para salvar os arquivos.
    // System.getProperty("user.home") pega o caminho da sua pasta de usuário (ex: /home/iago)
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.home") + "/servers/apache-tomcat-10.1.26/docuflow-uploads";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Pega os dados do formulário
        String templateName = request.getParameter("templateName");
        Part filePart = request.getPart("templateFile"); // "templateFile" é o 'name' do input type="file" no HTML
        
        // Extrai o nome original do arquivo
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
        
        // Monta o caminho completo onde o arquivo será salvo
        String filePath = UPLOAD_DIRECTORY + File.separator + fileName;
        File uploadDir = new File(UPLOAD_DIRECTORY);
        
        // Cria o diretório de upload se ele não existir
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 2. Salva o arquivo no disco
        try {
            filePart.write(filePath);
            System.out.println("Arquivo salvo em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
            // Em uma aplicação real, aqui teríamos um tratamento de erro melhor
            response.sendRedirect("dashboard.html?upload=error");
            return;
        }

        // 3. Salva os metadados no banco de dados
        DocumentTemplateDAO templateDAO = new DocumentTemplateDAO();
        DocumentTemplate newTemplate = new DocumentTemplate();
        
        newTemplate.setTemplateName(templateName);
        newTemplate.setFilePath(filePath);
        
        // NOTA: Ainda não temos um sistema de sessão para saber QUEM está logado.
        // Por enquanto, vamos "fingir" que é sempre o usuário com ID = 1.
        // Faremos a integração com o JWT mais tarde.
        int loggedInUserId = 1; // HARDCODED por enquanto
        newTemplate.setUploadedByUserId(loggedInUserId);
        
        templateDAO.save(newTemplate);
        
        System.out.println("Metadados do template salvos no banco de dados.");

        // 4. Redireciona o usuário de volta para o dashboard
        response.sendRedirect("dashboard.html?upload=success");
    }
}