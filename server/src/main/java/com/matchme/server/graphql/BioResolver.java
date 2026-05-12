package com.matchme.server.graphql;

import com.matchme.server.model.Profile;
import com.matchme.server.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BioResolver {

    @SchemaMapping(typeName = "Bio", field = "user")
    public User user(Profile profile) {
        return profile.getUser();
    }

    @SchemaMapping(typeName = "Bio", field = "id")
    public String id(Profile profile) {
        return profile.getId().toString();
    }
}