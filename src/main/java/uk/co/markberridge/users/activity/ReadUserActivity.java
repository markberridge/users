package uk.co.markberridge.users.activity;

import javax.ws.rs.core.UriInfo;

import uk.co.markberridge.users.api.UserRepresentation;
import uk.co.markberridge.users.dao.UserDao;
import uk.co.markberridge.users.domain.User;

import com.google.common.base.Optional;

public class ReadUserActivity {

    private final UserDao dao;

    @SuppressWarnings("unused")
    private final UriInfo uriInfo;

    public ReadUserActivity(UserDao dao, UriInfo uriInfo) {
        this.dao = dao;
        this.uriInfo = uriInfo;
    }

    public Optional<UserRepresentation> retrieveByUsername(String username) {
        Optional<User> user = dao.getUserByUsername(username);
        if (user.isPresent()) {
            return Optional.of(new UserRepresentation(user.get()));
        }
        return Optional.<UserRepresentation> absent();
    }
}
