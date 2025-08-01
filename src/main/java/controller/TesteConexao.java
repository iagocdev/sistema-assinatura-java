package controller;

import java.sql.Connection;
import java.sql.SQLException;
import config.DatabaseConnection;

public class TesteConexao {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Conexão com o banco de dados 'docflow' bem-sucedida!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ Falha ao conectar com o banco de dados.");
        }
    }
}