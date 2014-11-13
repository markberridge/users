package uk.co.markberridge.users.dao;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import uk.co.markberridge.users.domain.UserEvent;

public class UserEventDaoHibernate extends AbstractDAO<UserEvent> implements EventDao<UserEvent> {

    public UserEventDaoHibernate(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserEvent persistEvent(UserEvent atomEvent) {
        return super.persist(atomEvent);
    }

    @Override
    public UserEvent getEvent(Long id) {
        return super.get(id);
    }
    
    @Override
    public long getTotalNumberEvents() {
        return ((Long) criteria().setProjection(Projections.rowCount()).uniqueResult()).longValue();
    }

    @Override
    public List<UserEvent> getEvents(long startIndex, long maximumNumberOfEventsToFetch) {

        long firstEntryIndex = Math.max(0, startIndex);
        
        Query query = currentSession().createQuery("select e from UserEvent e order by e.id");
        query.setFirstResult((int)firstEntryIndex).setMaxResults(numberOfEventsToFetch(maximumNumberOfEventsToFetch, firstEntryIndex));
        return query.list();
    }

    private int numberOfEventsToFetch(long maximumNumberOfEventsToFetch, long firstEntryIndex) {
        final long eventCount = getTotalNumberEvents();
        long numberOfEventsToFetch = maximumNumberOfEventsToFetch;

        if (firstEntryIndex + numberOfEventsToFetch > eventCount) {
            numberOfEventsToFetch = eventCount - firstEntryIndex;
        }
        return (int) numberOfEventsToFetch;
    }

}