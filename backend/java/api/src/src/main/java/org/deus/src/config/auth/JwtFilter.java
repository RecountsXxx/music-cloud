package org.deus.src.config.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final GrpcClient grpcClient;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/java/public/")) {
            // allowing requests from outside and internal requests from other microservices inside of system
            filterChain.doFilter(request, response);
            return;
        } else if (path.startsWith("/api/java/protected/")) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                try {
                    UserDTO userDTO = grpcClient.validateToken(token);

                    if (userDTO == null) {
                        throw new RuntimeException();
                    }

                    request.setAttribute("userDTO", userDTO);
                    filterChain.doFilter(request, response);
                    return;
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    logger.error("Invalid JWT token", e);
                    return;
                }
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
            logger.error("Missing Authorization header");
            return;
        }

        response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "The request was made to an unexpected or unavailable path.");
        logger.error("The request was made to an unexpected or unavailable path.");
    }
}
