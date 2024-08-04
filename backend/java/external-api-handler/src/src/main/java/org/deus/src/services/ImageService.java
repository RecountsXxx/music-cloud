package org.deus.src.services;

import org.deus.src.exceptions.data.DataProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public byte[] getImageBytesByUrl(String imageUrl) throws DataProcessingException {
        try {
            URL url = URI.create(imageUrl).toURL();
            try (InputStream inputStream = url.openStream()) {
                return inputStream.readAllBytes();
            }
        }
        catch (IOException e) {
            String errorMessage = "Error while downloading image by url";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }
}
