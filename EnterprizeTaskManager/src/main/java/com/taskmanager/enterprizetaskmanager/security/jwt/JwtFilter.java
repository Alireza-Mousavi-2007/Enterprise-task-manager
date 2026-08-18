package com.taskmanager.enterprizetaskmanager.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtToken jwtToken;

    public JwtFilter(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        doBeforeFilter(request, response);

        filterChain.doFilter(request, response);
    }


    public void doBeforeFilter(HttpServletRequest request, HttpServletResponse response) {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) return;

        var token = header.substring("Bearer".length()).trim();
        try {
            var encoded = jwtToken.tokenVerifier(token);

            List<String> authorities = encoded.getClaim("authorities").asList(String.class);
            // have to learn java 8+
            var simpleAuthorities = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();


            var card = new UsernamePasswordAuthenticationToken(
                    encoded.getSubject(),
                    null,
                    simpleAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(card);


        } catch (JWTVerificationException e) {
            //  throw new JwtVerificationHandler(e.getMessage());
            System.out.println("invalid token : " + e.getMessage());
            SecurityContextHolder.clearContext();
        }
    }
}
