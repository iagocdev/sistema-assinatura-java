package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Pega a sessão atual, se ela existir.
        HttpSession session = request.getSession(false);
        
        // 2. Se a sessão existir, a invalida.
        if (session != null) {
            session.invalidate();
            System.out.println("Sessão invalidada com sucesso.");
        }
        
        // 3. Redireciona o usuário para a página de login com uma mensagem.
        response.sendRedirect("login.html?logout=success");
    }
}