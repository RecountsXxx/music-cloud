package org.deus.src.drivers;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.*;
import org.deus.src.exceptions.StorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class StorageMinioDriver implements StorageDriverInterface {
    private final MinioClient minioClient;
    private final String baseUrl;

    public StorageMinioDriver(MinioClient minioClient, String baseUrl) {
        this.minioClient = minioClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public byte[] getObjectAsBytes(String bucketName, String path) throws StorageException {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(path)
                        .build()))
        {
            return stream.readAllBytes();
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
               ServerException | InsufficientDataException |
               ErrorResponseException | InvalidResponseException |
               XmlParserException | InternalException e) {
            throw new StorageException("Error getting bytes", e);
        }
    }

    @Override
    public InputStream getObjectAsStream(String bucketName, String path) throws StorageException {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
               ServerException | InsufficientDataException |
               ErrorResponseException | InvalidResponseException |
               XmlParserException | InternalException e) {
            throw new StorageException("Error getting InputStream", e);
        }
    }

    @Override
    public void putObject(String bucketName, String path, byte[] bytes) throws StorageException {
        try (ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(bytesStream, bytesStream.available(), -1)
                            .build()
            );
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
               ServerException | InsufficientDataException |
               ErrorResponseException | InvalidResponseException |
               XmlParserException | InternalException e) {
            throw new StorageException("Error putting bytes", e);
        }
    }

    @Override
    public Boolean isObjectExists(String bucketName, String path) throws StorageException {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path).build());
            return true;
        }
        catch (ErrorResponseException e) {
            return false;
        }
        catch (IOException | InvalidKeyException | NoSuchAlgorithmException |
               ServerException | InsufficientDataException | InvalidResponseException |
               XmlParserException | InternalException e) {
            throw new StorageException("Error checking if certain object exists", e);
        }
    }

    @Override
    public String getPublicUrl(String bucketName, String path) {
        return String.format("%s/%s/%s", baseUrl, bucketName, path);
    }
}
