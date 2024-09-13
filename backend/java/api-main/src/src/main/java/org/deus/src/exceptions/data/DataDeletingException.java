package org.deus.src.exceptions.data;

public class DataDeletingException extends Exception{
    public DataDeletingException() {
        super();
    }

    public DataDeletingException(String message) {
        super(message);
    }

    public DataDeletingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDeletingException(Throwable cause) {
        super(cause);
    }
}
