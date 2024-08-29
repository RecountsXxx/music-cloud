package org.deus.src.services.storage;

import lombok.RequiredArgsConstructor;
import org.deus.src.drivers.StorageDriverInterface;
import org.deus.src.enums.ImageSize;
import org.deus.src.exceptions.StorageException;
import org.deus.src.exceptions.data.DataSavingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class StorageImageServiceBase {
    protected final StorageDriverInterface storage;
    protected final String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(StorageImageServiceBase.class);

    protected abstract String buildPathToOriginalBytes(String id);
    protected abstract String buildPathToFile(String id, ImageSize size);

    public void putOriginalBytes(String id, byte[] bytes) throws DataSavingException {
        try {
            storage.putObject(bucketName, buildPathToOriginalBytes(id), bytes);
        } catch (StorageException e) {
            String errorMessage = "Error while putting original bytes to storage, bucket/container: \"" + bucketName + "\"";
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    public void putNewBytesAsFile(String id, ImageSize size, byte[] bytes) throws DataSavingException {
        try {
            storage.putObject(bucketName, buildPathToFile(id, size), bytes);
        } catch (StorageException e) {
            String errorMessage = "Error while putting bytes to storage as a file, bucket/container: \"" + bucketName + "\"";
            logger.error(errorMessage, e);
            throw new DataSavingException(errorMessage, e);
        }
    }

    public Optional<byte[]> getOriginalBytes(String id) {
        try {
            byte[] bytes = storage.getObjectAsBytes(bucketName, buildPathToOriginalBytes(id));
            return Optional.ofNullable(bytes);
        } catch (StorageException e) {
            logger.error("Error while getting original bytes from storage, bucket/container: \"" + bucketName + "\"", e);
            return Optional.empty();
        }
    }

    public String getPathToFile(String id, ImageSize size) {
        return storage.getPublicUrl(bucketName, buildPathToFile(id, size));
    }
}
