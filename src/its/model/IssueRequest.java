package its.model;

public class IssueRequest {
    private String title;
    private String description;
    private int assigneeId;
    private Status status;
    private Priority priority;
    private int userId;
    public IssueRequest() {}

    public IssueRequest(String title, String description, int assigneeId, Status status, Priority priority, int userId) {
        this.title = title;
        this.description = description;
        this.assigneeId = assigneeId;
        this.status = status;
        this.priority = priority;
        this.userId = userId;
    }

    public IssueRequest(String title, String description, Priority priority, int userId) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.priority = priority;
        this.userId = userId;
    }
}