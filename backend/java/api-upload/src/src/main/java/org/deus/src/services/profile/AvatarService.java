package org.deus.src.services.profile;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataSavingException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.responses.SuccessMessageResponse;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.storage.StorageAvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final StorageAvatarService storageAvatarService;
    private final RabbitMQService rabbitMQService;
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public ResponseEntity<SuccessMessageResponse> avatarUpload(UserDTO userDTO, MultipartFile avatar) throws StatusException {
        String queueName = "convert.avatar";

        try {
            storageAvatarService.putOriginalBytes(userDTO.getId(), avatar.getBytes());

            rabbitMQService.sendUserDTO("convert.avatar", userDTO);

            return ResponseEntity.ok(new SuccessMessageResponse("Avatar upload process initiated. Please wait..."));
        }
        catch (IOException | DataSavingException | MessageSendingException e) {
            String message = "Failed to upload avatar file!" + e.getMessage();
            logger.error(message, e);

            if (e instanceof IOException) {
                throw new StatusException("An error occurred while processing the uploaded avatar file.", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (e instanceof DataSavingException) {
                throw new StatusException("Failed to save the avatar data.", HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (e instanceof MessageSendingException) {
                throw new StatusException("Failed to send the UserDTO to the message queue \"" + queueName + "\".", HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                throw new StatusException(message, HttpStatus.INTERNAL_SERVER_ERROR); // Default for unknown errors
            }
        }
    }

    public ResponseEntity<SuccessMessageResponse> gravatarUpdate(UserDTO userDTO) throws StatusException {
        try {
            rabbitMQService.sendUserDTO("reset.gravatar", userDTO);

            return ResponseEntity.ok(new SuccessMessageResponse("Gravatar update process initiated. Please wait..."));
        } catch (MessageSendingException e) {
            String message = "Gravatar update process failed. Try later...";
            logger.error(message, e);
            throw new StatusException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
