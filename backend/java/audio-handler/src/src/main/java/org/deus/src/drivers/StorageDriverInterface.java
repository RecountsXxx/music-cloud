package org.deus.src.drivers;

import org.deus.src.exceptions.StorageException;

import java.io.InputStream;

public interface StorageDriverInterface {
    byte[] getObjectAsBytes(String bucketName, String path)
            throws StorageException;

    InputStream getObjectAsStream(String bucketName, String path) throws StorageException;

    void putObject(String bucketName, String path, byte[] bytes)
            throws StorageException;

    Boolean isObjectExists(String bucketName, String path)
            throws StorageException;

    String getPublicUrl(String bucketName, String path);
}
