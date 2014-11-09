package uk.co.markberridge.users.event;

public enum EventType {

    USER("User Events");

    private String label;

    public String getLabel() {
        return label;
    }

    private EventType(String label) {
        this.label = label;
    }
}
