package org.deus.src.config.exceptions;

import org.deus.src.dtos.errors.ErrorResponseDTO;
import org.deus.src.exceptions.StatusException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(StatusException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(ex);
        HttpStatus status = errorResponseDTO.getStatus();
        return ResponseEntity.status(status.value()).body(errorResponseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation error",
                        (existing, replacement) -> existing
                ));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}