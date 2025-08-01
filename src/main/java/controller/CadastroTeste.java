package controller;

//Importa as classes 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import dao.UserDAO;
import model.User;

public class CadastroTeste {

 public static void main(String[] args) {
     
     // 1. Instancia o codificador de senhas que adicionamos ao projeto.
     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     // 2. Cria um novo objeto de usuário para o nosso teste.
     User newUser = new User();
     newUser.setName("Iago Desenvolvedor");
     newUser.setEmail("iagocdev@gmail.com"); // Use um email diferente se já tiver cadastrado este

     // 3. Define uma senha simples que o usuário digitaria.
     String plainPassword = "senhaSuperSegura123";
     
     // 4. Usa o codificador para criar o HASH da senha. É este hash que será salvo.
     //    O BCrypt gera um "salt" aleatório a cada vez, por isso o hash será 
     //    diferente a cada execução, mesmo para a mesma senha. Isso é ótimo para a segurança.
     String hashedPassword = passwordEncoder.encode(plainPassword);
     
     // 5. Armazena o HASH no nosso objeto de usuário (NUNCA a senha original).
     newUser.setPasswordHash(hashedPassword);

     // 6. Instancia o nosso DAO para poder interagir com o banco de dados.
     UserDAO userDAO = new UserDAO();

     // 7. Chama o método 'save' para persistir o objeto 'newUser' na tabela 'users'.
     userDAO.save(newUser);
     
     System.out.println("🎉 Usuário cadastrado com sucesso!");
     System.out.println("------------------------------------");
     System.out.println("Nome: " + newUser.getName());
     System.out.println("E-mail: " + newUser.getEmail());
     System.out.println("Senha original: " + plainPassword);
     System.out.println("Senha hasheada (salva no banco): " + hashedPassword);
 }
}