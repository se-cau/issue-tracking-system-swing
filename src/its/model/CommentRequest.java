package its.model;

public class CommentRequest {
    private String message;
    private int authorId;

    public CommentRequest() {}

    public CommentRequest(String message, int authorId) {
        this.message = message;
        this.authorId = authorId;
    }
}