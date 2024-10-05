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
public class PlaylistCreateRequest {
    @NotNull(message = "The name cannot be null")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @NotNull(message = "The name cannot be null")
    private Privacy privacy;

    @NotNull(message = "The creatorUserProfileId cannot be null")
    @NotBlank(message = "The creatorUserProfileId cannot be empty")
    private String creatorUserProfileId;

    private String description;
}
