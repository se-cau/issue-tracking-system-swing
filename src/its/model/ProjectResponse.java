package its.model;

import java.util.List;

public class ProjectResponse {
    private int projectId;
    private String title;
    private String adminName;
    private List<String> contributorNames;

    public ProjectResponse() {}
    public ProjectResponse(int projectId, String title, String adminName, List<String> contributorNames) {
        this.projectId = projectId;
        this.title = title;
        this.adminName = adminName;
        this.contributorNames = contributorNames;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getAdminName() {
        return adminName;
    }

    public List<String> getContributorNames() {
        return contributorNames;
    }
}
