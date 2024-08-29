package org.deus.src.services.tusUpload;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.desair.tus.server.upload.UploadInfo;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataDeletingException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.exceptions.data.DataSavingException;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.storage.StorageTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final TusFileUploadWrapperService tusFileUploadWrapperService;
    private final StorageTempService storageTempService;
    private final String[] REQUIRED_METADATA_KEYS = {"fileId"};
    private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public void processUpload(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String userId) throws IOException {
        tusFileUploadWrapperService.processRequest(servletRequest, servletResponse);
        String uploadURI = servletRequest.getRequestURI();

        Optional<UploadInfo> optionalUploadInfo = this.tusFileUploadWrapperService.getUploadInfo(uploadURI);

        if (optionalUploadInfo.isEmpty()) {
            logger.error("Couldn't get UploadInfo due to some problems");
            return;
        }

        UploadInfo uploadInfo = optionalUploadInfo.get();

        if (uploadInfo.isUploadInProgress()) {
            return;
        }

        Map<String, String> metadata = uploadInfo.getMetadata();

        CompletableFuture.runAsync(() -> {
            try {
                if (!checkMetadata(metadata, servletResponse, REQUIRED_METADATA_KEYS)) {
                    logger.error("User didn't provide necessary metadata");
                    return;
                }

                String fileId = metadata.get("fileId");
                Optional<InputStream> optionalInputStream = this.tusFileUploadWrapperService.getUploadedBytes(uploadURI);

                if (optionalInputStream.isEmpty()) {
                    String errorMessage = "Uploaded bytes not found for URI: " + uploadURI;
                    logger.error(errorMessage);
                    throw new DataNotFoundException(errorMessage);
                }

                try (InputStream inputStream = optionalInputStream.get()) {
                    this.processInputStream(userId, fileId, inputStream, uploadURI);
                }

                // send message via rabbitmq to websocket microservice about successful file upload
            }
            catch (IOException | DataNotFoundException | DataProcessingException e) {
                logger.error("Error during storing file upload with URI \"" + uploadURI + "\", for user with id \"" + userId + "\" and metadata: [" + metadata.toString() + "]", e);
            }
        });
    }

    private void processInputStream(String userId, String fileId, InputStream inputStream, String uploadURI) throws DataProcessingException {
        try {
            this.storageTempService.putOriginalBytes(userId, fileId, inputStream.readAllBytes());
            this.tusFileUploadWrapperService.deleteUpload(uploadURI);
        }
        catch (IOException | OutOfMemoryError e) {
            String errorMessage = "Error while trying to read bytes from provided InputStream";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
        catch (DataDeletingException | DataSavingException e) {
            String errorMessage = "Error while processing uploaded bytes";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }

    public Boolean checkFile(String userId, String fileId) throws StatusException {
        try {
            return this.storageTempService.isFileExists(userId, fileId);
        } catch (DataProcessingException e) {
            throw new StatusException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean checkMetadata(Map<String, String> metadata, HttpServletResponse servletResponse, String... requiredKeys) throws IOException {
        for (String key : requiredKeys) {
            if (!metadata.containsKey(key)) {
                String errorMessage = "You need to provide '" + key + "' inside metadata.";
                servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                servletResponse.getWriter().println(errorMessage);
                servletResponse.getWriter().flush();

                return false;
            }
        }
        return true;
    }

    public void cleanup() {
        this.tusFileUploadWrapperService.cleanup();
    }
}
