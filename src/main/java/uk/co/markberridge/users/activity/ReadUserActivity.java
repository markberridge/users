package uk.co.markberridge.users.activity;

import javax.ws.rs.core.UriInfo;

import uk.co.markberridge.users.api.UserRepresentation;
import uk.co.markberridge.users.dao.UserRepository;
import uk.co.markberridge.users.domain.User;

import com.google.common.base.Optional;

public class ReadUserActivity {

    private final UserRepository userRepository;

    @SuppressWarnings("unused")
    private final UriInfo uriInfo;

    public ReadUserActivity(UserRepository userRepository, UriInfo uriInfo) {
        this.userRepository = userRepository;
        this.uriInfo = uriInfo;
    }

    public Optional<UserRepresentation> retrieveByUsername(String username) {
        Optional<User> user = userRepository.getUser(username);
        if (user.isPresent()) {
            return Optional.of(new UserRepresentation(user.get()));
        }
        return Optional.<UserRepresentation> absent();
    }
}
