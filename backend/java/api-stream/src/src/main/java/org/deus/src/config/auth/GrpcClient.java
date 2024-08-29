package org.deus.src.config.auth;

import auth.Auth;
import auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.deus.src.dtos.fromModels.UserDTO;
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

    public UserDTO validateToken(String token) {
        try {
            Auth.ValidateTokenRequest request = Auth.ValidateTokenRequest.newBuilder()
                    .setToken(token)
                    .build();
            Auth.ValidateTokenResponse response = stub.validateToken(request);
            if (response.getValid()) {
                Auth.UserDTO grpcUserDTO = response.getUserDTO();
                if (grpcUserDTO != null) {
                    return new UserDTO(grpcUserDTO.getId(), grpcUserDTO.getEmail(), grpcUserDTO.getUsername());
                }
                else {
                    logger.info("grpcUserDTO is null");
                }
            } else {
                logger.info("JWT token is not valid");
            }
            return null;
        } catch (Exception e) {
            logger.error("Something went wrong while validating token", e);
            return null;
        }
    }
}
