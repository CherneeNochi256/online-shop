package ru.maxim.effectivemobiletesttask.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    ORG_OWNER,
    FROZEN;

    @Override
    public String getAuthority() {
        return name();
    }
}

