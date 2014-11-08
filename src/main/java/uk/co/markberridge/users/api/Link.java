package uk.co.markberridge.users.api;

import java.net.URI;

import javax.ws.rs.HttpMethod;

public class Link {

    private final String rel;
    private final URI uri;
    private final String method;

    public Link(String rel, URI uri, String method) {
        this.rel = rel;
        this.uri = uri;
        this.method = method;
    }

    public Link(String rel, URI uri) {
        this(rel, uri, HttpMethod.GET);
    }

    public String getRel() {
        return rel;
    }

    public URI getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }
}
