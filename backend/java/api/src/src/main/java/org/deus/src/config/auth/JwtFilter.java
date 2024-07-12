package org.deus.src.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/java/public-api/") || path.startsWith("/java/internal-api/")) {
            // allowing requests from outside and internal requests from other microservices inside of system
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body);
            try {
                ResponseEntity<Map> authResponse = restTemplate.exchange(
                        authServiceUrl + "/validate-token", HttpMethod.POST, entity, Map.class);
                if (authResponse.getBody() != null && (Boolean) authResponse.getBody().get("valid")) {
                    request.setAttribute("user", authResponse.getBody().get("user"));
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
    }
}
