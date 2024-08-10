package io.github.lsmcodes.notes_api.dto.model.note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import io.github.lsmcodes.notes_api.model.note.Note;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements a Data Transfer Object (DTO) for {@link Note}.
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {

    private UUID id;
    private List<String> tags;

    @NotNull(message = "Title cannot be null")
    @Length(max = 100, message = "Title must contain 100 characters or less")
    private String title;

    @NotNull(message = "Content cannot be null")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Converts the current NoteDTO entity to a {@link Note}.
     * 
     * @return A {@link Note} instance representing the current NoteDTO.
     */
    public Note DTOToEntity() {
        return new ModelMapper().map(this, Note.class);
    }

}