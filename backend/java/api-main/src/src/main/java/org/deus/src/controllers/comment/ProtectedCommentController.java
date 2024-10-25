package org.deus.src.controllers.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deus.src.dtos.fromModels.UserDTO;
import org.deus.src.dtos.fromModels.comment.CommentDTO;
import org.deus.src.exceptions.StatusException;
import org.deus.src.exceptions.action.ActionCannotBePerformedException;
import org.deus.src.exceptions.data.DataNotFoundException;
import org.deus.src.models.CommentModel;
import org.deus.src.requests.comment.CommentCreateRequest;
import org.deus.src.requests.comment.CommentUpdateRequest;
import org.deus.src.responses.api.comment.SuccessCommentResponse;
import org.deus.src.services.models.CommentService;
import org.deus.src.services.models.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/protected/comment")
@RequiredArgsConstructor
public class ProtectedCommentController {
    private final CommentService commentService;
    private final UserProfileService userProfileService;
    private static final Logger logger = LoggerFactory.getLogger(ProtectedCommentController.class);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessCommentResponse> create(@RequestBody @Valid CommentCreateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            CommentModel comment = commentService.create(request, UUID.fromString(userDTO.getId()));
            CommentDTO commentDTO = commentService.getDTOById(comment.getId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessCommentResponse(commentDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to add comment to song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<SuccessCommentResponse> update(@RequestBody @Valid CommentUpdateRequest request, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            CommentModel comment = commentService.update(request, UUID.fromString(userDTO.getId()));
            CommentDTO commentDTO = commentService.getDTOById(comment.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessCommentResponse(commentDTO));
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to update comment from song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/{songId}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, @PathVariable UUID songId, @RequestAttribute("userDTO") UserDTO userDTO) throws StatusException {
        try {
            commentService.delete(id, songId, UUID.fromString(userDTO.getId()));

            return ResponseEntity.noContent().build();
        }
        catch (DataNotFoundException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (ActionCannotBePerformedException e) {
            logger.error(e.getMessage(), e);

            throw new StatusException(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            String errorMessage = "Something went wrong while trying to delete comment from song";
            logger.error(errorMessage, e);

            throw new StatusException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
