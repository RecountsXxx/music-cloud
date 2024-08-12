package org.deus.src.services.storage;

import lombok.AllArgsConstructor;
import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.exceptions.StorageException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.exceptions.data.DataSavingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StorageTempService {
    private final StorageDriverInterface storage;
    private static final Logger logger = LoggerFactory.getLogger(StorageTempService.class);
    private final String bucketName = "temp-files";

    private String buildPath(String userId, String fileId) {
        return "/" + userId + "/" + fileId + "/originalBytes";
    }

    public void putOriginalBytes(String userId, String fileId, byte[] originalBytes) throws DataSavingException {
        try {
            this.storage.put(bucketName, buildPath(userId, fileId), originalBytes);
        } catch (StorageException e) {
            String errorMessage = "Error while putting original bytes to store, bucket/container: \"" + bucketName + "\"";
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    public Optional<byte[]> getOriginalBytes(String userId, String fileId) {
        try {
            byte[] bytes = storage.getBytes(bucketName, buildPath(userId, fileId));
            return Optional.ofNullable(bytes);
        }
        catch (StorageException e) {
            logger.error("Error while getting original bytes from store, bucket/container: \"" + bucketName + "\"", e);
            return Optional.empty();
        }
    }

    public Boolean isFileExists(String userId, String fileId) throws DataProcessingException {
        try {
            return this.storage.isFileExists(bucketName, buildPath(userId, fileId));
        } catch (StorageException e) {
            String errorMessage = "Error while checking if file exists in storage";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }
}