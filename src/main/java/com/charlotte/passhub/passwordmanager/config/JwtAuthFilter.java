package com.charlotte.passhub.passwordmanager.config;

import com.charlotte.passhub.passwordmanager.model.User;
import com.charlotte.passhub.passwordmanager.repository.UserRepository;
import com.charlotte.passhub.passwordmanager.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.validateTokenAndGetUserId(token);

            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    // Autenticação manual (sem Spring Security)
                    request.setAttribute("user", user);
                }
            }
        }

        chain.doFilter(request, response);
    }
}

