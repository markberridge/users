package uk.co.markberridge.users.event;

import uk.co.markberridge.users.UsersException;

public class EventFeedGenerationException extends UsersException {

    private static final long serialVersionUID = 1L;

    public EventFeedGenerationException(String message) {
        super(message);
    }

}
