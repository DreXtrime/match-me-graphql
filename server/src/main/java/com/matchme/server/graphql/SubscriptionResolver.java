package com.matchme.server.graphql;

import com.matchme.server.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SubscriptionResolver {

    private final ConnectionEventPublisher connectionEventPublisher;

    @SubscriptionMapping
    public Flux<User> connectionRequestReceived(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        return connectionEventPublisher.getConnectionRequests(userId);
    }
}