package io.github.lsmcodes.notes_api.dto.model.user;

import java.util.UUID;

import io.github.lsmcodes.notes_api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implements a Data Transfer Object (DTO) for requests related to {@link User}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;

    private String name;

    private String username;

    private String password;

    private String role;

}