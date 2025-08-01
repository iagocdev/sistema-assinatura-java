package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/docflow";
    private static final String USER = "doc_user";
    private static final String PASSWORD = "docflow#$ç"; // ⚠️ TROQUE AQUI!

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do PostgreSQL não encontrado no Build Path!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados. Verifique a URL, usuário e senha.");
            e.printStackTrace();
        }
        return conn;
    }
}