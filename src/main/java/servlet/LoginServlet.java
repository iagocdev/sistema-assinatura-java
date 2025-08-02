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

        UserDAO userDAO = new UserDAO();
        Optional<User> userOptional = userDAO.findByEmail(emailParaLogin);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("2. Usuário encontrado no banco: " + user.getName());
            
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            
            boolean passwordsMatch = passwordEncoder.matches(senhaParaLogin, user.getPasswordHash());
           
            
            if (passwordsMatch) {
                System.out.println("5. Senhas correspondem! Criando sessão e redirecionando para o dashboard.");
                
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60);
                
                String token = JwtUtil.generateToken(user);
                
                response.sendRedirect("dashboard.html");
                
            } else {
                response.sendRedirect("login.html?error=password");
            }
            
        } else {
         
            response.sendRedirect("login.html?error=user");
        }
    }
}