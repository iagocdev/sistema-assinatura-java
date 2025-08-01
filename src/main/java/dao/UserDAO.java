package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import config.DatabaseConnection;
import model.User;
import java.sql.ResultSet;
import java.util.Optional;

public class UserDAO {

    /**
     * Método para salvar um novo usuário no banco de dados.
     * @param user O objeto User contendo os dados a serem salvos.
     */
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário no banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    } // <<--- A CHAVE DO MÉTODO 'save' FECHA AQUI

    /**
     * Busca um usuário no banco de dados pelo seu e-mail.
     * @param email O e-mail do usuário a ser buscado.
     * @return um Optional contendo o User se encontrado, ou um Optional vazio caso contrário.
     */
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    
                    return Optional.of(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por e-mail: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    } // <<--- A CHAVE DO MÉTODO 'findByEmail' FECHA AQUI
    
} // <<--- A CHAVE DA CLASSE 'UserDAO' FECHA AQUI