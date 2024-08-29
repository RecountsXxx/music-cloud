package org.deus.src.exceptions.message;

public class MessageSendingException extends Exception {
    public MessageSendingException() {
        super();
    }

    public MessageSendingException(String message) {
        super(message);
    }

    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSendingException(Throwable cause) {
        super(cause);
    }
}
