package uk.co.markberridge.users.dao;

import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;

import com.google.common.base.Optional;

public interface UserRepository {

    Optional<User> getUser(String userId);

    User createUser(User user) throws UserAlreadyExistsException;

    User createOrUpdateUser(User user);

    boolean deleteUser(String userId);
}