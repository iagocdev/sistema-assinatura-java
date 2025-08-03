package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        // MENSAGEM DE TESTE - VERSÃO MAIS RECENTE
        System.out.println(">>> EXECUTANDO DatabaseConnection - VERSAO CORRIGIDA COM 3 PARAMETROS <<<");
        
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String user = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");

            if (dbUrl == null || dbUrl.isEmpty()) {
                System.out.println("Variáveis de ambiente não encontradas, usando configuração local.");
                dbUrl = "jdbc:postgresql://localhost:5432/docflow";
                user = "doc_user";
                password = "sua_senha_local"; // Lembre-se de usar sua senha local aqui
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