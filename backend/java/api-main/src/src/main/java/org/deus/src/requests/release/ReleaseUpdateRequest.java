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
public class ReleaseUpdateRequest {
    @NotNull(message = "The id cannot be null")
    @NotBlank(message = "The id cannot be empty")
    private String id;

    private String name;
    private Privacy privacy;
    private LocalDate releaseDate;
    private ReleaseType type;
    private String buyLink;
    private String recordLabel;
}
