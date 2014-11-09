package uk.co.markberridge.users;

import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.co.markberridge.users.event.EventFeedConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    EventFeedConfiguration eventFeedConfiguration = new EventFeedConfiguration();

    public EventFeedConfiguration getEventFeedConfiguration() {
        return eventFeedConfiguration;
    }
}
