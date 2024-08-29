package org.deus.src.config.exceptions;

import org.deus.src.dtos.errors.ErrorResponseDTO;
import org.deus.src.exceptions.StatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(StatusException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex);
        HttpStatus status = ex.getStatus();
        return ResponseEntity.status(status.value()).body(errorResponseDTO);
    }
}