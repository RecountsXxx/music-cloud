package org.deus.src.drivers;

import org.deus.src.exceptions.StorageException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

public class StorageS3Driver implements StorageDriverInterface {
    private final S3Client s3Client;
    private final String baseUrl;

    public StorageS3Driver(S3Client s3Client, String endpoint) {
        this.s3Client = s3Client;
        this.baseUrl = endpoint;
    }

    @Override
    public byte[] getObjectAsBytes(String bucketName, String path) throws StorageException {
        try (InputStream stream = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build())) {
            return stream.readAllBytes();
        } catch (AwsServiceException | SdkClientException | IOException e) {
            throw new StorageException("Error getting bytes", e);
        }
    }

    @Override
    public InputStream getObjectAsStream(String bucketName, String path) throws StorageException {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build());
        } catch (AwsServiceException | SdkClientException e) {
            throw new StorageException("Error getting InputStream", e);
        }
    }

    @Override
    public void putObject(String bucketName, String path, byte[] bytes) throws StorageException {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(path)
                            .build(),
                    RequestBody.fromBytes(bytes));
        } catch (AwsServiceException | SdkClientException e) {
            throw new StorageException("Error putting bytes", e);
        }
    }

    @Override
    public Boolean isObjectExists(String bucketName, String path) throws StorageException {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path).build());
            return true;
        } catch (AwsServiceException | SdkClientException e) {
            throw new StorageException("Error checking if certain object exists", e);
        }
    }

    @Override
    public String getPublicUrl(String bucketName, String path) {
        return String.format("%s/%s/%s", baseUrl, bucketName, path);
    }
}
