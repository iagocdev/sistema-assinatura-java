package dao;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import config.DatabaseConnection;
import model.DocumentTemplate;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

public class DocumentTemplateDAO {

    /**
     * Salva os metadados de um novo template de documento no banco.
     * @param template O objeto DocumentTemplate a ser salvo.
     */
	public void save(DocumentTemplate template) {
	    // Adicionamos a coluna created_at ao INSERT
	    String sql = "INSERT INTO document_templates (template_name, file_path, uploaded_by_user_id, created_at) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, template.getTemplateName());
	        pstmt.setString(2, template.getFilePath());
	        pstmt.setInt(3, template.getUploadedByUserId());
	        // Adicionamos o 4º parâmetro: a data e hora atuais
	        pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

	        pstmt.executeUpdate();

	    } catch (SQLException e) {
	        System.err.println("Erro ao salvar o template de documento no banco: " + e.getMessage());
	        e.printStackTrace();
	    }
	} 
 /**
  * Busca todos os templates de um usuário específico.
  * @param userId O ID do usuário.
  * @return Uma lista de objetos DocumentTemplate.
  */
 public List<DocumentTemplate> findByUserId(int userId) {
     List<DocumentTemplate> templates = new ArrayList<>();
     String sql = "SELECT * FROM document_templates WHERE uploaded_by_user_id = ? ORDER BY created_at DESC";

     try (Connection conn = DatabaseConnection.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
         
         pstmt.setInt(1, userId);
         
         try (ResultSet rs = pstmt.executeQuery()) {
             while (rs.next()) {
                 DocumentTemplate template = new DocumentTemplate();
                 template.setId(rs.getInt("id"));
                 template.setTemplateName(rs.getString("template_name"));
                 template.setFilePath(rs.getString("file_path"));
                 template.setUploadedByUserId(rs.getInt("uploaded_by_user_id"));
                 // Converte o Timestamp do SQL para o Instant do Java
                 template.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                 
                 templates.add(template);
             }
         }
         
     } catch (SQLException e) {
         System.err.println("Erro ao buscar templates por usuário: " + e.getMessage());
         e.printStackTrace();
     }
     
     return templates;
 }
}