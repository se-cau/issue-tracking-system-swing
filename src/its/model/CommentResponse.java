package its.model;

import its.network.DateFormatter;

import javax.management.relation.Role;
import java.util.Date;

public class CommentResponse {

    private int id;
    private String message;
    private String createdAt;
    private int authorId;
    private String username;
    private String role;

    public CommentResponse() {}

    public CommentResponse(int id, String message, String createdAt, int authorId, String username, String role) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.authorId = authorId;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return DateFormatter.formatDateString(createdAt);
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}