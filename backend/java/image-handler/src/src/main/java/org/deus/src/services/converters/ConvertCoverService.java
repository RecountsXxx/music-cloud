package org.deus.src.services.converters;

import lombok.RequiredArgsConstructor;
import org.deus.src.enums.ImageSize;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.exceptions.data.DataSavingException;
import org.deus.src.services.storage.StorageCoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConvertCoverService {
    private final StorageCoverService storageCoverService;
    private final ConverterService converterService;
    private static final Logger logger = LoggerFactory.getLogger(ConvertCoverService.class);

    public void convertCover(String collectionId, ImageSize imageSize, int targetWidth, int targetHeight) throws DataIsNotPresentException, DataProcessingException {
        Optional<byte[]> optionalOriginalBytes = storageCoverService.getOriginalBytes(collectionId);

        if (optionalOriginalBytes.isEmpty()) {
            String errorMessage = "OriginalBytes of requested cover was not present when trying to convert";
            logger.error(errorMessage);
            throw new DataIsNotPresentException(errorMessage);
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] data = converterService.convert(targetWidth, targetHeight, "image/webp", outputStream, optionalOriginalBytes.get());
            storageCoverService.putNewBytesAsFile(collectionId, imageSize, data);
        }
        catch (IOException e) {
            String errorMessage = "Error while converting cover";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
        catch (DataSavingException e) {
            String errorMessage = "Error while saving converted cover";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }
}
