package org.deus.src.requests.genre;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreUpdateRequest {
    @NotNull(message = "The id cannot be null")
    private Short id;

    private String name;
}
