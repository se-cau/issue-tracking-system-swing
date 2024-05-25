package its.model;

public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String code;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(String timestamp, int status, String error, String code, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}