package io.github.lsmcodes.notes_api.model.note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import io.github.lsmcodes.notes_api.dto.model.note.NoteDTO;
import io.github.lsmcodes.notes_api.model.user.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements a Note entity
*/
@Entity(name = "notes")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "note_id"))
    @Column(name = "tag", length = 30, nullable = false)
    private List<String> tags;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Converts a Note entity to a Note DTO
     * 
     * @return NoteDTO
     */
    public NoteDTO entityToDTO() {
        return new ModelMapper().map(this, NoteDTO.class);
    }

}