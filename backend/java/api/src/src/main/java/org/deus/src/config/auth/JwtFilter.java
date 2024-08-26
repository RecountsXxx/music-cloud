package org.deus.src.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final
    GrpcClient grpcClient;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException
    {
        String path = request.getRequestURI();

        if (path.startsWith("/api/java/public/")) {
            // allowing public requests from outside
            filterChain.doFilter(request, response);
        } else if (path.startsWith("/api/java/protected/")) {
            String header = request.getHeader("Authorization");
            String tokenFromParam = request.getParameter("token"); // Check for token in path parameter

            // Check for token in either header or path parameter
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                processToken(token, request, response, filterChain);
            } else if (tokenFromParam != null) {
                processToken(tokenFromParam, request, response, filterChain);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header or token parameter");
                logger.error("Missing Authorization header or token parameter");
            }
        } else {
            response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "The request was made to an unexpected or unavailable path.");
            logger.error("The request was made to an unexpected or unavailable path.");
        }
    }

    private void processToken(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try
        {
            UserDTO userDTO = grpcClient.validateToken(token);

            if (userDTO == null) {
                throw new RuntimeException();
            }

            request.setAttribute("userDTO", userDTO);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            logger.error("Invalid JWT token", e);
        }
    }
}
