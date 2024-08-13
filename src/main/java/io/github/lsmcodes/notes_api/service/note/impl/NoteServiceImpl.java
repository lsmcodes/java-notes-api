package io.github.lsmcodes.notes_api.service.note.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.model.note.Note;
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
    public boolean existsById(UUID id) {
        return this.noteRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Note> findById(UUID id) {
        return this.noteRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findAll() {
        return this.noteRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findByTitleOrContentContainingIgnoreCase(String term) {
        return this.noteRepository.findByTitleOrContentContainingIgnoreCase(term);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Note> findByTagsInIgnoreCase(List<String> tags) {
        return this.noteRepository.findByTagsInIgnoreCase(tags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(UUID id) {
        this.noteRepository.deleteById(id);
    }

}