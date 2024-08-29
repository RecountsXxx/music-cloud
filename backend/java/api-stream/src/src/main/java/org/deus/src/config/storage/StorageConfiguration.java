package org.deus.src.config.storage;

import io.minio.MinioClient;
import org.deus.src.config.storage.properties.MinioStorageProperties;
import org.deus.src.config.storage.properties.S3StorageProperties;
import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.drivers.StorageMinioDriver;
import org.deus.src.drivers.StorageS3Driver;
import org.deus.src.enums.StorageEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableConfigurationProperties({MinioStorageProperties.class, S3StorageProperties.class})
public class StorageConfiguration {
    @Value("${storage.main}")
    private StorageEnum currentMainStorage;

    @Bean
    public StorageDriverInterface storageDriverInterface(MinioStorageProperties minioStorageProperties, S3StorageProperties s3StorageProperties) {
        StorageDriverInterface storageClient = null;

        switch (currentMainStorage) {
            case MINIO -> {
                MinioClient client =
                        MinioClient.builder()
                                .endpoint(minioStorageProperties.getEndpoint())
                                .credentials(minioStorageProperties.getAccessKey(), minioStorageProperties.getSecretKey())
                                .build();

                storageClient = new StorageMinioDriver(client, minioStorageProperties.getPublicUrl());
            }

            case S3 -> {
                S3Client client = S3Client.builder()
                        .region(Region.of(s3StorageProperties.getRegion()))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(s3StorageProperties.getAccessKey(), s3StorageProperties.getSecretKey())))
                        .build();

                storageClient = new StorageS3Driver(client, s3StorageProperties.getEndpoint());
            }
        }

        return storageClient;
    }
}
