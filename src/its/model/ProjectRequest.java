package its.model;

import java.util.List;

public class ProjectRequest {
    private String title;
    private int adminId;
    private List<Integer> contributorIds;
    private ProjectRequest() {}

    public ProjectRequest(String title, int adminId, List<Integer> contributorIds) {
        this.title = title;
        this.adminId = adminId;
        this.contributorIds = contributorIds;
    }
}