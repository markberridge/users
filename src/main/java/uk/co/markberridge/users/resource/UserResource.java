package uk.co.markberridge.users.resource;

import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import uk.co.markberridge.users.activity.CreateUserActivity;
import uk.co.markberridge.users.activity.DeleteUserActivity;
import uk.co.markberridge.users.activity.ReadUserActivity;
import uk.co.markberridge.users.api.UserRepresentation;
import uk.co.markberridge.users.dao.UserDao;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/user")
public class UserResource {

    private final UserDao dao;

    public UserResource(UserDao dao) {
        this.dao = dao;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Timed
    public Response getUser(@PathParam("username") String username) {
        Optional<UserRepresentation> user = new ReadUserActivity(dao, uriInfo).retrieveByUsername(username);
        if (!user.isPresent()) {
            return Response.status(Status.NOT_FOUND).entity("user with username [" + username + "] does not exist")
                    .build();
        }
        return Response.ok().entity(user.get()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Timed
    public Response createUser(UserRepresentation user) {
        UserRepresentation responseUser = new CreateUserActivity(dao).create(user);
        return Response.created(UriBuilder.fromResource(getClass()).path(responseUser.getUsername()).build())
                .entity(responseUser).build();
    }

    @PUT
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Timed
    public Response putUser(@PathParam("username") String username, UserRepresentation user) {
        if (!username.equals(user.getUsername())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("username in URI does not match username in entity").build();
        }
        UserRepresentation responseUser = new CreateUserActivity(dao).createOrUpdate(user);
        return Response.created(UriBuilder.fromResource(getClass()).path(responseUser.getUsername()).build())
                .entity(responseUser).build();
    }

    @DELETE
    @Path("/{username}")
    @UnitOfWork
    @Timed
    public Response deleteUser(@PathParam("username") String userId) {
        new DeleteUserActivity(dao).delete(userId);
        return Response.noContent().build();
    }
}
