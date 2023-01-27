package com.team1.spreet.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_UNACCEPTED_CREW("ROLE_UNACCEPTED_CREW"),
    ROLE_ACCEPTED_CREW("ROLE_ACCEPTED_CREW"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
