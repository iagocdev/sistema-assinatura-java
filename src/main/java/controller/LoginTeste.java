package controller;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import dao.UserDAO;
import model.User;
import util.JwtUtil;

public class LoginTeste {

    public static void main(String[] args) {
        // --- DADOS DE ENTRADA (Simulando um formulário de login) ---
        String emailParaLogin = "iagocdev@gmail.com"; // O mesmo e-mail que você cadastrou
        String senhaParaLogin = "senhaSuperSegura123";  // A mesma senha original

        System.out.println("Tentativa de login para o e-mail: " + emailParaLogin);
        
        // 1. Instanciar o DAO para buscar o usuário.
        UserDAO userDAO = new UserDAO();
        
        // 2. Buscar o usuário no banco de dados pelo e-mail.
        Optional<User> userOptional = userDAO.findByEmail(emailParaLogin);

        // 3. Verificar se o usuário foi encontrado.
        if (userOptional.isPresent()) {
            // Se o usuário existe, pegamos o objeto User.
            User user = userOptional.get();
            
            // 4. Instanciar o codificador BCrypt para verificar a senha.
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            
            // 5. Comparar a senha digitada com o hash salvo no banco.
            //    O método .matches() faz a comparação segura.
            if (passwordEncoder.matches(senhaParaLogin, user.getPasswordHash())) {
                
                // 6. Se as senhas correspondem, o login é bem-sucedido!
                //    Geramos um token JWT para o usuário.
                String token = JwtUtil.generateToken(user);
                
                System.out.println("✅ Login bem-sucedido!");
                System.out.println("Usuário autenticado: " + user.getName());
                System.out.println("Token JWT gerado: " + token);
                
            } else {
                // Se as senhas não correspondem.
                System.out.println("❌ Senha incorreta.");
            }
            
        } else {
            // Se o e-mail não foi encontrado no banco de dados.
            System.out.println("❌ Usuário não encontrado com o e-mail: " + emailParaLogin);
        }
    }
}