package org.deus.src.responses.api.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.song.SongDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessSongResponse {
    private SongDTO song;
}
