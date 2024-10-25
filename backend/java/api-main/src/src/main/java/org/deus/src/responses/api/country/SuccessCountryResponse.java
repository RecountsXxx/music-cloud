package org.deus.src.responses.api.country;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.deus.src.dtos.fromModels.country.CountryDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessCountryResponse {
    private CountryDTO country;
}
