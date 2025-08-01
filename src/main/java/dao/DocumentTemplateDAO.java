package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import config.DatabaseConnection;
import model.DocumentTemplate;

public class DocumentTemplateDAO {

    /**
     * Salva os metadados de um novo template de documento no banco.
     * @param template O objeto DocumentTemplate a ser salvo.
     */
    public void save(DocumentTemplate template) {
        String sql = "INSERT INTO document_templates (template_name, file_path, uploaded_by_user_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, template.getTemplateName());
            pstmt.setString(2, template.getFilePath());
            pstmt.setInt(3, template.getUploadedByUserId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o template de documento no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}