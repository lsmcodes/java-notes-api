package io.github.lsmcodes.notes_api.exception;

/**
 * Exception thrown when a user tries to use an username that is already in use.
 */
public class UsernameAlreadyExistsException extends Exception {

    /**
     * Builds a new exception with the specified message.
     * 
     * @param message The exception description message.
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

}