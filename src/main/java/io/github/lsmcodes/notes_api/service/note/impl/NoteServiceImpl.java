package io.github.lsmcodes.notes_api.service.note.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.note.NoteRepository;
import io.github.lsmcodes.notes_api.service.note.NoteService;

/**
 * Implements {@link NoteService} interface methods.
 */
@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Note save(Note note) {
        return this.noteRepository.save(note);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUserAndId(User user, UUID id) {
        return this.noteRepository.existsByUserAndId(user, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Note> findByUserAndId(User user, UUID id) {
        return this.noteRepository.findByUserAndId(user, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Note> findByUser(User user, Pageable pageable) {
        return this.noteRepository.findByUser(user, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Note> findByUserAndTitleOrContentContainingIgnoreCase(User user, String term, Pageable pageable) {
        return this.noteRepository.findByUserAndTitleOrContentContainingIgnoreCase(user, term, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Note> findByUserAndTagsInIgnoreCase(User user, List<String> tags, Pageable pageable) {
        return this.noteRepository.findByUserAndTagsInIgnoreCase(user, tags, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByUserAndId(User user, UUID id) {
        this.noteRepository.deleteByUserAndId(user, id);
    }

}