package io.github.lsmcodes.notes_api.exception;

/**
 * Exception thrown when a requested note cannot be found.
 */
public class NoteNotFoundException extends Exception {

    /**
     * Builds a new exception with the specified message.
     * 
     * @param message The exception description message.
     */
    public NoteNotFoundException(String message) {
        super(message);
    }

}