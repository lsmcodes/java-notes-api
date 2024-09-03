package io.github.lsmcodes.notes_api.service.verification.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.exception.NoteNotFoundException;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.exception.UsernameAlreadyExistsException;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.note.NoteService;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;

/**
 * Implements methods for verifying various conditions related to users and
 * notes.
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private UserService UserService;

    @Autowired
    private NoteService noteService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyIfUserExistsByUsername(String username) throws UserNotFoundException {
        if (!this.UserService.existsByUsername(username)) {
            throw new UserNotFoundException("There is no user with the provided username");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyIfUsernameDoesNotExist(String username) throws UsernameAlreadyExistsException {
        if (this.UserService.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("The provided username is already in use");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyIfNoteExistsByUserAndId(User user, UUID id) throws NoteNotFoundException {
        if (!this.noteService.existsByUserAndId(user, id)) {
            throw new NoteNotFoundException("There is no user with the provided id");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyIfCurrentUserIsAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return false;

        return (authentication.isAuthenticated()) && (authentication instanceof UsernamePasswordAuthenticationToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verifyIfCurrentUserIsNotAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return true;

        return (authentication.isAuthenticated()) && (authentication instanceof AnonymousAuthenticationToken);
    }

}