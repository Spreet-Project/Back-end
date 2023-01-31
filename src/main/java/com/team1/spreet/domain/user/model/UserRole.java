package com.team1.spreet.domain.user.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_WAITING_CREW("ROLE_WAITING_CREW"),
    ROLE_REJECTED_CREW("ROLE_REJECTED_CREW"),
    ROLE_APPROVED_CREW("ROLE_APPROVED_CREW"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
