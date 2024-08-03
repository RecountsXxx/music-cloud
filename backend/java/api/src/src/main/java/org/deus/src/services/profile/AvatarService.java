package org.deus.src.services.profile;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataSavingException;
import org.deus.src.exceptions.message.MessageSendingException;
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
    public ResponseEntity<String> avatarUpload(MultipartFile avatar, UserDTO userDTO) throws StatusException {
        try {
            storageAvatarService.putOriginalBytes(userDTO.getId(), avatar.getBytes());

            rabbitMQService.sendUserDTO("convert.avatar", userDTO);

            return ResponseEntity.ok("Process of updating avatar has started. Please wait...");
        }
        catch (IOException | DataSavingException | MessageSendingException e) {
            String message = "Failed to upload avatar file!";
            logger.error(message, e);
            throw new StatusException(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
