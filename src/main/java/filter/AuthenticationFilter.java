package filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Pega a sessão atual. 'false' significa que não cria uma nova se não existir.
        HttpSession session = httpRequest.getSession(false);
        
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        // Se o usuário está logado, permite que a requisição continue.
        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            // Se não está logado, redireciona para a página de login.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }
    }
}