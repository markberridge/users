package uk.co.markberridge.users.dao;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.markberridge.users.UsersApplication;
import uk.co.markberridge.users.domain.UserEvent;

public class UserEventDaoHibernateTest extends AbstractHibernateTestCase {
    
    private static final long LARGE_NUMBER_OF_EVENTS = 20;
    
    private UserEventDaoHibernate userEventDao;
    
    private UserDao userDao;

    @Rule
    public HibernateRule hibernateRule = new HibernateRule(UsersApplication.PERSISTENT_CLASSES);

    @Before
    public void before() {
        
        userEventDao = new UserEventDaoHibernate(hibernateRule.getSessionFactory());
        userDao = new UserDaoHibernate(hibernateRule.getSessionFactory());

//        hibernateRule.beginTransaction();
//        hibernateRule.getSession().createSQLQuery("DELETE FROM USER_EVENT").executeUpdate();
//        hibernateRule.getSession().createSQLQuery("DELETE FROM USERS").executeUpdate();
//        hibernateRule.commit();

        hibernateRule.beginTransaction();
    }

    @After
    public void after() {
        hibernateRule.rollback();
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

    // @Test
    // public void testSaveOneEvent() {
    // Attendance attendance = LitUkAttendance.newAttendance()
    // .attendeeId("xx")
    // .testName("aaa")
    // .testDay(new Date())
    // .supervisor(AuthenticationSource.GENESES, "username", "id")
    // .surname("sur")
    // .dob(new LocalDate())
    // .forename("fore")
    // .postcode("post")
    // .additionalInfo(
    // AttendanceTestDataFactory.createAdditionalInfo("bob", "surname",
    // 23))
    // .build();
    //
    // attendanceDao.saveAttendance(attendance);
    //
    // Result r = result(attendance, State.PASSED, 0);
    // userDao.persistResult(r);
    //
    // UserEvent event = new UserEvent(r);
    // assertThat(event.getId()).isEqualTo(0);
    //
    // userDao.persistResult(r);
    // userEventDao.persistEvent(event);
    //
    // assertThat(event.getId()).isNotEqualTo(0);
    // hibernateRule.commitAndCloseSession();
    //
    // hibernateRule.beginTransaction();
    //
    // assertThat(userEventDao.getTotalNumberEvents()).isEqualTo(1);
    //
    // List<UserEvent> events = userEventDao.getEvents(0, 200);
    //
    // assertThat(events).hasSize(1);
    //
    // UserEvent persistentEvent = events.get(0);
    // assertThat(persistentEvent.getId()).isEqualTo(event.getId());
    // assertThat(persistentEvent.getResult()).isNotNull();
    // }
    //
    // @Test
    // public void testGetEventsRange() {
    // List<UserEvent> events = userEventDao.getEvents(-100, -20);
    // assertThat(events).isEmpty();
    // }
    //
    // @Test
    // public void testSaveManyEvents() {
    // for (int i = 0; i < LARGE_NUMBER_OF_EVENTS; i++) {
    // persistResultAndEvent(i);
    // }
    // hibernateRule.commitAndCloseSession();
    // hibernateRule.beginTransaction();
    //
    // assertThat(userEventDao.getTotalNumberEvents()).isEqualTo(LARGE_NUMBER_OF_EVENTS);
    //
    // List<UserEvent> events = userEventDao.getEvents(0, 15);
    // assertThat(events).hasSize(15);
    // assertThat(events.get(0).getResult().getPassMark()).isEqualTo(new BigDecimal("0.00"));
    // assertThat(events.get(14).getResult().getPassMark()).isEqualTo(new BigDecimal("14.00"));
    //
    // events = userEventDao.getEvents(15, 15);
    // assertThat(events).hasSize(5);
    //
    // assertThat(events.get(0).getResult().getPassMark()).isEqualTo(new BigDecimal("15.00"));
    // assertThat(events.get(4).getResult().getPassMark()).isEqualTo(new BigDecimal("19.00"));
    // }
    //
    // private void persistResultAndEvent(int idx) {
    //
    // Attendance attendance = LitUkAttendance.newAttendance()
    // .attendeeId("" + idx)
    // .bookingId(UUID.randomUUID().toString())
    // .testName("aaa")
    // .testDay(new Date())
    // .supervisor(AuthenticationSource.GENESES, "username", "id")
    // .surname("sur")
    // .dob(new LocalDate())
    // .forename("fore")
    // .postcode("post")
    // .additionalInfo(
    // AttendanceTestDataFactory.createAdditionalInfo("bob", "surname",
    // idx))
    // .build();
    // attendance.setSpecialRequirement(specialReq(true, false, "12", false, Reason.DYSCALCULIA, Reason.HEARING));
    // attendanceDao.saveAttendance(attendance);
    // Result r = result(attendance, State.PASSED, idx);
    // userDao.persistResult(r);
    // userEventDao.persistEvent(new UserEvent(r));
    // }

}
