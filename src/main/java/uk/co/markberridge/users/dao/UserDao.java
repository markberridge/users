package uk.co.markberridge.users.dao;

import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;

import com.google.common.base.Optional;

public interface UserDao {

    Optional<User> getUserByUsername(String userId);

    User createUser(User user) throws UserAlreadyExistsException;

    User createOrUpdateUser(User user);

    boolean deleteUserByUsername(String userId);
}