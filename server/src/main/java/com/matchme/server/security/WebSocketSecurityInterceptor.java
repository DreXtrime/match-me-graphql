package com.matchme.server.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.webmvc.AuthenticationWebSocketInterceptor;
import org.springframework.graphql.server.support.BearerTokenAuthenticationExtractor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor {

    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationWebSocketInterceptor authenticationWebSocketInterceptor() {
        AuthenticationManager authManager = authentication -> {
            String token = (String) authentication.getCredentials();
            if (jwtUtil.isTokenValid(token)) {
                UUID userId = jwtUtil.extractUserId(token);
                return new UsernamePasswordAuthenticationToken(userId, token, List.of());
            }
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid JWT");
        };

        return new AuthenticationWebSocketInterceptor(
                new BearerTokenAuthenticationExtractor(),
                authManager
        );
    }
}
