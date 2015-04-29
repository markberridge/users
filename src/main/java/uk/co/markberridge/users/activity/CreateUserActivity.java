package uk.co.markberridge.users.activity;

import uk.co.markberridge.users.api.UserRepresentation;
import uk.co.markberridge.users.dao.UserDao;

public class CreateUserActivity {

    private final UserDao dao;

    public CreateUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public UserRepresentation create(UserRepresentation user) {
        return new UserRepresentation(dao.createUser(user.buildNewUser()));
    }

    public UserRepresentation createOrUpdate(UserRepresentation user) {
        return new UserRepresentation(dao.createOrUpdateUser(user.buildNewUser()));
    }
}
