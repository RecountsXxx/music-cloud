package org.deus.src.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.responses.grpc.TokenValidationResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        String errorMessage = "";

        if (path.startsWith("/api/upload/public/")) {
            // allowing public requests from outside
            filterChain.doFilter(request, response);
        } else if (path.startsWith("/api/upload/protected/")) {
            String header = request.getHeader("Authorization");
            String tokenFromParam = request.getParameter("token"); // Check for token in path parameter

            // Check for token in either header or path parameter
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                processToken(token, request, response, filterChain);
            } else if (tokenFromParam != null) {
                processToken(tokenFromParam, request, response, filterChain);
            } else {
                errorMessage = "Missing Authorization header or token parameter";
                logger.error(errorMessage);
                sendErrorResponse(response, errorMessage, HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            errorMessage = "The request was made to an unexpected or unavailable path";
            logger.error(errorMessage);
            sendErrorResponse(response, errorMessage, HttpServletResponse.SC_EXPECTATION_FAILED);
        }
    }

    private void processToken(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try
        {
            TokenValidationResponse tokenValidationResponse = grpcClient.validateToken(token);
            String tokenStatus = tokenValidationResponse.getTokenStatus();
            UserDTO userDTO = tokenValidationResponse.getUserDTO();

            if(tokenStatus.equals("VALID_TOKEN") && userDTO != null) {
                request.setAttribute("userDTO", userDTO);
                filterChain.doFilter(request, response);
            }
            else {
                sendErrorResponse(response, tokenStatus, HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            String errorMessage = "Something went wrong while trying validate token";

            sendErrorResponse(response, errorMessage, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error(errorMessage, e);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", message);
        errorDetails.put("status", statusCode);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
