package its.model;

public class CommentRequest {
    private String message;
    private int authorid;

    public CommentRequest() {}

    public CommentRequest(String message, int authorid) {
        this.message = message;
        this.authorid = authorid;
    }
}