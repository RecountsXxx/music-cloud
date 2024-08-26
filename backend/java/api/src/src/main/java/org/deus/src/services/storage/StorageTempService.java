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

/**
 * Service for temporary files bucket storage operations.
 */
@Service
@AllArgsConstructor
public class StorageTempService {
    // Interface for interacting with the storage driver
    private final StorageDriverInterface storage;

    // Logger for recording messages
    private static final Logger logger = LoggerFactory.getLogger(StorageTempService.class);

    // Name of the bucket used for temporary files
    private final String bucketName = "temp-files";

    /**
     * Builds a path for storing a file based on user ID and file ID.
     *
     * @param userId The ID of the user who uploaded the file.
     * @param fileId The ID of the file.
     * @return The path to the file in the storage system.
     */
    private String buildPath(String userId, String fileId) {
        return "/" + userId + "/" + fileId + "/originalBytes";
    }

    /**
     * Stores the original bytes of a file in temporary files bucket storage.
     *
     * @param userId The ID of the user who uploaded the file.
     * @param fileId The ID of the file.
     * @param originalBytes The byte array containing the original content of the file.
     * @throws DataSavingException If an error occurs while saving the file.
     */
    public void putOriginalBytes(String userId, String fileId, byte[] originalBytes) throws DataSavingException {
        try {
            this.storage.putObject(bucketName, buildPath(userId, fileId), originalBytes);
        } catch (StorageException e) {
            String errorMessage = "Error while putting original bytes to store, bucket/container: \"" + bucketName + "\"";
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    /**
     * Retrieves the original bytes of a file from temporary files bucket storage.
     *
     * @param userId The ID of the user who uploaded the file.
     * @param fileId The ID of the file.
     * @return An Optional containing the byte array of the file if it exists, or empty if not.
     */
    public Optional<byte[]> getOriginalBytes(String userId, String fileId) {
        try {
            byte[] bytes = storage.getObjectAsBytes(bucketName, buildPath(userId, fileId));
            return Optional.ofNullable(bytes);
        }
        catch (StorageException e) {
            logger.error("Error while getting original bytes from store, bucket/container: \"" + bucketName + "\"", e);
            return Optional.empty();
        }
    }

    /**
     * Checks if a file exists in temporary files bucket storage.
     *
     * @param userId The ID of the user who uploaded the file.
     * @param fileId The ID of the file.
     * @return True if the file exists, False otherwise.
     * @throws DataProcessingException If an error occurs while checking for the file.
     */
    public Boolean isFileExists(String userId, String fileId) throws DataProcessingException {
        try {
            return this.storage.isObjectExists(bucketName, buildPath(userId, fileId));
        } catch (StorageException e) {
            String errorMessage = "Error while checking if file exists in storage";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }
}