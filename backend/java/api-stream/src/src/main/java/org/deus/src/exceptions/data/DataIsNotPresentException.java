package org.deus.src.exceptions.data;

public class DataIsNotPresentException extends Exception {
    public DataIsNotPresentException() {
        super();
    }

    public DataIsNotPresentException(String message) {
        super(message);
    }

    public DataIsNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIsNotPresentException(Throwable cause) {
        super(cause);
    }
}
