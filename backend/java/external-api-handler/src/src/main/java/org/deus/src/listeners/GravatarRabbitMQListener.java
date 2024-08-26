package org.deus.src.listeners;

import lombok.AllArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.exceptions.data.DataSavingException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.services.GravatarService;
import org.deus.src.services.ImageService;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.storage.StorageAvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class GravatarRabbitMQListener {
    private final RabbitMQService rabbitMQService;
    private final GravatarService gravatarService;
    private final StorageAvatarService storageAvatarService;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(GravatarRabbitMQListener.class);

    @RabbitListener(queues = {"user.register", "reset.gravatar"})
    public void gravatar(Message message) {
        Optional<UserDTO> optionalUserDTO = this.rabbitMQService.receiveUserDTO(message);

        if (optionalUserDTO.isEmpty()) {
            logger.error(UserDTO.class.getName() + " was not present when trying to get gravatar's result");
            return;
        }

        UserDTO userDTO = optionalUserDTO.get();

        try {
            String gravatarUrl = this.gravatarService.getGravatarUrl(userDTO.getEmail());
            byte[] imageData = this.imageService.getImageBytesByUrl(gravatarUrl);
            this.storageAvatarService.putOriginalBytes(userDTO.getId(), imageData);
            this.rabbitMQService.sendUserDTO("convert.avatar", userDTO);
        }
        catch (DataProcessingException | DataSavingException e) {
            logger.error("Error while getting gravatar's result", e);
            //this.sendErrorMessage(roomIdentifier);
        }
        catch (MessageSendingException e) {
            logger.error("Error while trying to send gravatar's result to microservice for converting", e);
            //this.sendErrorMessage(roomIdentifier);
        }
    }

    private void sendErrorMessage(String roomIdentifier) {
        String errorMessage = "Something went wrong while trying to get user's avatar with service gravatar. We'll try later";

        // send message via rabbitmq to websoket microservice about error
    }
}
