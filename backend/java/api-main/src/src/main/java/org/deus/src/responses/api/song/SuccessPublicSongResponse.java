package org.deus.src.responses.api.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.song.PublicSongDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessPublicSongResponse {
    private PublicSongDTO song;
}
