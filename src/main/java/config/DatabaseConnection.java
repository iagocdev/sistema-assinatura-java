package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // ----- LINHA ADICIONADA -----
            // FORÇA O CARREGAMENTO DA CLASSE DO DRIVER POSTGRESQL
            Class.forName("org.postgresql.Driver");
            
            // O resto do código continua igual...
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String user = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");

            if (dbUrl == null || dbUrl.isEmpty()) {
                System.out.println("Variáveis de ambiente não encontradas, usando configuração local.");
                dbUrl = "jdbc:postgresql://localhost:5432/docflow";
                user = "doc_user";
                password = "docflow#$ç"; // Lembre-se de usar sua senha local aqui
            }

            conn = DriverManager.getConnection(dbUrl, user, password);

        } catch (ClassNotFoundException e) {
            System.err.println("Erro CRÍTICO: Driver JDBC do PostgreSQL não foi encontrado no classpath.");
            e.printStackTrace();
            throw new RuntimeException("Erro de driver", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro de conexão com o banco de dados", e);
        }
        return conn;
    }
}