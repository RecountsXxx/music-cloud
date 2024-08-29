package org.deus.src.controllers.collection;

import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.responses.SuccessMessageResponse;
import org.deus.src.services.collection.CoverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload/protected/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final CoverService coverService;

    @PostMapping(value = "/{id}/cover")
    public ResponseEntity<SuccessMessageResponse> coverUpload(@PathVariable String id, @RequestAttribute("userDTO") UserDTO userDTO, @RequestParam("cover") MultipartFile cover) throws StatusException {
        if (cover.isEmpty()) {
            throw new StatusException("Please select a file!", HttpStatus.BAD_REQUEST);
        }

        return coverService.coverUpload(id, userDTO.getId(), cover);
    }
}
