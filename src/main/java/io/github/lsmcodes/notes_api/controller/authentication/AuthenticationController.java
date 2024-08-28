package io.github.lsmcodes.notes_api.controller.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.lsmcodes.notes_api.dto.model.security.AuthenticationDTO;
import io.github.lsmcodes.notes_api.dto.model.security.TokenDTO;
import io.github.lsmcodes.notes_api.dto.response.Response;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.security.TokenService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Implements authentication endpoints.
 */
@RestController
@RequestMapping("notes-api/authentication")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VerificationService verificationService;

    /**
     * Authenticates an user and generates a JWT token.
     * 
     * @param dto    An {@link AuthenticationDTO} containing user credentials.
     * @param result A {@link BindingResult} containing the result of the validation
     *               checks on the {@link AuthenticationDTO}.
     * @return A {@link ResponseEntity} with a
     *         {@link Response}<{@link TokenDTO}> object.
     * @throws UserNotFoundException 
     */
    @Operation(summary = "Authenticates an user and generates a JWT token")
    @PostMapping
    public ResponseEntity<Response<TokenDTO>> login(@RequestBody @Valid AuthenticationDTO dto, BindingResult result) throws UserNotFoundException {
        Response<TokenDTO> response = new Response<>();

        if(result.hasErrors()) {
            NotesApiUtil.getResponseErrorMessages(result).forEach(errorMessage -> response.setErrors(401, errorMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String username = dto.getUsername();
        this.verificationService.verifyIfUserExistsByUsername(username);

        var usernamePassword = new UsernamePasswordAuthenticationToken(username, dto.getPassword());
        var authentication = this.authenticationManager.authenticate(usernamePassword);
        String token = this.tokenService.generateToken((User) authentication.getPrincipal());
        response.setData(new TokenDTO(token));

        return ResponseEntity.ok().body(response);
    }

}