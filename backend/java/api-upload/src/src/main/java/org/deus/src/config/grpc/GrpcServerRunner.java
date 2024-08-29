package org.deus.src.config.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.deus.src.services.grpc.MediaServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrpcServerRunner implements CommandLineRunner {
    private final MediaServiceImpl mediaService;

    @Value("${grpc.server.port}")
    private int grpcServerPort;

    @Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(grpcServerPort)
                .addService(mediaService)
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        server.awaitTermination();
    }
}
