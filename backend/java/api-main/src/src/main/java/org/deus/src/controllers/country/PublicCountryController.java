package org.deus.src.controllers.country;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.country.CountryDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.responses.api.country.SuccessCountryResponse;
import org.deus.src.services.models.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/country")
@RequiredArgsConstructor
public class PublicCountryController {
    private final CountryService countryService;
    private static final Logger logger = LoggerFactory.getLogger(PublicCountryController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessCountryResponse> getById(@PathVariable Short id) throws StatusException {
        try {
            CountryDTO countryDTO = countryService.getDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessCountryResponse(countryDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get country";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<CountryDTO>> getAll() throws StatusException {
        try {
            List<CountryDTO> countries = countryService.getAllDTOs();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(countries);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get countries";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
