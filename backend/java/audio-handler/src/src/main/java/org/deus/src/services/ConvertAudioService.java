package org.deus.src.services;

import lombok.RequiredArgsConstructor;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.data.DataIsNotPresentException;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.services.storage.StorageTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Converts audio files into HLS playlists with various quality levels.
 */
@Service
@RequiredArgsConstructor
public class ConvertAudioService {
    private final StorageTempService storageTempService;
    private final HlsPlaylistService hlsPlaylistService;
    private static final Logger logger = LoggerFactory.getLogger(ConvertAudioService.class);

    /**
     * Bitrates for different audio qualities (injected from application properties)
     */
    @Value("${bitrate.low}")
    private int bitrateLow;

    @Value("${bitrate.medium}")
    private int bitrateMedium;

    @Value("${bitrate.high}")
    private int bitrateHigh;

    /**
     * Converts an audio file into HLS playlists with specified qualities.
     *
     * @param userId  The user ID associated with the audio.
     * @param audioId The ID of the audio file.
     * @param fileId  The ID of the file containing the original audio data.
     * @throws DataIsNotPresentException If the original audio data is not found.
     * @throws DataProcessingException   If an error occurs during conversion.
     */
    public void convertAudio(String userId, String audioId, String fileId) throws DataIsNotPresentException, DataProcessingException {
        // Fetch the original audio data from temp-files bucket in storage.
        Optional<byte[]> optionalOriginalBytes = storageTempService.getOriginalBytes(userId, fileId);

        if (optionalOriginalBytes.isEmpty()) {
            String errorMessage = "OriginalBytes of requested audio file was not present when trying to convert.";
            logger.error(errorMessage);
            throw new DataIsNotPresentException(errorMessage);
        }

        byte[] originalBytes = optionalOriginalBytes.get();

        // Convert audio to different qualities.
        for (AudioQuality quality : AudioQuality.values()) {
            int bitrate = getBitrateForQuality(quality);

            try {
                System.out.println(audioId);
                System.out.println(quality);
                System.out.println(bitrate);

                hlsPlaylistService.createHlsPlaylist(audioId, originalBytes, quality, bitrate);
            } catch (DataProcessingException e) {
                String errorMessage = "Error while converting audio to " + quality;
                logger.error(errorMessage, e);
                throw new DataProcessingException(errorMessage, e);
            }
        }
    }

    /**
     * Maps an audio quality to its corresponding bitrate.
     *
     * @param quality The audio quality.
     * @return The bitrate for the given quality.
     */
    private int getBitrateForQuality(AudioQuality quality) {
        return switch (quality) {
            case LOW -> bitrateLow;
            case MEDIUM -> bitrateMedium;
            case HIGH -> bitrateHigh;
        };
    }
}
