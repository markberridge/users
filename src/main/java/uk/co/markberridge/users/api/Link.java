package uk.co.markberridge.users.api;

import java.net.URI;

import javax.ws.rs.HttpMethod;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Link {

    @XmlAttribute
    private String rel;

    @XmlAttribute
    private URI href;

    @XmlAttribute
    private String method;

    @SuppressWarnings("unused")
    private Link() {
        // marshaling
    }

    public Link(String rel, URI href, String method) {
        this.rel = rel;
        this.href = href;
        this.method = method;
    }

    public Link(String rel, URI href) {
        this(rel, href, HttpMethod.GET);
    }

    public String getRel() {
        return rel;
    }

    public URI getHref() {
        return href;
    }

    public String getMethod() {
        return method;
    }
}
