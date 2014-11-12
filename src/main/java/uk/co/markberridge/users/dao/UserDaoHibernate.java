package uk.co.markberridge.users.dao;

import io.dropwizard.hibernate.AbstractDAO;

import java.io.Serializable;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.User.UserBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class UserDaoHibernate extends AbstractDAO<User> implements UserDao {

    public UserDaoHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    // TODO remove this method
    protected User get(Serializable id) {
        Preconditions.checkNotNull(id, "id is null");
        return super.get(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        Preconditions.checkNotNull(username, "username is null");
        Query query = this.currentSession().createQuery("from User u where u.username = :username");
        query.setString("username", username);
        return Optional.fromNullable(uniqueResult(query));
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        Preconditions.checkNotNull(user, "user is null");
        if (getUserByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUsername());
        }
        return persist(user);
    }

    @Override
    public User createOrUpdateUser(User user) {
        Preconditions.checkNotNull(user, "user is null");
        Optional<User> retrievedUser = getUserByUsername(user.getUsername());
        if (retrievedUser.isPresent()) {
            currentSession().evict(retrievedUser.get());
            return persist(new UserBuilder().from(retrievedUser.get())
                                            .username(user.getUsername())
                                            .password(user.getPassword())
                                            .build());
        }
        return persist(user);
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        Preconditions.checkNotNull(username, "username is null");
        Optional<User> user = getUserByUsername(username);
        if (user.isPresent()) {
            currentSession().delete(user.get());
            return true;
        }
        return false;
    }
}
