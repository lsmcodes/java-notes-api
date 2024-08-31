package io.github.lsmcodes.notes_api.dto.model.note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.github.lsmcodes.notes_api.model.note.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements a Data Transfer Object (DTO) for responses related to {@link Note}.
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDTO {

    private UUID id;

    private List<String> tags;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}