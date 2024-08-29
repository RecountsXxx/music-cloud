package org.deus.src.exceptions.data;

public class DataProcessingException extends Exception {
    public DataProcessingException() {
        super();
    }

    public DataProcessingException(String message) {
        super(message);
    }

    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataProcessingException(Throwable cause) {
        super(cause);
    }
}
