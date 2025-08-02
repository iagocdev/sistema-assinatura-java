package servlet;

import java.io.IOException;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.JwtUtil;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	String emailParaLogin = request.getParameter("email").toLowerCase();
        String senhaParaLogin = request.getParameter("password");

        System.out.println("--- INICIANDO TENTATIVA DE LOGIN ---");
        System.out.println("1. Buscando usuário com e-mail: " + emailParaLogin);
        
        UserDAO userDAO = new UserDAO();
        Optional<User> userOptional = userDAO.findByEmail(emailParaLogin);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("2. Usuário encontrado no banco: " + user.getName());
            
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            
            System.out.println("3. Verificando a senha...");
            System.out.println("   - Senha do formulário: " + senhaParaLogin);
            System.out.println("   - Hash do banco: " + user.getPasswordHash());
            
            boolean passwordsMatch = passwordEncoder.matches(senhaParaLogin, user.getPasswordHash());
            System.out.println("4. Resultado da verificação: " + passwordsMatch);
            
            if (passwordsMatch) {
                System.out.println("5. Senhas correspondem! Criando sessão e redirecionando para o dashboard.");
                
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60);
                
                String token = JwtUtil.generateToken(user);
                System.out.println("   - Sessão criada. Token: " + token);
                
                response.sendRedirect("dashboard.html");
                
            } else {
                System.out.println("5. ERRO: Senhas NÃO correspondem.");
                response.sendRedirect("login.html?error=password");
            }
            
        } else {
            System.out.println("2. ERRO: Usuário NÃO encontrado no banco.");
            response.sendRedirect("login.html?error=user");
        }
        System.out.println("--- FIM DA TENTATIVA DE LOGIN ---");
    }
}