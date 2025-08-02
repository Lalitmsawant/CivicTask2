package enums;

public enum IssueStatus {
    REPORTED("Reported"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    FLAGGED("Flagged");

    private final String displayName;

    IssueStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
