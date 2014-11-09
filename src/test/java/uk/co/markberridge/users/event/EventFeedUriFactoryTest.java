package uk.co.markberridge.users.event;

import static org.fest.assertions.api.Assertions.assertThat;

import java.net.URI;

import org.junit.Test;

public class EventFeedUriFactoryTest {

    private EventFeedUriFactory factory = new EventFeedUriFactory(20);

    // TODO Add test for relative uri and fix

    @Test
    public void checkUrlDoesNotEndWithSlash() {
        String uri = factory.generateCanonicalUri(
                URI.create("http://localhost:8380/api/events/ATTENDANCE_VALIDATION/recent"), 1);
        assertThat(uri).isEqualTo("http://localhost:8380/api/events/ATTENDANCE_VALIDATION/1,20");
    }

    @Test
    public void checkUrlEndsWithSlash() {
        String uri = factory.generateCanonicalUri(
                URI.create("http://localhost:8380/api/events/ATTENDANCE_VALIDATION/recent/"), 1);
        assertThat(uri).isEqualTo("http://localhost:8380/api/events/ATTENDANCE_VALIDATION/1,20");
    }

}
