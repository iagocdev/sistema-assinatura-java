package util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import model.User;

import java.security.Key;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JwtUtil {

    // Chave secreta para assinar o token. Em uma aplicação real,
    // esta chave deve ser muito mais segura e vir de um arquivo de configuração.
    private static final String SECRET_KEY = "SuaChaveSecretaSuperSeguraParaODocuFlowAssinarTokensJWT";
    
    // Define a chave de assinatura.
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /**
     * Gera um token JWT para um usuário.
     * @param user O usuário para quem o token será gerado.
     * @return Uma string que representa o token JWT.
     */
    public static String generateToken(User user) {
        // Pega o momento atual
        Instant now = Instant.now();
        
        // Define a data de expiração do token (ex: 24 horas a partir de agora)
        Date expirationDate = Date.from(now.plus(24, ChronoUnit.HOURS));

        // Constrói o token JWT
        return Jwts.builder()
                // Define o "assunto" do token, geralmente o ID ou e-mail do usuário.
                .subject(user.getEmail())
                
                // Adiciona "claims" (informações extras) ao token, como o nome do usuário.
                .claim("name", user.getName())
                
                // Define a data de emissão do token (agora).
                .issuedAt(Date.from(now))
                
                // Define a data de expiração.
                .expiration(expirationDate)
                
                // Assina o token com a chave secreta usando o algoritmo HS256.
                .signWith(key)
                
                // Constrói e retorna a string final do token.
                .compact();
    }
}