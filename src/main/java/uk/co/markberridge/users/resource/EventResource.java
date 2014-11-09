package uk.co.markberridge.users.resource;

import io.dropwizard.hibernate.UnitOfWork;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import uk.co.markberridge.users.event.Event;
import uk.co.markberridge.users.event.EventFeedGenerationException;
import uk.co.markberridge.users.event.EventFeedGenerator;
import uk.co.markberridge.users.event.EventType;

import com.codahale.metrics.annotation.Timed;

@Path("/events")
public class EventResource<T extends Event<T>> {

    private @Context
    UriInfo uriInfo;

    private final EventFeedGenerator<T> generator;

    public EventResource(EventFeedGenerator<T> eventGenerator) {
        this.generator = eventGenerator;
    }

    @GET
    @Timed
    @Path("/recent")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @UnitOfWork(readOnly = true)
    public Response getRecentFeed() {
        Feed feed = generator.getRecentFeed(uriInfo.getRequestUri());
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        return Response.ok().cacheControl(cc).entity(feed).build();
    }

    @GET
    @Timed
    @Path("/{startPos},{endPos}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @UnitOfWork(readOnly = true)
    public Response getSpecificFeed(@PathParam("startPos") int startPos, @PathParam("endPos") int endPos) {

        if (generator.invalidStartAndEndEntries(startPos, endPos)) {
            // Bad URI - the paramters don't align with our feeds
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (generator.workingFeedRequested(startPos)) {
            return getWorkingFeed();
        }

        CacheControl cc = new CacheControl();
        cc.setNoCache(false);
        cc.setMaxAge((int) TimeUnit.DAYS.toSeconds(365));
        Feed feed = generator.getArchiveFeed(uriInfo.getRequestUri(), startPos);
        return Response.ok().cacheControl(cc).entity(feed).build();
    }

    @GET
    @Timed
    @Path("/{entryId}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @UnitOfWork(readOnly = true)
    public Response getSpecificEntry(@PathParam("type") EventType type, @PathParam("entryId") int entryId)
            throws EventFeedGenerationException {

        Entry entry = generator.getFeedEntry(uriInfo.getRequestUri(), entryId);
        return Response.ok().entity(entry).build();
    }

    private Response getWorkingFeed() {

        Feed feed = generator.getWorkingFeed(uriInfo.getRequestUri());

        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        return Response.ok().cacheControl(cc).entity(feed).build();
    }
}
