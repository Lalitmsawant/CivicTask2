package enums;

public enum UserRole {
    CITIZEN("Citizen"),
    ADMIN("Administrator"),
    MODERATOR("Moderator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
