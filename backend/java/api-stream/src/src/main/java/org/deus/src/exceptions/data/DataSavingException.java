package org.deus.src.exceptions.data;

public class DataSavingException extends Exception {
    public DataSavingException() {
        super();
    }

    public DataSavingException(String message) {
        super(message);
    }

    public DataSavingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSavingException(Throwable cause) {
        super(cause);
    }
}
