package io.github.lsmcodes.notes_api.exception;

/**
 * Exception thrown when a requested user cannot be found.
 */
public class UserNotFoundException extends Exception {

    /**
     * Builds a new exception with the specified message.
     * 
     * @param message The exception description message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }

}