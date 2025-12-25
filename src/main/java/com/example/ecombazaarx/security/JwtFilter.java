package com.example.ecombazaarx.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// import org.hibernate.mapping.List;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");
                if(authHeader != null && authHeader.startsWith("Bearer")) {
                    String token = authHeader.substring(7);
                    String username = jwtUtil.extractUsername(token);

                    if(username != null) {
                       UsernamePasswordAuthenticationToken authetication = new UsernamePasswordAuthenticationToken(username,null,List.of(() -> "ROLE_USER"));
                       SecurityContextHolder.getContext().setAuthentication(authetication);
                    }
                }
                        chain.doFilter(request, response);
    }
}

