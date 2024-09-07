package org.deus.src.config.auth;

import auth.Auth;
import auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.responses.grpc.TokenValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcClient {
    private final AuthServiceGrpc.AuthServiceBlockingStub stub;

    private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);

    public GrpcClient(@Value("${grpc.server.host}") String host, @Value("${grpc.server.port.for.client}") int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = AuthServiceGrpc.newBlockingStub(channel);

        if (stub == null) {
            logger.error("Something went wrong while connecting to GRPC");
        }
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            Auth.ValidateTokenRequest request = Auth.ValidateTokenRequest.newBuilder()
                    .setToken(token)
                    .build();
            Auth.ValidateTokenResponse response = stub.validateToken(request);

            String tokenStatus = response.getTokenStatus();
            Auth.UserDTO grpcUserDTO = response.getUserDTO();

            switch (tokenStatus) {
                case "VALID_TOKEN" -> {
                    return new TokenValidationResponse(tokenStatus, new UserDTO(grpcUserDTO.getId(), grpcUserDTO.getEmail(), grpcUserDTO.getUsername()));
                }
                case "EXPIRED_TOKEN" -> {
                    UserDTO userDTO = null;

                    if (grpcUserDTO != null) {
                        userDTO = new UserDTO(grpcUserDTO.getId(), grpcUserDTO.getEmail(), grpcUserDTO.getUsername());
                    }

                    logger.info("Authorization token is expired");
                    return new TokenValidationResponse(tokenStatus, userDTO);
                }
                case "INVALID_TOKEN" -> {
                    logger.info("Authorization token is invalid");
                    return new TokenValidationResponse(tokenStatus, null);
                }
                case "ERROR_TOKEN" -> {
                    logger.info("Authorization token could not be validated because of unexpected problems");
                    return new TokenValidationResponse(tokenStatus, null);
                }
            }

            return null;
        } catch (Exception e) {
            logger.error("Something went wrong while validating token", e);
            return null;
        }
    }
}
