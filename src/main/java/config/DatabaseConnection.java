package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            
            // Lemos as três variáveis de ambiente separadamente
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String user = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");

            // Lógica para ambiente local continua a mesma
            if (dbUrl == null || dbUrl.isEmpty()) {
                System.out.println("Variáveis de ambiente não encontradas, usando configuração local.");
                dbUrl = "jdbc:postgresql://localhost:5432/docflow";
                user = "doc_user"; // Seu usuário local
                password = "sua_senha_local"; // Sua senha local
            }

            // A MUDANÇA ESTÁ AQUI: Passamos a URL, o usuário e a senha como
            // argumentos separados para o DriverManager.
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