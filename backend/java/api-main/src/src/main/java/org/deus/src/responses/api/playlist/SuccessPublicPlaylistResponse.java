package org.deus.src.responses.api.playlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.playlist.PublicPlaylistDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessPublicPlaylistResponse {
    private PublicPlaylistDTO playlist;
}
