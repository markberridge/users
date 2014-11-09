package uk.co.markberridge.users.event;

import java.net.URI;

import org.apache.commons.lang.StringUtils;

public class EventFeedUriFactory {
    private final int entriesPerFeed;

    public EventFeedUriFactory(int entriesPerFeed) {
        this.entriesPerFeed = entriesPerFeed;
    }

    public String generateCanonicalUri(URI uri, long startFrom) {
        return getServiceUri(uri) + "/" + startFrom + "," + (startFrom + entriesPerFeed - 1);
    }

    public String getServiceUri(URI uri) {

        String scheme = uri.getScheme();
        String hostname = uri.getHost();
        int port = uri.getPort();

        String path = StringUtils.removeEnd(uri.getPath(), "/");
        path = path.substring(0, path.lastIndexOf("/"));
        if (port != 80 && port != -1) {
            return scheme + "://" + hostname + ":" + port + path;
        }
        return scheme + "://" + hostname + path;
    }

    public String getPageUri(URI uri, long startFrom) {
        return getServiceUri(uri) + "/" + startFrom + "," + (startFrom + entriesPerFeed - 1);
    }

    public String getEventSelfUri(URI uri, long eventId) {
        return getServiceUri(uri) + "/" + eventId;
    }
}