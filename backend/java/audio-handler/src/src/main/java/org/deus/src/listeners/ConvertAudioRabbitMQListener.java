package org.deus.src.listeners;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.helpers.AudioConvertingDTO;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.services.ConvertAudioService;
import org.deus.src.services.RabbitMQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ConvertAudioRabbitMQListener {
    private final RabbitMQService rabbitMQService;
    private final ConvertAudioService convertAudioService;
    private static final Logger logger = LoggerFactory.getLogger(ConvertAudioRabbitMQListener.class);

    @RabbitListener(queues = "convert.audio")
    public void convertSong(Message message) {
        Optional<AudioConvertingDTO> optionalAudioCreatingDTO = this.rabbitMQService.receiveAudioConvertingDTO(message);

        if (optionalAudioCreatingDTO.isEmpty()) {
            logger.error(AudioConvertingDTO.class.getName() + " was not present when trying to convert song");
            return;
        }

        AudioConvertingDTO audioConvertingDTO = optionalAudioCreatingDTO.get();

        try {
            this.convertAudioService.convertAudio(audioConvertingDTO.getUserId(), audioConvertingDTO.getAudioId(), audioConvertingDTO.getFileId());

            // update status of audio to ready
        }
        catch (DataIsNotPresentException | DataProcessingException e) {
            logger.error("Some problems have occurred while trying to convert audio with id \"" + audioConvertingDTO.getAudioId() + "\"", e);
            // update status of audio to error
        }
    }
}
