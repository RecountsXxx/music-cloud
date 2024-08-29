package org.deus.src.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.helpers.AudioConvertingDTO;
import org.deus.src.dtos.helpers.CoverConvertingDTO;
import org.deus.src.exceptions.message.MessageSendingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service for sending and receiving messages through RabbitMQ.
 */
@Component
@AllArgsConstructor
public class RabbitMQService {
    // RabbitMQ template for sending and receiving messages
    private final RabbitTemplate rabbitTemplate;

    // ObjectMapper for serializing and deserializing objects to JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

    /**
     * Serializes a given object into JSON and sends it to a specified RabbitMQ queue.
     *
     * @param queueName  The name of the RabbitMQ queue.
     * @param data       The object to be serialized and sent.
     * @param targetClass The class of the object for error handling.
     * @throws MessageSendingException If an error occurs during serialization or sending.
     */
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

    /**
     * Deserializes a received RabbitMQ message into an object of the specified type.
     *
     * @param message   The received RabbitMQ message.
     * @param targetClass The class of the object to be deserialized.
     * @return An Optional containing the deserialized object, or empty if an error occurs.
     */
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

    /**
     * Sends a UserDTO object to a specified RabbitMQ queue.
     *
     * @param queueName The name of the RabbitMQ queue.
     * @param object    The UserDTO object to be sent.
     * @throws MessageSendingException If an error occurs during serialization or sending.
     */
    public void sendUserDTO(String queueName, UserDTO object) throws MessageSendingException {
        this.serializeAndSendMessage(queueName, object, UserDTO.class);
    }

    /**
     * Receives a message from RabbitMQ and deserializes it into a UserDTO object.
     *
     * @param message The received RabbitMQ message.
     * @return An Optional containing the deserialized UserDTO object, or empty if an error occurs.
     */
    public Optional<UserDTO> receiveUserDTO(Message message) {
        return this.deserializeMessage(message, UserDTO.class);
    }

    /**
     * Sends a CoverConvertingDTO object to a specified RabbitMQ queue.
     *
     * @param queueName The name of the RabbitMQ queue.
     * @param object    The CoverConvertingDTO object to be sent.
     * @throws MessageSendingException If an error occurs during serialization or sending.
     */
    public void sendCoverConvertingDTO(String queueName, CoverConvertingDTO object) throws MessageSendingException {
        this.serializeAndSendMessage(queueName, object, CoverConvertingDTO.class);
    }

    /**
     * Receives a message from RabbitMQ and deserializes it into a CoverConvertingDTO object.
     *
     * @param message The received RabbitMQ message.
     * @return An Optional containing the deserialized CoverConvertingDTO object, or empty if an error occurs.
     */
    public Optional<CoverConvertingDTO> receiveCoverConvertingDTO(Message message) {
        return this.deserializeMessage(message, CoverConvertingDTO.class);
    }

    /**
     * Sends an AudioConvertingDTO object to a specified RabbitMQ queue.
     *
     * @param queueName The name of the RabbitMQ queue.
     * @param object    The AudioConvertingDTO object to be sent.
     * @throws MessageSendingException If an error occurs during serialization or sending.
     */
    public void sendAudioConvertingDTO(String queueName, AudioConvertingDTO object) throws MessageSendingException {
        this.serializeAndSendMessage(queueName, object, AudioConvertingDTO.class);
    }

    /**
     * Receives a message from RabbitMQ and deserializes it into an AudioConvertingDTO object.
     *
     * @param message The received RabbitMQ message.
     * @return An Optional containing the deserialized AudioConvertingDTO object, or empty if an error occurs.
     */
    public Optional<AudioConvertingDTO> receiveAudioConvertingDTO(Message message) {
        return this.deserializeMessage(message, AudioConvertingDTO.class);
    }
}
