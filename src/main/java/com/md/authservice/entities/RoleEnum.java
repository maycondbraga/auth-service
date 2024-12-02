package com.md.authservice.entities;

public enum RoleEnum {
    USER("Default user role"),
    ADMIN("Administrator role"),
    SUPER_ADMIN("Super Administrator role");

    public final String description;

    RoleEnum(String description) {
        this.description = description;
    }
}
