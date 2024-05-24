package its.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
public enum Priority {
    BLOCKER("BLOCKER"),
    CRITICAL("CRITICAL"),
    MAJOR("MAJOR"),
    MINOR("MINOR"),
    TRIVIAL("TRIVIAL");

    private final String value;

    Priority(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Priority forValue(String value) {
        for (Priority priority : Priority.values()) {
            if (priority.value.equals(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}