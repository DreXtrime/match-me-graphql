package com.matchme.server.graphql;

import com.matchme.server.model.Profile;
import com.matchme.server.model.User;
import com.matchme.server.repository.ProfileRepository;
import com.matchme.server.utils.GravatarUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserResolver {

    private final ProfileRepository profileRepository;

    @SchemaMapping(typeName = "User", field = "name")
    public String name(User user) {
        return profileRepository.findByUserId(user.getId())
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse(null);
    }

    @SchemaMapping(typeName = "User", field = "profilePicture")
    public String profilePicture(User user) {
        return GravatarUtils.getGravatarUrl(user.getEmail());
    }

    @SchemaMapping(typeName = "User", field = "email")
    public String email(User user, Authentication auth) {
        if (auth == null) return null;
        UUID requesterId = (UUID) auth.getPrincipal();
        return user.getId().equals(requesterId) ? user.getEmail() : null;
    }

    @SchemaMapping(typeName = "User", field = "bio")
    public Profile bio(User user) {
        return profileRepository.findByUserId(user.getId()).orElse(null);
    }

    @SchemaMapping(typeName = "User", field = "profile")
    public Profile profile(User user) {
        return profileRepository.findByUserId(user.getId()).orElse(null);
    }
}