package org.deus.src.requests.song;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongCreateRequest {
    @NotNull(message = "The tempFileId cannot be null")
    @NotBlank(message = "The tempFileId cannot be empty")
    private String tempFileId;

    @NotNull(message = "The name cannot be null")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @NotNull(message = "The releaseId cannot be null")
    @NotBlank(message = "The releaseId cannot be empty")
    private String releaseId;

    @NotNull(message = "The placeNumber cannot be null")
    private Short placeNumber;

    @NotNull(message = "The duration cannot be null")
    private Float duration;

    private Set<Short> genreIds;
    private Set<String> tags;
}
