package servlet;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import util.JwtUtil;

// A anotação @WebServlet é a forma moderna de mapear uma URL para o seu servlet.
// Qualquer requisição para "/login" será direcionada para esta classe.
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // O método doPost é chamado quando um formulário é enviado com method="POST".
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Pegar os dados que vieram do formulário HTML.
        //    O nome 'email' e 'password' deve ser o mesmo do atributo 'name' nos inputs do HTML.
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 2. Usar nosso DAO para buscar o usuário no banco.
        UserDAO userDAO = new UserDAO();
        Optional<User> userOptional = userDAO.findByEmail(email);
        
        // 3. Lógica de autenticação
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            
            // 4. Compara a senha enviada com o hash salvo no banco.
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                // SUCESSO! Usuário e senha corretos.
                
                // Geramos o token JWT
                String token = JwtUtil.generateToken(user);
                
                // Em uma aplicação real, você guardaria este token (ex: em um cookie)
                // e redirecionaria o usuário para o dashboard.
                // Por enquanto, vamos apenas imprimir uma resposta de sucesso.
                
                System.out.println("Login bem-sucedido para: " + user.getName());
                System.out.println("Token: " + token);
                
                // Redireciona o usuário para a página de dashboard
                response.sendRedirect("frontend/dashboard.html");
                
            } else {
                // Senha incorreta.
                System.out.println("Senha incorreta para o e-mail: " + email);
                response.sendRedirect("frontend/login.html?error=true"); // Redireciona de volta para o login com um erro
            }
        } else {
            // Usuário não encontrado.
            System.out.println("Usuário não encontrado com o e-mail: " + email);
            response.sendRedirect("frontend/login.html?error=true"); // Redireciona de volta para o login com um erro
        }
    }
}