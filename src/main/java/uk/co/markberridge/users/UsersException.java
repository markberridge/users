package uk.co.markberridge.users;

public class UsersException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsersException(String message) {
        super(message);
    }

    public UsersException(Throwable cause) {
        super(cause);
    }

    public UsersException(String message, Throwable cause) {
        super(message, cause);
    }
}
