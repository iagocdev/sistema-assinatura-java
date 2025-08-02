package servlet;

import java.io.IOException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Pega os dados do formulário
        String name = request.getParameter("name");
        String email = request.getParameter("email").toLowerCase();
        String password = request.getParameter("password");

        // 2. Criptografa a senha
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        // 3. Cria o objeto User
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);

        // 4. Salva o usuário no banco
        UserDAO userDAO = new UserDAO();
        userDAO.save(newUser);

        // 5. Redireciona para a página de login com uma mensagem de sucesso
        response.sendRedirect("login.html?register=success");
    }
}