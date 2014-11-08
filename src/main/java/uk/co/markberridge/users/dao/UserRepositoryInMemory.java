package uk.co.markberridge.users.dao;

import java.util.Map;

import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.User.UserBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class UserRepositoryInMemory implements UserRepository {

    private final Map<String, User> users = Maps.newConcurrentMap();

    public UserRepositoryInMemory() {
        createUser(new UserBuilder().username("admin").build());
    }

    public Optional<User> getUser(String username) {
        return Optional.fromNullable(users.get(username));
    }

    public User createUser(User user) throws UserAlreadyExistsException {
        Preconditions.checkNotNull(user);
        if (users.get(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        return createOrUpdateUser(user);
    }

    public User createOrUpdateUser(User user) {
        Preconditions.checkNotNull(user);
        users.put(user.getUsername(), user);
        return user;
    }

    public boolean deleteUser(String userId) {
        return users.remove(userId) != null;
    }
}
