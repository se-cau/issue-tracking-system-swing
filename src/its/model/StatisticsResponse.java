package its.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsResponse {
    private Map<String, Integer> statusDistribution = new HashMap<>();
    private Map<String, Integer> reporterDistribution = new HashMap<>();
    private Map<String, Integer> assigneeDistribution = new HashMap<>();
    private List<String> topCommentedIssues;

    @JsonAnyGetter
    public Map<String, Integer> getStatusDistribution() {
        return statusDistribution;
    }

    @JsonAnySetter
    public void setStatusDistribution(Map<String, Integer> statusDistribution) {
        this.statusDistribution = statusDistribution;
    }

    @JsonAnyGetter
    public Map<String, Integer> getReporterDistribution() {
        return reporterDistribution;
    }

    @JsonAnySetter
    public void setReporterDistribution(Map<String, Integer> reporterDistribution) {
        this.reporterDistribution = reporterDistribution;
    }

    @JsonAnyGetter
    public Map<String, Integer> getAssigneeDistribution() {
        return assigneeDistribution;
    }

    @JsonAnySetter
    public void setAssigneeDistribution(Map<String, Integer> assigneeDistribution) {
        this.assigneeDistribution = assigneeDistribution;
    }

    public List<String> getTopCommentedIssues() {
        return topCommentedIssues;
    }

    public void setTopCommentedIssues(List<String> topCommentedIssues) {
        this.topCommentedIssues = topCommentedIssues;
    }
}
