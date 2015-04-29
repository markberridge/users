package uk.co.markberridge.users.dao;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import uk.co.markberridge.users.UserAlreadyExistsException;
import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.User.UserBuilder;

import com.google.common.base.Optional;

public class UserDaoHibernateTest extends AbstractHibernateTestCase {

//    private UserDaoHibernate userDao;
    //
    //    @Rule
    //    public HibernateRule hibernateRule = new HibernateRule(User.class);
    //
    //    @BeforeClass
    //    public static void reset() {
    //        AbstractHibernateTestCase.reset();
    //    }
    //
    //    @Override
    //    @Before
    //    public void setUp() {
    //        userDao = new UserDaoHibernate(hibernateRule.getSessionFactory());
    //        if (initialise()) {
    //            hibernateRule.beginTransaction();
    //            seedData();
    //            hibernateRule.commit();
    //        }
    //        hibernateRule.beginTransaction();
    //    }
    //
    //    @After
    //    public void after() {
    //        hibernateRule.commit();
    //    }
    //
    //    public void seedData() {
    //        // Also test createUser
    //        User seed = new UserBuilder().username("seed").password("password").build();
    //        userDao.createUser(seed);
    //        User seed1 = new UserBuilder().username("seed1").password("password").build();
    //        userDao.createUser(seed1);
    //    }
    //
    //    @Test
    //    public void getUserByUsername() {
    //        Optional<User> user = userDao.getUserByUsername("admin");
    //        assertTrue(user.isPresent());
    //        assertThat(user.get().getId()).isEqualTo("1");
    //        assertThat(user.get().getUsername()).isEqualTo("admin");
    //        assertThat(user.get().getPassword()).isEqualTo("letmein");
    //    }
    //
    //    @Test
    //    public void get() {
    //        User user = userDao.get("1");
    //        assertThat(user).isNotNull();
    //        assertThat(user.getId()).isEqualTo("1");
    //        assertThat(user.getUsername()).isEqualTo("admin");
    //        assertThat(user.getPassword()).isEqualTo("letmein");
    //    }
    //
    //    @Test(expected = UserAlreadyExistsException.class)
    //    public void createUser_duplicate() {
    //        User user = new UserBuilder().username("seed").password("different-password").build();
    //        userDao.createUser(user);
    //    }
    //
    //    @Test
    //    public void createOrUpdateUser() {
    //        User user = new UserBuilder().username("seed").password("different-password").build();
    //        userDao.createOrUpdateUser(user);
    //
    //        Optional<User> retrievedUser = userDao.getUserByUsername("seed");
    //        assertTrue(retrievedUser.isPresent());
    //        assertThat(retrievedUser.get().getId()).isNotNull();
    //        assertThat(retrievedUser.get().getUsername()).isEqualTo("seed");
    //        assertThat(retrievedUser.get().getPassword()).isEqualTo("different-password");
    //    }
    //
    //    @Test
    //    public void deleteUser() {
    //
    //        Optional<User> user = userDao.getUserByUsername("seed1");
    //        assertThat(user.isPresent()).isTrue();
    //
    //        assertThat(userDao.deleteUserByUsername("seed1")).isTrue();
    //
    //        user = userDao.getUserByUsername("seed1");
    //        assertThat(user.isPresent()).isFalse();
    //
    //        assertThat(userDao.deleteUserByUsername("seed1")).isFalse();
    //    }
    //
}
