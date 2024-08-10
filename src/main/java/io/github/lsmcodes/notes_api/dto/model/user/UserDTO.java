package io.github.lsmcodes.notes_api.dto.model.user;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.util.security.BCryptUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements an User Data Transfer Object (DTO)
*/
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Getter
    private UUID id;

    @Getter
    @NotNull(message = "Name cannot be null")
    private String name;

    @Getter
    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Length(min = 10, max = 255, message = "Password must contain between 10 and 255 characters")
    private String password;

    @Getter
    @NotNull(message = "Role cannot be null")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$", message = "Role must be ROLE_ADMIN or ROLE_USER")
    private String role;

    @Getter
    private List<Note> notes;

    /**
     * Returns encrypted password
     * 
     * @return String
     */
    public String getPassword() {
        return BCryptUtil.getHash(password);
    }

    /**
     * Converts an User DTO to a User entity
     * 
     * @return User
     */
    public User DTOToEntity() {
        return new ModelMapper().map(this, User.class);
    }

}