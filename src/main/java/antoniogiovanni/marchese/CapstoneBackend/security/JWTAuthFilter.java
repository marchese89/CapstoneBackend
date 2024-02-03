package antoniogiovanni.marchese.CapstoneBackend.security;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    // Creo un filtro che andrò ad aggiungere alla Security Filter Chain

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // doFilterInternal è il metodo di questo filtro che verrà eseguito per ogni richiesta che richieda di essere autenticati, e dovrà:

        // 1. Verifichiamo se la richiesta contiene un Authorization Header ed eventualmente estraiamo il token da esso
        String authHeader = request.getHeader("Authorization"); // "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjOTY0YjQwYy1iMzliLTQ4NzYtYTBkYi1jNDA4Yjc5ZDlhNDUiLCJpYXQiOjE3MDU5MjIyMjYsImV4cCI6MTcwNjUyNzAyNn0.stxc0Bko-lN8ej_Yp8hpjLTQmmlnzqJChrDQ7XkAR0Q"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Per favore metti il token nell'Authorization header");
        } else {
            String accessToken = authHeader.substring(7);

            // 2. Verifichiamo se il token è scaduto o se è stato manipolato (verifica signature)
            jwtTools.verifyToken(accessToken);

            // 3. Se è tutto OK

            // 3.1 Cerco l'utente a DB (l'id sta all'interno del token...)
            String id = jwtTools.extractIdFromToken(accessToken); // L'id è nel token quindi devo estrarlo da lì
            User user = userService.findById(UUID.fromString(id));

            // 3.2 Informo Spring Security che l'utente è autenticato (se non faccio questo passaggio continuerò ad avere 403 come risposte)
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3.3 Possiamo proseguire al prossimo elemento della chain (e prima o poi si arriverà al controller)
            filterChain.doFilter(request, response); // va al prossimo elemento della catena
            // 4. Se non è OK --> 401
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
