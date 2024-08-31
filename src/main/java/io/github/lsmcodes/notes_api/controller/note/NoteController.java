package io.github.lsmcodes.notes_api.controller.note;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.lsmcodes.notes_api.dto.model.note.NoteRequestDTO;
import io.github.lsmcodes.notes_api.dto.model.note.NoteResponseDTO;
import io.github.lsmcodes.notes_api.dto.model.security.AuthenticationDTO;
import io.github.lsmcodes.notes_api.dto.response.Response;
import io.github.lsmcodes.notes_api.exception.NoteNotFoundException;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.note.NoteService;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Implements note endpoints.
 */
@RestController
@RequestMapping("notes-api/notes")
@Tag(name = "Note", description = "Note endpoints")
public class NoteController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private VerificationService verificationService;

    /**
     * Creates a new note.
     * 
     * @param dto    A {@link NoteRequestDTO} containing the note details.
     * @param result A {@link BindingResult} containing the result of the validation
     *               checks on the {@link AuthenticationDTO}.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link NoteResponseDTO}> object.
     * @throws UserNotFoundException if no user was found based in the username
     *                               defined in the authentication.
     */
    @Operation(summary = "Creates a new note")
    @SecurityRequirement(name = "JWT token")
    @PostMapping
    public ResponseEntity<Response<NoteResponseDTO>> createNote(@RequestBody @Valid NoteRequestDTO dto,
            BindingResult result) throws UserNotFoundException {
        Response<NoteResponseDTO> response = new Response<>();

        if (result.hasErrors()) {
            NotesApiUtil.getResponseErrorMessages(result)
                    .forEach(errorMessage -> response.setErrors(400, errorMessage));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();
        Note note = dto.DTOToEntity();
        note.setUser(loggedInUser);
        note = this.noteService.save(note);

        loggedInUser.getNotes().add(note);
        this.userService.save(loggedInUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(note.getId())
                .toUri();

        response.setData(note.entityToDTO());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    /**
     * Retrieves a note with the provided id.
     * 
     * @param id The id of the note to be retrieved.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link NoteResponseDTO}> object.
     * @throws UserNotFoundException if no user was found based in the username
     *                               defined in the authentication.
     * @throws NoteNotFoundException if no note was found based in the provided id.
     */
    @Operation(summary = "Retrieves a note by id")
    @SecurityRequirement(name = "JWT token")
    @GetMapping("/{id}")
    public ResponseEntity<Response<NoteResponseDTO>> findById(@PathVariable UUID id)
            throws UserNotFoundException, NoteNotFoundException {
        Response<NoteResponseDTO> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        this.verificationService.verifyIfNoteExistsByUserAndId(loggedInUser, id);
        Note foundNote = this.noteService.findByUserAndId(loggedInUser, id).get();

        response.setData(foundNote.entityToDTO());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves all notes.
     * 
     * @param page          The notes page number.
     * @param size          The size of the notes page.
     * @param property      The property by which the notes will be sorted. Accepted
     *                      values are: "title", "createdAt" and "updatedAt".
     * @param sortDirection The direction in which the notes should be sorted.
     *                      Accepted values are: "asc" and "desc".
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link Page}<{@link NoteResponseDTO}>> object.
     * @throws UserNotFoundException if no user was found
     *                               based in the username defined in the
     *                               authentication.
     * @throws NoteNotFoundException If the retrieved notes page is empty.
     */
    @Operation(summary = "Retrieves all notes")
    @SecurityRequirement(name = "JWT token")
    @GetMapping
    public ResponseEntity<Response<Page<NoteResponseDTO>>> findAll(
            @RequestParam(defaultValue = "0") @NotNull int page,
            @RequestParam(defaultValue = "10") @NotNull int size,
            @RequestParam(defaultValue = "title") @NotNull @Pattern(regexp = "^(title|createdAt|updatedAt)$") String property,
            @RequestParam(defaultValue = "asc") @NotNull @Pattern(regexp = "^(asc|desc)$") String sortDirection)
            throws UserNotFoundException, NoteNotFoundException {
        Response<Page<NoteResponseDTO>> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        Direction direction = Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));

        Page<Note> foundPage = this.noteService.findByUser(loggedInUser, pageable);
        this.verificationService.verifyIfPageOfNotesIsNotEmpty(foundPage);

        response.setData(foundPage.map(note -> note.entityToDTO()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves all notes containing a specified term whether in title or content.
     * 
     * @param term          The term to be searched for.
     * @param page          The notes page number.
     * @param size          The size of the notes page.
     * @param property      The property by which the notes will be sorted. Accepted
     *                      values are: "title", "createdAt" and "updatedAt".
     * @param sortDirection The direction in which the notes should be sorted.
     *                      Accepted values are: "asc" and "desc".
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link Page}<{@link NoteResponseDTO}>> object.
     * @throws UserNotFoundException if no user was found
     *                               based in the username defined in the
     *                               authentication.
     * @throws NoteNotFoundException If the retrieved notes page is empty.
     */
    @Operation(summary = "Retrieves all notes containing a specified term whether in title or content")
    @SecurityRequirement(name = "JWT token")
    @GetMapping("/search/by-term")
    public ResponseEntity<Response<Page<NoteResponseDTO>>> findByTitleOrContentContainingTerm(
            @RequestParam String term, @RequestParam(defaultValue = "0") @NotNull int page,
            @RequestParam(defaultValue = "10") @NotNull int size,
            @RequestParam(defaultValue = "title") @NotNull @Pattern(regexp = "^(title|createdAt|updatedAt)$") String property,
            @RequestParam(defaultValue = "asc") @NotNull @Pattern(regexp = "^(asc|desc)$") String sortDirection)
            throws UserNotFoundException, NoteNotFoundException {
        Response<Page<NoteResponseDTO>> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        Direction direction = Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));

        Page<Note> foundPage = this.noteService.findByUserAndTitleOrContentContainingIgnoreCase(loggedInUser, term,
                pageable);
        this.verificationService.verifyIfPageOfNotesIsNotEmpty(foundPage);

        response.setData(foundPage.map(note -> note.entityToDTO()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves all notes containing at least one of the specified tags.
     * 
     * @param tags          The {@link List} of tags to be searched for.
     * @param page          The notes page number.
     * @param size          The size of the notes page.
     * @param property      The property by which the notes will be sorted. Accepted
     *                      values are: "title", "createdAt" and "updatedAt".
     * @param sortDirection The direction in which the notes should be sorted.
     *                      Accepted values are: "asc" and "desc".
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link Page}<{@link NoteResponseDTO}>> object.
     * @throws UserNotFoundException if no user was found
     *                               based in the username defined in the
     *                               authentication.
     * @throws NoteNotFoundException if the retrieved notes page is empty.
     */
    @Operation(summary = "Retrieves all notes containing at least one of the specified tags")
    @SecurityRequirement(name = "JWT token")
    @GetMapping("/search/by-tags")
    public ResponseEntity<Response<Page<NoteResponseDTO>>> findByTags(@RequestParam List<String> tags,
            @RequestParam(defaultValue = "0") @NotNull int page,
            @RequestParam(defaultValue = "10") @NotNull int size,
            @RequestParam(defaultValue = "title") @NotNull @Pattern(regexp = "^(title|createdAt|updatedAt)$") String property,
            @RequestParam(defaultValue = "asc") @NotNull @Pattern(regexp = "^(asc|desc)$") String sortDirection)
            throws UserNotFoundException, NoteNotFoundException {
        Response<Page<NoteResponseDTO>> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        Direction direction = Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));

        Page<Note> foundPage = this.noteService.findByUserAndTagsInIgnoreCase(loggedInUser, tags, pageable);
        this.verificationService.verifyIfPageOfNotesIsNotEmpty(foundPage);

        response.setData(foundPage.map(note -> note.entityToDTO()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Updates a note.
     * 
     * @param id     The id of the note to be updated.
     * @param dto    A {@link NoteRequestDTO} containing the note details.
     * @param result A {@link BindingResult} containing the result of the validation
     *               checks on the {@link AuthenticationDTO}.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link NoteResponseDTO}> object.
     * @throws UserNotFoundException if no user was found based in the username
     *                               defined in the authentication.
     * @throws NoteNotFoundException if no note was found with the provided id.
     */
    @Operation(summary = "Updates a note")
    @SecurityRequirement(name = "JWT token")
    @PutMapping("/{id}")
    public ResponseEntity<Response<NoteResponseDTO>> updateById(@PathVariable UUID id,
            @RequestBody @Valid NoteRequestDTO dto, BindingResult result)
            throws UserNotFoundException, NoteNotFoundException {
        Response<NoteResponseDTO> response = new Response<>();

        if (result.hasErrors()) {
            NotesApiUtil.getResponseErrorMessages(result)
                    .forEach(errorMessage -> response.setErrors(400, errorMessage));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        this.verificationService.verifyIfNoteExistsByUserAndId(loggedInUser, id);
        Note note = this.noteService.findByUserAndId(loggedInUser, id).get();

        note.setTags(dto.getTags());
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        this.noteService.save(note);

        response.setData(note.entityToDTO());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Deletes a note.
     * 
     * @param id The id of the note to be deleted.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link NoteResponseDTO}> object.
     * @throws UserNotFoundException if no user was found based in the username
     *                               defined in the authentication.
     * @throws NoteNotFoundException if no note was found with the provided id.
     */
    @Operation(summary = "Deletes a note")
    @SecurityRequirement(name = "JWT token")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteById(@PathVariable UUID id)
            throws UserNotFoundException, NoteNotFoundException {
        Response<String> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        this.verificationService.verifyIfNoteExistsByUserAndId(loggedInUser, id);
        Note note = this.noteService.findByUserAndId(loggedInUser, id).get();

        loggedInUser.getNotes().remove(note);
        this.noteService.deleteByUserAndId(loggedInUser, id);

        response.setData("The note \"" + note.getTitle() + "\" was deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}