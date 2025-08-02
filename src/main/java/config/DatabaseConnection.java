package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Tenta ler as variáveis de ambiente (para o deploy no Render)
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String user = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");

            // Se as variáveis não existirem, usa os valores locais (para rodar no seu PC)
            if (dbUrl == null || dbUrl.isEmpty()) {
                System.out.println("Variáveis de ambiente não encontradas, usando configuração local.");
                dbUrl = "jdbc:postgresql://localhost:5432/docflow";
                user = "doc_user"; // Seu usuário local
                password = "docflow#$ç"; // Sua senha local
            }

            conn = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro de conexão com o banco de dados", e);
        }
        return conn;
    }
}