package io.github.lsmcodes.notes_api.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lsmcodes.notes_api.dto.model.security.AuthenticationDTO;
import io.github.lsmcodes.notes_api.dto.model.user.UserRequestDTO;
import io.github.lsmcodes.notes_api.dto.model.user.UserResponseDTO;
import io.github.lsmcodes.notes_api.dto.response.Response;
import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.exception.UsernameAlreadyExistsException;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.note.NoteService;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Implements user endpoints.
 */
@RestController
@RequestMapping("notes-api/users")
@Tag(name = "User", description = "User endpoints")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder BCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private VerificationService verificationService;

    /**
     * Creates a new user.
     * 
     * @param dto    An {@link UserRequestDTO} containing the user credentials.
     * @param result A {@link BindingResult} containing the result of the validation
     *               checks on the {@link AuthenticationDTO}.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link UserResponseDTO}> object.
     * @throws AuthenticatedUserException     If an user tries to create another
     *                                        account
     *                                        while logged in.
     * @throws UsernameAlreadyExistsException If the provided username already
     *                                        exists in the database.
     */
    @Operation(summary = "Creates a new user")
    @PostMapping
    public ResponseEntity<Response<UserResponseDTO>> createUser(@RequestBody @Valid UserRequestDTO dto,
            BindingResult result)
            throws UsernameAlreadyExistsException {
        Response<UserResponseDTO> response = new Response<>();

        this.verificationService.verifyIfUsernameDoesNotExist(dto.getUsername());

        if (result.hasErrors()) {
            NotesApiUtil.getResponseErrorMessages(result)
                    .forEach(errorMessage -> response.setErrors(400, errorMessage));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        dto.setPassword(this.BCryptPasswordEncoder.encode(dto.getPassword()));
        User user = dto.DTOToEntity();
        user.setRole(UserRole.ROLE_USER);
        user = this.userService.save(user);

        response.setData(user.entityToDTO());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Returns the logged-in user details.
     * 
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link UserResponseDTO}> object.
     * @throws UserNotFoundException If no user was found based in the username
     *                               defined in authentication.
     */
    @Operation(summary = "Retrieves logged-in user details")
    @SecurityRequirement(name = "JWT token")
    @GetMapping
    public ResponseEntity<Response<UserResponseDTO>> getLoggedInUser() throws UserNotFoundException {
        Response<UserResponseDTO> response = new Response<>();
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        response.setData(loggedInUser.entityToDTO());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Updates the logged-in user details. After successfully updating user details,
     * the current token becomes invalid.
     * 
     * @param dto    An {@link UserRequestDTO} containing the user credentials.
     * @param result A {@link BindingResult} containing the result of the validation
     *               checks on the {@link AuthenticationDTO}.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link UserResponseDTO}> object.
     * @throws UserNotFoundException          If no user was found based in the
     *                                        username defined in authentication.
     * @throws UsernameAlreadyExistsException If the provided username already
     *                                        exists in the database.
     */
    @Operation(summary = "Updates logged-in user details (invalidates the current JWT token, a new token must be issued, unless the details are reverted to their original values)")
    @SecurityRequirement(name = "JWT token")
    @PutMapping
    public ResponseEntity<Response<UserResponseDTO>> updateLoggedInUser(@RequestBody @Valid UserRequestDTO dto,
            BindingResult result) throws UserNotFoundException, UsernameAlreadyExistsException {
        Response<UserResponseDTO> response = new Response<>();

        this.verificationService.verifyIfUsernameDoesNotExist(dto.getUsername());

        if (result.hasErrors()) {
            NotesApiUtil.getResponseErrorMessages(result)
                    .forEach(errorMessage -> response.setErrors(400, errorMessage));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        dto.setPassword(this.BCryptPasswordEncoder.encode(dto.getPassword()));
        loggedInUser.setName(dto.getName());
        loggedInUser.setUsername(dto.getUsername());
        loggedInUser.setPassword(dto.getPassword());
        this.userService.save(loggedInUser);

        response.setData(loggedInUser.entityToDTO());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Deletes the logged-in user.
     * 
     * @return A {@link ResponseEntity} with a {@link Response}<{@link String}>
     *         object.
     * @throws UserNotFoundException If no user was found based in the username
     *                               defined in authentication.
     */
    @Operation(summary = "Deletes logged-in user")
    @SecurityRequirement(name = "JWT token")
    @DeleteMapping
    public ResponseEntity<Response<String>> deleteLoggedInUser() throws UserNotFoundException {
        Response<String> response = new Response<>();

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        this.verificationService.verifyIfUserExistsByUsername(username);
        User loggedInUser = this.userService.findByUsername(username).get();

        this.noteService.deleteByUser(loggedInUser);
        this.userService.deleteById(loggedInUser.getId());

        response.setData("Your account was deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}