package org.deus.src.requests.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.enums.Privacy;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistUpdateRequest {
    @NotNull(message = "The id cannot be null")
    @NotBlank(message = "The id cannot be empty")
    private String id;

    private String name;
    private Privacy privacy;
    private String description;
}
