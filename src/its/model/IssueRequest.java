package its.model;

public class IssueRequest {
    private String title;
    private String description;
    private int assigneeid;
    private Status status;
    private int userid;

    public IssueRequest() {}

    public IssueRequest(String title, String description, int assigneeid, Status status, int userid) {
        this.title = title;
        this.description = description;
        this.assigneeid = assigneeid;
        this.status = status;
        this.userid = userid;
    }
}