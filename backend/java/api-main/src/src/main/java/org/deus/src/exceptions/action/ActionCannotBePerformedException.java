package org.deus.src.exceptions.action;

public class ActionCannotBePerformedException extends Exception {
    public ActionCannotBePerformedException() {
        super();
    }

    public ActionCannotBePerformedException(String message) {
        super(message);
    }

    public ActionCannotBePerformedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionCannotBePerformedException(Throwable cause) {
        super(cause);
    }
}
