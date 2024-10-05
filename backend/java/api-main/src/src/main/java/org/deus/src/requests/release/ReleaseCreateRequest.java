package org.deus.src.requests.release;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.enums.Privacy;
import org.deus.src.enums.ReleaseType;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseCreateRequest {
    @NotNull(message = "The name cannot be null")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @NotNull(message = "The name cannot be null")
    private Privacy privacy;

    @NotNull(message = "The creatorUserProfileId cannot be null")
    @NotBlank(message = "The creatorUserProfileId cannot be empty")
    private String creatorUserProfileId;

    @NotNull(message = "The releaseDate cannot be null")
    private LocalDate releaseDate;

    @NotNull(message = "The type cannot be null")
    private ReleaseType type;

    @NotNull(message = "The buyLink cannot be null")
    @NotBlank(message = "The buyLink cannot be empty")
    private String buyLink;

    @NotNull(message = "The buyLink cannot be null")
    @NotBlank(message = "The buyLink cannot be empty")
    private String recordLabel;
}
