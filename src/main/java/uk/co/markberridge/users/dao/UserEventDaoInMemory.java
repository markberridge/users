package uk.co.markberridge.users.dao;

import java.util.List;

import uk.co.markberridge.users.domain.UserEvent;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class UserEventDaoInMemory implements EventDao<UserEvent> {

    private final List<UserEvent> userEvents = Lists.newArrayList();

    @Override
    public UserEvent getEvent(Long id) {
        return userEvents.get(id.intValue());
    }

    @Override
    public long getTotalNumberEvents() {
        return userEvents.size();
    }

    @Override
    public List<UserEvent> getEvents(long startEntry, long entriesPerFeed) {
        return FluentIterable.from(userEvents).skip((int) startEntry).limit((int) entriesPerFeed).toList();
    }
}
