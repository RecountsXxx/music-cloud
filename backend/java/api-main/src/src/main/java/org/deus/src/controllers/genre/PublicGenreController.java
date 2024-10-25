package org.deus.src.controllers.genre;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.genre.GenreDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.responses.api.genre.SuccessGenreResponse;
import org.deus.src.services.models.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/genre")
@RequiredArgsConstructor
public class PublicGenreController {
    private final GenreService genreService;
    private static final Logger logger = LoggerFactory.getLogger(PublicGenreController.class);

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<SuccessGenreResponse> getById(@PathVariable Short id) throws StatusException {
        try {
            GenreDTO genreDTO = genreService.getDTOById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessGenreResponse(genreDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get genre";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<GenreDTO>> getAll() throws StatusException {
        try {
            List<GenreDTO> genres = genreService.getAllDTOs();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(genres);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to get genres";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
