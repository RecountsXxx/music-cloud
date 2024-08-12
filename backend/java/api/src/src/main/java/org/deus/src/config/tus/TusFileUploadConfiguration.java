package org.deus.src.config.tus;

import me.desair.tus.server.TusFileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TusFileUploadConfiguration {
    @Value("${app.tus.upload-directory}")
    private String uploadDirectory;
    @Value("${app.tus.upload-uri}")
    private String uploadUri;

    @Bean
    public TusFileUploadService tusFileUploadService() {
        return new TusFileUploadService().withStoragePath(uploadDirectory)
                .withUploadUri(uploadUri);
    }
}