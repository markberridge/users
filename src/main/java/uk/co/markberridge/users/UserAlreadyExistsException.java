package uk.co.markberridge.users;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class UserAlreadyExistsException extends UsersRuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String username) {
        super("User with username [" + username + "] already exists");
    }

    @Provider
    public static class Mapper implements ExceptionMapper<UserAlreadyExistsException> {
        public Response toResponse(UserAlreadyExistsException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
