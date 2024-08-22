package io.github.lsmcodes.notes_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerErrorException;

import com.fasterxml.jackson.core.JsonParseException;

import io.github.lsmcodes.notes_api.dto.response.Response;

/**
 * Implements methods to handle exceptions and return customized responses.
 */
@RestControllerAdvice
public class NotesApiExceptionHandler<T> {

    /**
     * Handles {@link NoteNotFoundException} and returns a customized
     * {@link Response<T>}.
     * 
     * @param exception A {@link NoteNotFoundException}.
     * @return A {@link Response<T>} containing a 404 status.
     */
    @ExceptionHandler(value = { NoteNotFoundException.class })
    public ResponseEntity<Response<T>> handleNoteNotFoundException(NoteNotFoundException exception) {
        Response<T> response = new Response<>();
        response.setErrors(404, exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles {@link UsernameAlreadyExistsException} and returns a customized
     * {@link Response<T>}.
     * 
     * @param exception A {@link UsernameAlreadyExistsException}.
     * @return A {@link Response<T>} containing a 409 status.
     */
    @ExceptionHandler(value = { UsernameAlreadyExistsException.class })
    public ResponseEntity<Response<T>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception) {
        Response<T> response = new Response<>();
        response.setErrors(409, exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles {@link UserNotFoundException} and returns a customized
     * {@link Response<T>}.
     * 
     * @param exception A {@link UserNotFoundException}.
     * @return A {@link Response<T>} containing a 404 status.
     */
    @ExceptionHandler(value = { UserNotFoundException.class })
    public ResponseEntity<Response<T>> handleUserNotFoundException(UserNotFoundException exception) {
        Response<T> response = new Response<>();
        response.setErrors(404, exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles {@link HttpMessageNotReadableException} and
     * {@link JsonParseException} returns a customized {@link Response<T>}.
     * 
     * @param exception A {@link Exception}.
     * @return A {@link Response<T>} containing a 400 status.
     */
    @ExceptionHandler(value = { HttpMessageNotReadableException.class, JsonParseException.class })
    public ResponseEntity<Response<T>> handleHttpMessageNotReadableException(Exception exception) {
        Response<T> response = new Response<>();
        response.setErrors(400, exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles {@link ServerErrorException} and returns a customized
     * {@link Response<T>}.
     * 
     * @param exception A {@link ServerErrorException}.
     * @return A {@link Response<T>} containing a 500 status.
     */
    @ExceptionHandler(value = { ServerErrorException.class })
    public ResponseEntity<Response<T>> handleServerErrorException(ServerErrorException exception) {
        Response<T> response = new Response<>();
        response.setErrors(500, exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}