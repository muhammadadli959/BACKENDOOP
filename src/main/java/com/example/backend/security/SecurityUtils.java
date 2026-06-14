package com.example.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.backend.exception.UnauthorizedException;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof JwtUserDetails) {
            return ((JwtUserDetails) auth.getPrincipal()).getId();
        }
        throw new UnauthorizedException("Unauthorized");
    }
}
