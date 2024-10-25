package org.deus.src.responses.api.genre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.genre.GenreDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessGenreResponse {
    private GenreDTO genre;
}
