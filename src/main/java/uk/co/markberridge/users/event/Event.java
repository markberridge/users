package uk.co.markberridge.users.event;

import org.joda.time.LocalDateTime;

public interface Event<THIS> extends Comparable<THIS> {

    long getId();

    String getTagUri();

    LocalDateTime getCreatedDate();

    String getEventType();
}