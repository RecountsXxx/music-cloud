package org.deus.src.requests.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateRequest {
    @NotNull(message = "The id cannot be null")
    @NotBlank(message = "The id cannot be empty")
    private String id;

    private String name;
}
