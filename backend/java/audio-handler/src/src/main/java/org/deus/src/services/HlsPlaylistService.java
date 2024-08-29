package org.deus.src.services;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.deus.src.enums.AudioQuality;
import org.deus.src.exceptions.data.DataProcessingException;
import org.deus.src.services.storage.StorageAudioService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class HlsPlaylistService {
    private final StorageAudioService storageAudioService;
    private static final Logger logger = LoggerFactory.getLogger(HlsPlaylistService.class);

    @Value("${base.api.streaming.segment.url}")
    private String baseApiStreamingSegmentUrl;

    @Value("${audio.format}")
    private String audioFormat;

    @Value("${segment.length}")
    private String segmentLength;

    public void createHlsPlaylist(String audioId, byte[] audioBytes, AudioQuality quality, int bitrate) throws DataProcessingException {
        try {
            File tempDir = Files.createTempDirectory("hls").toFile();

            Process process = getProcess(tempDir, audioFormat, bitrate, segmentLength);

            try (OutputStream os = process.getOutputStream()) {
                os.write(audioBytes);
                os.flush();
            }

            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), logger::info);
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), logger::error);

            new Thread(outputGobbler).start();
            new Thread(errorGobbler).start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg exited with error code " + exitCode);
            }

            File playlistFile = new File(tempDir, "playlist.m3u8");
            byte[] playlistBytes = Files.readAllBytes(playlistFile.toPath());

            File[] segmentFiles = tempDir.listFiles((dir, name) -> name.endsWith(".ts"));
            if (segmentFiles == null) {
                throw new DataProcessingException("No segments generated");
            }

            for (File segmentFile : segmentFiles) {
                byte[] segmentBytes = Files.readAllBytes(segmentFile.toPath());

                this.storageAudioService.putHlsSegment(audioId, segmentBytes, segmentFile.getName(), quality);
            }

            byte[] updatedPlaylistBytes = this.getBytes(audioId, quality, playlistBytes);

            this.storageAudioService.putHlsPlaylist(audioId, updatedPlaylistBytes, quality);

            FileUtils.deleteDirectory(tempDir);
        }
        catch (Exception e) {
            String errorMessage = "Error while creating HLS playlist";
            logger.error(errorMessage, e);
            throw new DataProcessingException(errorMessage, e);
        }
    }

    @NotNull
    private byte[] getBytes(String audioId, AudioQuality quality, byte[] playlistBytes) {
        String baseUrl = this.baseApiStreamingSegmentUrl + "/" + audioId + "/" + quality.toString().toUpperCase() + "/";
        String playlistContent = new String(playlistBytes, StandardCharsets.UTF_8);

        playlistContent = playlistContent.replaceAll("segment_", baseUrl + "segment_");

        return playlistContent.getBytes(StandardCharsets.UTF_8);
    }

    @NotNull
    private static Process getProcess(File tempDir, String audioFormat, int bitrate, String segmentLength) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", // Invokes the ffmpeg command
                "-i", "pipe:0", // Takes input from the standard input (stdin)
                "-c:a", audioFormat, // Re-encodes audio to the specified format
                "-b:a", bitrate + "k", // Re-encodes audio to the specified format
                "-fflags", "+genpts", // Generate PTS (Presentation TimeStamp)
                "-start_number", "0", // Starts segment numbering from 0
                "-hls_time", segmentLength, // Length of segments in seconds
                "-hls_list_size", "0", // Maximum number of segments listed. When set to "0" playlist will include all available segments.
                "-hls_flags", "independent_segments", // Ensure independent segments
                "-hls_segment_type", "mpegts", // Uses MPEG-TS format for segments
                "-f", "hls", // Sets the output format to HLS
                "-hls_segment_filename", tempDir.getAbsolutePath() + "/segment_%03d.ts", // Specifies the segment filename template
                tempDir.getAbsolutePath() + "/playlist.m3u8" // Sets the output playlist filename
        );

        // Starts the ffmpeg process
        return pb.start();
    }

    private record StreamGobbler(InputStream inputStream, java.util.function.Consumer<String> consumer) implements Runnable {
        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .forEach(consumer);
        }
    }
}