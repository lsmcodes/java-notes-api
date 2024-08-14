package io.github.lsmcodes.notes_api.dto.model.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
* Implements Data Transfer Object (DTO) for token.
*/
@AllArgsConstructor
public class TokenDTO {

    @Getter
    private String token;

}