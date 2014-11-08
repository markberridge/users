package uk.co.markberridge.users.activity;

import uk.co.markberridge.users.api.UserRepresentation;
import uk.co.markberridge.users.dao.UserRepository;

public class CreateUserActivity {

    private final UserRepository userRepository;

    public CreateUserActivity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepresentation create(UserRepresentation user) {
        return new UserRepresentation(userRepository.createUser(user.buildNewUser()));
    }

    public UserRepresentation createOrUpdate(UserRepresentation user) {
        return new UserRepresentation(userRepository.createOrUpdateUser(user.buildNewUser()));
    }
}
