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
    private String name;
    private Privacy privacy;
    private LocalDate releaseDate;
    private ReleaseType type;
    private String buyLink;
    private String recordLabel;
}
