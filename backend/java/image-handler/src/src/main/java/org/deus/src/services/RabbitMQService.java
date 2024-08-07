package org.deus.src.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.helpers.CoverConvertingDTO;
import org.deus.src.exceptions.message.MessageSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RabbitMQService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

    private <Data, TClass> void serializeAndSendMessage(String queueName, Data data, Class<TClass> targetClass) throws MessageSendingException {
        try {
            String json = objectMapper.writeValueAsString(data);
            rabbitTemplate.convertAndSend(queueName, json);
        }
        catch (JsonProcessingException e) {
            String errorMessage = "Error while sending " + targetClass.getName() + " via RabbitMQ to queue \"" + queueName + "\"";
            logger.error(errorMessage, e);
            throw new MessageSendingException(errorMessage, e);
        }
    }

    private <T> Optional<T> deserializeMessage(Message message, Class<T> targetClass) {
        try {
            String json = new String(message.getBody());
            T object = objectMapper.readValue(json, targetClass);
            return Optional.of(object);
        }
        catch (JsonProcessingException e) {
            logger.error("Error while trying to convert message back from json", e);
            return Optional.empty();
        }
    }

    public void sendUserDTO(String queueName, UserDTO object) throws MessageSendingException {
        this.serializeAndSendMessage(queueName, object, UserDTO.class);
    }

    public Optional<UserDTO> receiveUserDTO(Message message) {
        return this.deserializeMessage(message, UserDTO.class);
    }

    public void sendCoverConvertingDTO(String queueName, CoverConvertingDTO object) throws MessageSendingException {
        this.serializeAndSendMessage(queueName, object, CoverConvertingDTO.class);
    }

    public Optional<CoverConvertingDTO> receiveCoverConvertingDTO(Message message) {
        return this.deserializeMessage(message, CoverConvertingDTO.class);
    }
}
