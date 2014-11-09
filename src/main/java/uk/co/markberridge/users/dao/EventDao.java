package uk.co.markberridge.users.dao;

import java.util.List;

public interface EventDao<E> {

    public abstract E getEvent(Long id);

    public abstract long getTotalNumberEvents();

    public abstract List<E> getEvents(long startEntry, long entriesPerFeed);

}