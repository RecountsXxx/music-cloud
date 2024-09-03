package org.deus.src.listeners;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.helpers.AvatarsReadyDTO;
import org.deus.src.enums.ImageSize;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.converters.ConvertAvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ConvertAvatarRabbitMQListener {
    private final RabbitMQService rabbitMQService;
    private final ConvertAvatarService convertAvatarService;
    private static final Logger logger = LoggerFactory.getLogger(ConvertAvatarRabbitMQListener.class);

    @Value("${avatar.size.small.width}")
    private int smallWidth;
    @Value("${avatar.size.small.height}")
    private int smallHeight;

    @Value("${avatar.size.medium.width}")
    private int mediumWidth;
    @Value("${avatar.size.medium.height}")
    private int mediumHeight;

    @Value("${avatar.size.large.width}")
    private int largeWidth;
    @Value("${avatar.size.large.height}")
    private int largeHeight;

    @RabbitListener(queues = "convert.avatar")
    public void convertAvatar(Message message) {
        Optional<UserDTO> optionalUserDTO = this.rabbitMQService.receiveUserDTO(message);

        if (optionalUserDTO.isEmpty()) {
            logger.error(UserDTO.class.getName() + " was not present when trying to convert avatar");
            return;
        }

        UserDTO userDTO = optionalUserDTO.get();

        try {
            String smallAvatarUrl = this.convertAvatarService.convertAvatar(userDTO.getId(), ImageSize.SMALL, smallWidth, smallHeight);
            String mediumAvatarUrl = this.convertAvatarService.convertAvatar(userDTO.getId(), ImageSize.MEDIUM, mediumWidth, mediumHeight);
            String largeAvatarUrl = this.convertAvatarService.convertAvatar(userDTO.getId(), ImageSize.LARGE, largeWidth, largeHeight);

            AvatarsReadyDTO avatarsReadyDTO = new AvatarsReadyDTO(smallAvatarUrl, mediumAvatarUrl, largeAvatarUrl);

            // send message via rabbitmq to websocket microservice: avatars is ready.
            this.rabbitMQService.sendWebsocketMessageDTO(
                    "websocket.messages",
                    userDTO.getId(),
                    "avatar.ready",
                    "Your avatar was successfully uploaded and optimized!",
                    avatarsReadyDTO
            );
        }
        catch (DataIsNotPresentException | DataProcessingException e) {
            logger.error("Some problems have occurred while trying to convert avatar for user with id \"" + userDTO.getId() + "\"", e);

            // send message via rabbitmq to websocket microservice: conversion of avatar have failed.
        }
    }
}