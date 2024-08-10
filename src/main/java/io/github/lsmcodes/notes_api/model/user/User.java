package io.github.lsmcodes.notes_api.model.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.lsmcodes.notes_api.dto.model.user.UserDTO;
import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.note.Note;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* Implements an User entity
*/
@Entity(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id", "username" })
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Note> notes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().toString()));
    }

    /**
     * Converts an User entity to an User DTO
     * 
     * @return UserDTO
     */
    public UserDTO entityToDTO() {
        return new ModelMapper().map(this, UserDTO.class);
    }

}