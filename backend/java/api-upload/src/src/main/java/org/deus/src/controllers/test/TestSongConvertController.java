package org.deus.src.controllers.test;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.helpers.AudioConvertingDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.message.MessageSendingException;
import org.deus.src.responses.SuccessMessageResponse;
import org.deus.src.services.RabbitMQService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/upload/protected/test/audio")
@RequiredArgsConstructor
public class TestSongConvertController {
    private final RabbitMQService rabbitMQService;

    @PostMapping(value = "/{audioId}/{fileId}/convert")
    public ResponseEntity<SuccessMessageResponse> convertSong(@PathVariable String audioId, @PathVariable String fileId, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        AudioConvertingDTO audioConvertingDTO = new AudioConvertingDTO(userDTO.getId(), audioId, fileId);

        String queueName = "convert.audio";

        try {
            rabbitMQService.sendAudioConvertingDTO(queueName, audioConvertingDTO);
        }
        catch (MessageSendingException e) {
            throw new StatusException("Failed to send the SongConvertingDTO to the message queue \"" + queueName + "\".", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(new SuccessMessageResponse("Song convert process initiated. Please wait..."));
    }
}