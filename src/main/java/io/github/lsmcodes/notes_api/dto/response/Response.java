package io.github.lsmcodes.notes_api.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Implements a generic response for endpoints.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private T data;

    /**
     * Sets error details and timestamp.
     * 
     * @param status The http status code.
     * @param message The error message.
     */
    public void setErrors(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

}