package its.model;

public class CommentRequest {
    private String message;
    private int userId;

    public CommentRequest() {}

    public CommentRequest(String message, int userId) {
        this.message = message;
        this.userId = userId;
    }
}