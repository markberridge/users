package uk.co.markberridge.users;

public class UsersRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsersRuntimeException(String message) {
        super(message);
    }

    public UsersRuntimeException(Throwable cause) {
        super(cause);
    }

    public UsersRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
