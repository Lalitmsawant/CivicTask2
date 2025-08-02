package enums;

public enum IssueCategory {
    ROADS("Roads", "Potholes, obstructions"),
    LIGHTING("Lighting", "Broken or flickering lights"),
    WATER_SUPPLY("Water Supply", "Leaks, low pressure"),
    CLEANLINESS("Cleanliness", "Overflowing bins, garbage"),
    PUBLIC_SAFETY("Public Safety", "Open manholes, exposed wiring"),
    OBSTRUCTIONS("Obstructions", "Fallen trees, debris");

    private final String displayName;
    private final String description;

    IssueCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
