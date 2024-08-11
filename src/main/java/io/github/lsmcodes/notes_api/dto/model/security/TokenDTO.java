package io.github.lsmcodes.notes_api.dto.model.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
* Implements a token Data Transfer Object (DTO)
*/
@AllArgsConstructor
public class TokenDTO {

    @Getter
    private String token;

}