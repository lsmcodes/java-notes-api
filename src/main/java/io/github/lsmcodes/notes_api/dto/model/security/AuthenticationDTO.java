package io.github.lsmcodes.notes_api.dto.model.security;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements a Data Transfer Object (DTO) for authentication
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDTO {

    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;

}