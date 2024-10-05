package org.deus.src.requests.country;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryUpdateRequest {
    @NotNull(message = "The id cannot be null")
    private Short id;

    private String name;
}
