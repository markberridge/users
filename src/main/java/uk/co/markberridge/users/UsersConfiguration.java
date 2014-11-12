package uk.co.markberridge.users;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.co.markberridge.users.event.EventFeedConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsersConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("migrationDatabase")
    private DataSourceFactory migrationDataSourceFactory = new DataSourceFactory();

    public DataSourceFactory getApplicationDataSourceFactory() {
        return applicationDataSourceFactory;
    }

    public DataSourceFactory getMigrationDataSourceFactory() {
        return migrationDataSourceFactory;
    }

    @Valid
    @NotNull
    @JsonProperty("applicationDatabase")
    private DataSourceFactory applicationDataSourceFactory = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty
    EventFeedConfiguration eventFeedConfiguration = new EventFeedConfiguration();

    public EventFeedConfiguration getEventFeedConfiguration() {
        return eventFeedConfiguration;
    }
}
