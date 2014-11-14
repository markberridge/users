package uk.co.markberridge.users.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.markberridge.users.UsersApplication;
import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.User.UserBuilder;
import uk.co.markberridge.users.domain.UserEvent;

public class UserEventDaoHibernateTest extends AbstractHibernateTestCase {

    private static final long LARGE_NUMBER_OF_EVENTS = 20;

    private UserEventDaoHibernate userEventDao;
    private UserDao userDao;

    @Rule
    public HibernateRule hibernateRule = new HibernateRule(UsersApplication.PERSISTENT_CLASSES);

    @Before
    public void setUp() {

        userEventDao = new UserEventDaoHibernate(hibernateRule.getSessionFactory());
        userDao = new UserDaoHibernate(hibernateRule.getSessionFactory());

        hibernateRule.beginTransaction();
        hibernateRule.getSession().createSQLQuery("DELETE FROM USER_EVENT").executeUpdate();
        hibernateRule.getSession().createSQLQuery("DELETE FROM USERS").executeUpdate();
        hibernateRule.commit();

        hibernateRule.beginTransaction();
    }

    @After
    public void after() {
        hibernateRule.commit();
    }

    @Test
    public void shouldReportEmptyEventListCorrectly() {
        assertThat(userEventDao.getTotalNumberEvents()).isEqualTo(0);
    }

    @Test
    public void shouldReturnEmptyEventListWhenThereAreNoEvents() {
        List<UserEvent> events = userEventDao.getEvents(0, 20);
        assertThat(events).isEmpty();
    }

    @Test
    public void testSaveOneEvent() {

        User seedUser = userDao.createUser(new UserBuilder().username("seed").password("password").build());

        UserEvent event = new UserEvent(seedUser);
        assertThat(event.getId()).isEqualTo(0);

        userEventDao.persistEvent(event);

        assertThat(event.getId()).isNotEqualTo(0);
        hibernateRule.commitAndCloseSession();

        hibernateRule.beginTransaction();

        assertThat(userEventDao.getTotalNumberEvents()).isEqualTo(1);

        List<UserEvent> events = userEventDao.getEvents(0, 200);

        assertThat(events).hasSize(1);

        UserEvent persistentEvent = events.get(0);
        assertThat(persistentEvent.getId()).isEqualTo(event.getId());
        assertThat(persistentEvent.getUser()).isNotNull();
    }

    @Test
    public void testGetEventsRange() {
        List<UserEvent> events = userEventDao.getEvents(-100, -20);
        assertThat(events).isEmpty();
    }

    @Test
    public void testSaveManyEvents() {
        for (int i = 0; i < LARGE_NUMBER_OF_EVENTS; i++) {
            persistUserAndEvent(i);
        }
        hibernateRule.commitAndCloseSession();
        hibernateRule.beginTransaction();

        assertThat(userEventDao.getTotalNumberEvents()).isEqualTo(LARGE_NUMBER_OF_EVENTS);

        List<UserEvent> events = userEventDao.getEvents(0, 15);
        assertThat(events).hasSize(15);
        assertThat(events.get(0).getUser().getUsername()).isEqualTo("user0");
        assertThat(events.get(14).getUser().getUsername()).isEqualTo("user14");

        events = userEventDao.getEvents(15, 15);
        assertThat(events).hasSize(5);

        assertThat(events.get(0).getUser().getUsername()).isEqualTo("user15");
        assertThat(events.get(4).getUser().getUsername()).isEqualTo("user19");
    }

    private void persistUserAndEvent(int i) {
        User u = new UserBuilder().username("user" + i).build();
        userDao.createUser(u);
        userEventDao.persistEvent(new UserEvent(u));
    }

}
