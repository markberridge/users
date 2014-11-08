package uk.co.markberridge.users.api;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class Representation {

    public static final String REL_SELF = "self";

    protected Map<String, Link> links = Maps.newHashMap();

    public Representation() {
        super();
    }

    public Representation(Link... links) {
        for (Link link : links) {
            this.links.put(link.getRel(), link);
        }
    }

    protected Link getLinkByName(String uri) {
        if (uri == null) {
            return null;
        }
        return links.get(uri);
    }

    public Collection<Link> getLinks() {
        return links.values();
    }
}
