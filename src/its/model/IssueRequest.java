package its.model;

public class IssueRequest {
    private String title;
    private String description;
    private int assigneeid;
    private Status status;
    private Priority priority;
    private int userid;
    public IssueRequest() {}

    public IssueRequest(String title, String description, int assigneeid, Status status, Priority priority, int userid) {
        this.title = title;
        this.description = description;
        this.assigneeid = assigneeid;
        this.status = status;
        this.priority = priority;
        this.userid = userid;
    }

    public IssueRequest(String title, String description, Priority priority, int userid) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.priority = priority;
        this.userid = userid;
    }
}