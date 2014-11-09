package uk.co.markberridge.users.event;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.StringWriter;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Generator;
import org.apache.abdera.model.Link;
import org.apache.abdera.model.Person;
import org.joda.time.LocalDateTime;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class EventFeedGenerator<T extends Event<T>> {

    public static final String SELF = "self";

    private static final LocalDateTime EARLIER_THAN_ANY_EVENT_TIME = new LocalDateTime(1900, 1, 1, 0, 0, 0);

    private static final String PREV_ARCHIVE = "prev-archive";
    private static final String NEXT_ARCHIVE = "next-archive";
    private static final String VIA = "via";
    private static final String EVENT_FEED_AUTHOR = "CAP";
    private static final String PRODUCING_SERVICE = "CAP";
    private static final String MEDIA_TYPE = "application/atom+xml";
    private final JAXBContext context;

    // private final Class<T> clazz;
    private final EventType type;
    private final EventFeedUriFactory uriFactory;
    private final int entriesPerFeed;
    private final EventDao<T> eventDao;
    private final static Factory abderaFactory = Abdera.getNewFactory();

    public EventFeedGenerator(Class<T> clazz, EventType type, EventDao<T> eventDao,
            EventFeedConfiguration eventFeedConfiguration, EventFeedUriFactory uriFactory) {

        checkArgument(eventFeedConfiguration.getEntriesPerFeed() > 0,
                "Entries per event feed in config needs to be > 0");

        // this.clazz = clazz;
        this.type = type;
        this.eventDao = eventDao;
        this.entriesPerFeed = eventFeedConfiguration.getEntriesPerFeed();
        this.uriFactory = uriFactory;
        try {
            this.context = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw Throwables.propagate(e);
        }
    }

    public Feed getRecentFeed(URI uri) {

        long startEntry = findStartingEntryForHeadFeeds();
        Feed working = feedFor(uri, startEntry);
        working.addLink(uriFactory.generateCanonicalUri(uri, startEntry), SELF);

        for (Link link : generatePagingLinks(uri, startEntry)) {
            working.addLink(link);
        }

        return working;
    }

    public Feed getWorkingFeed(URI uri) {

        long startEntry = findStartingEntryForHeadFeeds();
        Feed recent = feedFor(uri, startEntry);

        Link self = abderaFactory.newLink();
        self.setHref(uri.toString());
        self.setRel(SELF);
        self.setMimeType(MEDIA_TYPE);
        recent.addLink(self);

        for (Link link : generatePagingLinks(uri, startEntry)) {
            recent.addLink(link);
        }

        Link via = abderaFactory.newLink();
        via.setHref(uriFactory.generateCanonicalUri(uri, startEntry));
        via.setRel(VIA);
        via.setMimeType(MEDIA_TYPE);
        recent.addLink(via);

        return recent;
    }

    public Feed getArchiveFeed(URI uri, int startEntry) {

        Feed archive = feedFor(uri, startEntry);

        Link self = abderaFactory.newLink();
        self.setHref(uriFactory.generateCanonicalUri(uri, startEntry));
        self.setRel(SELF);
        self.setMimeType(MEDIA_TYPE);
        archive.addLink(self);

        for (Link link : generatePagingLinks(uri, startEntry)) {
            archive.addLink(link);
        }
        return archive;
    }

    public boolean invalidStartAndEndEntries(int startPos, int endPos) {

        return startPos % entriesPerFeed != 0 || endPos != startPos + entriesPerFeed - 1;
    }

    public boolean workingFeedRequested(int startPos) {

        final long numberOfEvents = eventDao.getTotalNumberEvents();
        return startPos <= numberOfEvents && numberOfEvents <= startPos + entriesPerFeed;
    }

    private long findStartingEntryForHeadFeeds() {

        long totalNumberOfEvents = eventDao.getTotalNumberEvents();

        long numberOfEventsInWorkingFeed = totalNumberOfEvents % entriesPerFeed;
        if (numberOfEventsInWorkingFeed == 0) {
            numberOfEventsInWorkingFeed = entriesPerFeed;
        }

        return Math.max(0, totalNumberOfEvents - numberOfEventsInWorkingFeed);
    }

    private Feed feedFor(URI uri, long startEntry) {

        Feed feed = abderaFactory.newFeed();

        feed.setId(UUID.randomUUID().toString());
        feed.setTitle(type.getLabel());

        final Generator generator = abderaFactory.newGenerator();
        generator.setUri(uriFactory.getServiceUri(uri));
        generator.setText(PRODUCING_SERVICE);
        feed.setGenerator(generator);
        feed.addAuthor(generateAuthorsList());

        List<T> events = eventDao.getEvents(startEntry, entriesPerFeed);

        Collections.sort(events);

        for (Entry entry : createEntries(uri, events)) {
            feed.addEntry(entry);
        }
        feed.setUpdated(newestEventDate(events).toDate());

        return feed;
    }

    private List<Link> generatePagingLinks(URI uri, long currentFeedStart) {

        List<Link> links = Lists.newArrayList();

        if (hasNewerFeed(currentFeedStart)) {
            Link next = abderaFactory.newLink();
            next.setRel(NEXT_ARCHIVE);
            next.setMimeType(MEDIA_TYPE);
            next.setHref(uriFactory.getPageUri(uri, currentFeedStart + entriesPerFeed));
            links.add(next);
        }

        if (hasOlderFeed(currentFeedStart)) {
            Link prev = abderaFactory.newLink();
            prev.setRel(PREV_ARCHIVE);
            prev.setMimeType(MEDIA_TYPE);
            prev.setHref(uriFactory.getPageUri(uri, currentFeedStart - entriesPerFeed));
            links.add(prev);
        }

        return links;
    }

    private boolean hasOlderFeed(long currentPosition) {
        return currentPosition - entriesPerFeed >= 0;
    }

    private boolean hasNewerFeed(long startFrom) {
        return startFrom + entriesPerFeed < eventDao.getTotalNumberEvents();
    }

    private Person generateAuthorsList() {

        final Person person = abderaFactory.newAuthor();
        person.setName(EVENT_FEED_AUTHOR);
        return person;
    }

    private List<Entry> createEntries(URI uri, List<T> events) {

        List<Entry> entries = Lists.newArrayList();
        for (T e : events) {
            entries.add(createEntry(uri, e));
        }
        return entries;
    }

    private LocalDateTime newestEventDate(List<T> events) {

        LocalDateTime date = EARLIER_THAN_ANY_EVENT_TIME;
        for (T e : events) {
            if (e.getCreatedDate().isAfter(date)) {
                date = e.getCreatedDate();
            }
        }
        return date;
    }

    public Entry getFeedEntry(URI uri, int entryId) throws EventFeedGenerationException {

        T attendanceValidationEvent = eventDao.getEvent(new Long(entryId));

        if (attendanceValidationEvent == null) {
            throw new EventFeedGenerationException("Unable to find atom entry " + new Long(entryId));
        }
        return createEntry(uri, attendanceValidationEvent);
    }

    public Entry createEntry(URI uri, T attendanceValidationEvent) {

        final Entry entry = abderaFactory.newEntry();
        entry.setId(attendanceValidationEvent.getTagUri());
        entry.setTitle(attendanceValidationEvent.getEventType());
        entry.setUpdated(attendanceValidationEvent.getCreatedDate().toDate());

        for (Link link : generateLinks(uri, attendanceValidationEvent)) {
            entry.addLink(link);
        }

        entry.setContent(toXmlString(attendanceValidationEvent), Content.Type.XML);

        return entry;
    }

    private List<Link> generateLinks(URI uri, T event) {

        List<Link> links = Lists.newArrayList();

        Link self = abderaFactory.newLink();
        self.setHref(uriFactory.getEventSelfUri(uri, event.getId()));

        self.setRel(EventFeedGenerator.SELF);
        links.add(self);

        return links;
    }

    protected Marshaller newMarshaller() throws JAXBException {
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        return marshaller;
    }

    // protected Class<?>[] getMarshallingClasses() {
    // return new Class<?>[] {};
    // }

    private String toXmlString(T event) {

        try {
            StringWriter stringWriter = new StringWriter();
            newMarshaller().marshal(event, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to marshal event into XML.", e);
        }
    }

}
