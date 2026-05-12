package com.matchme.server.graphql;

import com.matchme.server.model.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Component
public class ConnectionEventPublisher {

    private final Sinks.Many<ConnectionRequestEvent> sink =
            Sinks.many().multicast().onBackpressureBuffer(256, false);

    public void publishConnectionRequest(UUID receiverId, User requester) {
        sink.tryEmitNext(new ConnectionRequestEvent(receiverId, requester));
    }

    public Flux<User> getConnectionRequests(UUID userId) {
        return sink.asFlux()
                .filter(event -> event.receiverId().equals(userId))
                .map(ConnectionRequestEvent::requester);
    }

    public record ConnectionRequestEvent(UUID receiverId, User requester) {}
}
