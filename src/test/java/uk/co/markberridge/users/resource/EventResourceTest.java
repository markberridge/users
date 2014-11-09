package uk.co.markberridge.users.resource;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Feed;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import uk.co.markberridge.users.event.Event;
import uk.co.markberridge.users.event.EventDao;
import uk.co.markberridge.users.event.EventFeedConfiguration;
import uk.co.markberridge.users.event.EventFeedGenerator;
import uk.co.markberridge.users.event.EventFeedUriFactory;
import uk.co.markberridge.users.event.EventType;
import uk.co.markberridge.users.provider.ContextInjectableProvider;

import com.google.common.primitives.Longs;

public class EventResourceTest {

    private final static EventFeedConfiguration configuration = new EventFeedConfiguration();

    @SuppressWarnings("unchecked")
    private final static EventDao<MockEvent> eventDao = mock(EventDao.class);

    private final static EventFeedUriFactory feedUriFactory = new EventFeedUriFactory(20);

    private static EventFeedGenerator<MockEvent> eventGenerator = new EventFeedGenerator<>(MockEvent.class,
            EventType.USER, eventDao, configuration, feedUriFactory);

    private final static EventResource<MockEvent> genericEventResource = new EventResource<>(eventGenerator);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(genericEventResource)
            .addProvider(new ContextInjectableProvider<UriInfo>(UriInfo.class)).build();

    @After
    @SuppressWarnings("unchecked")
    public void afterTest() {
        reset(eventDao);
    }

    @Test
    public void checkEmptyFeedSuccessfullyGET() {
        Feed recentFeed = resources.client().resource("/events/recent").get(Feed.class);

        assertThat(recentFeed.getEntries()).isEmpty();
        // test client retrieves as relative url so we can't extract out url
        // properly
        assertThat(recentFeed.getLink("self").getHref()).isEqualTo(new IRI("null://null/events/0,19"));
    }

    @Test
    public void checkRecentFeedReturnsLast20Events() {

        List<MockEvent> seedData = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            seedData.add(new MockEvent(i));
        }

        List<MockEvent> firstPageResults = seedData.subList(0, 20);

        when(Long.valueOf(eventDao.getTotalNumberEvents())).thenReturn(Long.valueOf(seedData.size()));

        when(eventDao.getEvents(20, 20)).thenReturn(firstPageResults);

        Feed recentFeed = resources.client().resource("/events/recent").get(Feed.class);

        // test client retrieves as relative url so we can't extract out url
        // properly
        assertThat(recentFeed.getLinks()).hasSize(2);
        assertThat(recentFeed.getLink("self").getHref()).isEqualTo(new IRI("null://null/events/20,39"));
        assertThat(recentFeed.getLink("prev-archive").getHref()).isEqualTo(new IRI("null://null/events/0,19"));

        assertThat(recentFeed.getEntries()).hasSize(20);
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.NONE)
    private static class MockEvent implements Event<MockEvent> {

        @XmlElement
        private Long id;

        private final LocalDateTime createddate = new LocalDateTime();
        private final String tagUri = "urn:uuid:" + UUID.randomUUID().toString();

        @SuppressWarnings("unused")
        MockEvent() {
            super();
        }

        public MockEvent(int id) {
            this.id = Long.valueOf(id);
        }

        public int compareTo(MockEvent o) {
            return Longs.compare(o.id, this.id);
        }

        public long getId() {
            return id;
        }

        public String getTagUri() {
            return tagUri;
        }

        public LocalDateTime getCreatedDate() {
            return createddate;
        }

        public String getEventType() {
            return "MOCK";
        }
    }
}