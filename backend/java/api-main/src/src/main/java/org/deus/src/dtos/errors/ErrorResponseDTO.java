package org.deus.src.dtos.errors;

import lombok.Data;
import org.deus.src.exceptions.StatusException;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponseDTO {

    private final HttpStatus status;
    private final String message;

    public ErrorResponseDTO(StatusException ex) {
        this.status = ex.getStatus();
        this.message = ex.getMessage();
    }

    public ErrorResponseDTO(HttpStatus status, String Message) {
        this.status = status;
        this.message = Message;
    }
}
