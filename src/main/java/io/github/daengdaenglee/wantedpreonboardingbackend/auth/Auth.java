package io.github.daengdaenglee.wantedpreonboardingbackend.auth;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public record Auth(Long userId) {

    public static Optional<Auth> create(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .flatMap(principal -> {
                    if (principal instanceof Long) {
                        return Optional.of((Long) principal);
                    }
                    return Optional.empty();
                })
                .map(Auth::new);
    }

}
