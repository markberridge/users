package uk.co.markberridge.users.dao;

import com.google.common.base.Optional;
import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;

public class UserDaoMongo implements UserDao {

    @Override
    public Optional<User> getUserByUsername(String userId) {
        return null;
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        return null;
    }

    @Override
    public User createOrUpdateUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUserByUsername(String userId) {
        return false;
    }
}
