package its.model;

import its.network.DateFormatter;

public class IssueResponse {
    private int id;
    private String title;
    private String description;
    private String reporter;
    private String assignee;
    private String fixer;
    private Status status;
    private Priority priority;
    private String createdAt;
    private String updatedAt;

    public IssueResponse() {}

    public IssueResponse(int id, String title, String description, String reporter, String assignee, String fixer, Status status, Priority priority, String createdAt, String updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.reporter = reporter;
        this.assignee = assignee;
        this.fixer = fixer;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updated_at;
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

    public String getCreatedAt() {
        return DateFormatter.formatDateString(createdAt);
    }

    public String getUpdatedAt() {
        return DateFormatter.formatDateString(updatedAt);
    }

}