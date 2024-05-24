package its.model;

public class RegisterRequest {
    private String username;
    private String password;
    private String role;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}