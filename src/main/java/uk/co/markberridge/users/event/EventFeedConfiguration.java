package uk.co.markberridge.users.event;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventFeedConfiguration {

    @Range(min = 1L)
    @JsonProperty
    private int entriesPerFeed = 20;

    public int getEntriesPerFeed() {
        return entriesPerFeed;
    }
}
