package its.model;

public class LoginResponse {
    private int userId;
    private String username;

    public LoginResponse() {}

    public LoginResponse(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
