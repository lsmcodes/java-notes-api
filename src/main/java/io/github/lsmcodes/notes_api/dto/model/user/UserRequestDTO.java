package io.github.lsmcodes.notes_api.dto.model.user;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import io.github.lsmcodes.notes_api.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implements a Data Transfer Object (DTO) for responses related to {@link User}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Length(min = 10, max = 255, message = "Password must contain between 10 and 255 characters")
    private String password;

    /**
     * Converts the current UserDTO entity to a {@link User}.
     * 
     * @return A {@link User} instance representing the current UserDTO.
     */
    public User DTOToEntity() {
        return new ModelMapper().map(this, User.class);
    }

}