package com.matchme.server.graphql;

import com.matchme.server.dto.request.LoginRequest;
import com.matchme.server.dto.request.RegisterRequest;
import com.matchme.server.dto.request.UpdateBioRequest;
import com.matchme.server.dto.request.UpdateProfileRequest;
import com.matchme.server.dto.response.AuthResponse;
import com.matchme.server.dto.response.SimpleResponse;
import com.matchme.server.model.Profile;
import com.matchme.server.service.AuthService;
import com.matchme.server.service.ConnectionService;
import com.matchme.server.service.ProfileService;
import com.matchme.server.model.enums.FridayNightActivity;
import com.matchme.server.model.enums.Interest;
import com.matchme.server.model.enums.MusicGenre;
import com.matchme.server.model.enums.RelationshipGoal;
import com.matchme.server.utils.GravatarUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MutationResolver {

    private final AuthService authService;
    private final ProfileService profileService;
    private final ConnectionService connectionService;

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }

    @MutationMapping
    public AuthResponse register(@Argument String email, @Argument String password) {
        return authService.register(new RegisterRequest(email, password));
    }

    @MutationMapping
    public AuthResponse login(@Argument String email, @Argument String password) {
        return authService.login(new LoginRequest(email, password));
    }

    @MutationMapping
    public SimpleResponse updateProfile(
            @Argument String firstName,
            @Argument String lastName,
            @Argument String aboutMe,
            @Argument Integer maxDistanceKm,
            @Argument Double latitude,
            @Argument Double longitude,
            Authentication auth) {
        return profileService.updateProfile(userId(auth), new UpdateProfileRequest(
                firstName,
                lastName,
                aboutMe,
                maxDistanceKm,
                latitude != null ? BigDecimal.valueOf(latitude) : null,
                longitude != null ? BigDecimal.valueOf(longitude) : null
        ));
    }

    @MutationMapping
    public SimpleResponse updateBio(
            @Argument Integer age,
            @Argument List<String> interests,
            @Argument List<String> fridayNightActivities,
            @Argument List<String> musicGenres,
            @Argument String relationshipGoal,
            Authentication auth) {
        return profileService.updateBio(userId(auth), new UpdateBioRequest(
                age,
                interests.stream().map(Interest::valueOf).toList(),
                fridayNightActivities.stream().map(FridayNightActivity::valueOf).toList(),
                musicGenres.stream().map(MusicGenre::valueOf).toList(),
                RelationshipGoal.valueOf(relationshipGoal)
        ));
    }

    @MutationMapping
    public SimpleResponse sendConnectionRequest(@Argument String id, Authentication auth) {
        return connectionService.sendRequest(userId(auth), UUID.fromString(id));
    }

    @MutationMapping
    public SimpleResponse acceptConnectionRequest(@Argument String id, Authentication auth) {
        return connectionService.acceptRequest(userId(auth), UUID.fromString(id));
    }

    @MutationMapping
    public SimpleResponse declineConnectionRequest(@Argument String id, Authentication auth) {
        return connectionService.declineRequest(userId(auth), UUID.fromString(id));
    }

    @MutationMapping
    public SimpleResponse deleteConnection(@Argument String id, Authentication auth) {
        return connectionService.disconnect(userId(auth), UUID.fromString(id));
    }

    @MutationMapping
    public SimpleResponse dismissRecommendation(@Argument String id, Authentication auth) {
        return profileService.dismissRecommendation(userId(auth), UUID.fromString(id));
    }

    @SchemaMapping(typeName = "Profile", field = "profilePicture")
    public String profilePicture(Profile profile) {
        return GravatarUtils.getGravatarUrl(profile.getUser().getEmail());
    }
}