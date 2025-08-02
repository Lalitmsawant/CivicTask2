package dto;

import enums.IssueCategory;
import enums.IssueStatus;

import java.util.Map;

public class AnalyticsDTO {

    private long totalIssues;
    private long totalUsers;
    private long resolvedIssues;
    private long pendingIssues;
    private long flaggedIssues;
    private Map<IssueCategory, Long> issuesByCategory;
    private Map<IssueStatus, Long> issuesByStatus;
    private Map<String, Long> issuesPerMonth;
    private double averageResolutionTimeHours;

    // Constructors
    public AnalyticsDTO() {}

    // Getters and Setters
    public long getTotalIssues() { return totalIssues; }
    public void setTotalIssues(long totalIssues) { this.totalIssues = totalIssues; }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getResolvedIssues() { return resolvedIssues; }
    public void setResolvedIssues(long resolvedIssues) { this.resolvedIssues = resolvedIssues; }

    public long getPendingIssues() { return pendingIssues; }
    public void setPendingIssues(long pendingIssues) { this.pendingIssues = pendingIssues; }

    public long getFlaggedIssues() { return flaggedIssues; }
    public void setFlaggedIssues(long flaggedIssues) { this.flaggedIssues = flaggedIssues; }

    public Map<IssueCategory, Long> getIssuesByCategory() { return issuesByCategory; }
    public void setIssuesByCategory(Map<IssueCategory, Long> issuesByCategory) {
        this.issuesByCategory = issuesByCategory;
    }

    public Map<IssueStatus, Long> getIssuesByStatus() { return issuesByStatus; }
    public void setIssuesByStatus(Map<IssueStatus, Long> issuesByStatus) {
        this.issuesByStatus = issuesByStatus;
    }

    public Map<String, Long> getIssuesPerMonth() { return issuesPerMonth; }
    public void setIssuesPerMonth(Map<String, Long> issuesPerMonth) {
        this.issuesPerMonth = issuesPerMonth;
    }

    public double getAverageResolutionTimeHours() { return averageResolutionTimeHours; }
    public void setAverageResolutionTimeHours(double averageResolutionTimeHours) {
        this.averageResolutionTimeHours = averageResolutionTimeHours;
    }
}
