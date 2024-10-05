package org.deus.src.dtos.fromModels.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongListenedDTO {
    private ShortSongDTO song;
    private LocalDateTime listenedAt;
}
