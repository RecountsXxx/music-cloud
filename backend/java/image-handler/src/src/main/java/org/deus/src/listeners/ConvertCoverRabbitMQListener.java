package org.deus.src.listeners;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.helpers.CoverConvertingDTO;
import org.deus.src.enums.ImageSize;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.services.RabbitMQService;
import org.deus.src.services.converters.ConvertCoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ConvertCoverRabbitMQListener {
    private final RabbitMQService rabbitMQService;
    private final ConvertCoverService convertCoverService;
    private static final Logger logger = LoggerFactory.getLogger(ConvertCoverService.class);

    @Value("${cover.size.small.width}")
    private int smallWidth;
    @Value("${cover.size.small.height}")
    private int smallHeight;

    @Value("${cover.size.medium.width}")
    private int mediumWidth;
    @Value("${cover.size.medium.height}")
    private int mediumHeight;

    @Value("${cover.size.large.width}")
    private int largeWidth;
    @Value("${cover.size.large.height}")
    private int largeHeight;

    @RabbitListener(queues = "convert.cover")
    public void convertCover(Message message) {
        Optional<CoverConvertingDTO> optionalCoverConvertingDTO = this.rabbitMQService.receiveCoverConvertingDTO(message);

        if(optionalCoverConvertingDTO.isEmpty()) {
            logger.error(CoverConvertingDTO.class.getName() + " was not present when trying to convert cover");
            return;
        }

        CoverConvertingDTO coverConvertingDTO = optionalCoverConvertingDTO.get();

        try {
            this.convertCoverService.convertCover(coverConvertingDTO.getCollectionId(), ImageSize.SMALL, smallWidth, smallHeight);
            this.convertCoverService.convertCover(coverConvertingDTO.getCollectionId(), ImageSize.MEDIUM, mediumWidth, mediumHeight);
            this.convertCoverService.convertCover(coverConvertingDTO.getCollectionId(), ImageSize.LARGE, largeWidth, largeHeight);

            this.rabbitMQService.sendWebsocketMessageDTO(
                    "websocket.messages",
                    coverConvertingDTO.getUserId(),
                    "cover.ready",
                    "Your cover was successfully uploaded and optimized!",
                    null
            );
        }
        catch (DataIsNotPresentException | DataProcessingException e) {
            logger.error("Some problems have occurred while trying to convert cover for collection with id \"" + coverConvertingDTO.getCollectionId() + "\"", e);

            // send message via rabbitmq to websoket microservice: conversion of cover have failed.
        }
    }
}
