package its.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
public enum Status {
    NEW("NEW"),
    ASSIGNED("ASSIGNED"),
    FIXED("FIXED"),
    RESOLVED("RESOLVED"),
    CLOSE("CLOSE"),
    REOPENED("REOPENED");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Status forValue(String value) {
        for (Status status : Status.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
