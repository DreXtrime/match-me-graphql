package com.matchme.server.graphql;

import com.matchme.server.model.Profile;
import com.matchme.server.model.User;
import com.matchme.server.repository.ProfileRepository;
import com.matchme.server.repository.UserRepository;
import com.matchme.server.service.ConnectionService;
import com.matchme.server.service.RecommendationService;
import com.matchme.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class QueryResolver {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final RecommendationService recommendationService;
    private final ConnectionService connectionService;

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }

    @QueryMapping
    public User me(Authentication auth) {
        return userService.getUserEntityById(userId(auth));
    }

    @QueryMapping
    public Profile myProfile(Authentication auth) {
        return profileRepository.findByUserId(userId(auth)).orElse(null);
    }

    @QueryMapping
    public Profile myBio(Authentication auth) {
        return profileRepository.findByUserId(userId(auth)).orElse(null);
    }

    @QueryMapping
    public User user(@Argument String id, Authentication auth) {
        UUID targetId = UUID.fromString(id);
        userService.getUserById(userId(auth), targetId);
        return userService.getUserEntityById(targetId);
    }

    @QueryMapping
    public Profile profile(@Argument String id, Authentication auth) {
        UUID targetId = UUID.fromString(id);
        userService.getUserProfile(userId(auth), targetId);
        return profileRepository.findByUserId(targetId).orElse(null);
    }

    @QueryMapping
    public Profile bio(@Argument String id, Authentication auth) {
        UUID targetId = UUID.fromString(id);
        userService.getUserBio(userId(auth), targetId);
        return profileRepository.findByUserId(targetId).orElse(null);
    }

    @QueryMapping
    public List<User> recommendations(Authentication auth) {
        UUID id = userId(auth);
        return recommendationService.getRecommendations(id)
                .recommendations()
                .stream()
                .map(userService::getUserEntityById)
                .toList();
    }

    @QueryMapping
    public List<User> connections(Authentication auth) {
        UUID id = userId(auth);
        return connectionService.getConnections(id)
                .connections()
                .stream()
                .map(userService::getUserEntityById)
                .toList();
    }
}