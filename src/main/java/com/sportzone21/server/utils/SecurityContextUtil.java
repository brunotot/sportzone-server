package com.sportzone21.server.utils;

import com.sportzone21.server.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextUtil {

    private static final String ANONYMOUS_USER_TAG = "anonymousUser";

    public static User getUser() {
        Authentication auth = Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElse(null);
        return auth == null ? null : (User) auth.getPrincipal();
    }

}
