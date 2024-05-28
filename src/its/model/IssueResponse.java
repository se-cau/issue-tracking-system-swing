package its.model;

public class IssueResponse {
    private int id;
    private String title;
    private String description;
    private String reporter;
    private String assignee;
    private String fixer;
    private Status status;
    private Priority priority;
    private String created_at;
    private String updated_at;

    public IssueResponse() {}

    public IssueResponse(int id, String title, String description, String reporter, String assignee, String fixer, Status status, Priority priority, String created_at, String updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.assignee = assignee;
        this.fixer = fixer;
        this.status = status;
        this.priority = priority;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReporter() {
        return reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getFixer() {
        return fixer;
    }

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}