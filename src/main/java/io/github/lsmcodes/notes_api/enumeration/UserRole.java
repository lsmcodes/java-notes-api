package io.github.lsmcodes.notes_api.enumeration;

/**
 * Represents different roles a user can have in the system.
 */
public enum UserRole {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String role;

    private UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }

}