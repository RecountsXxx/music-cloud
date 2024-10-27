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
    private String name;
    private Privacy privacy;
    private String description;
}
