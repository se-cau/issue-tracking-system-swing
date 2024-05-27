package its.model;

public class UserResponse {
    private int userId;
    private String username;
    private String role;

    public UserResponse() {}

    public UserResponse(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}