package dto;

import enums.IssueCategory;
import enums.IssueStatus;

public class IssueFilterDTO {
    private Double userLatitude;
    private Double userLongitude;
    private Double radiusKm = 3.0;
    private IssueStatus status;
    private IssueCategory category;
    private boolean includeHidden = false;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private int page = 0;
    private int size = 20;

    // Constructors
    public IssueFilterDTO() {}

    // Getters and Setters
    public Double getUserLatitude() { return userLatitude; }
    public void setUserLatitude(Double userLatitude) { this.userLatitude = userLatitude; }

    public Double getUserLongitude() { return userLongitude; }
    public void setUserLongitude(Double userLongitude) { this.userLongitude = userLongitude; }

    public Double getRadiusKm() { return radiusKm; }
    public void setRadiusKm(Double radiusKm) { this.radiusKm = radiusKm; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public IssueCategory getCategory() { return category; }
    public void setCategory(IssueCategory category) { this.category = category; }

    public boolean isIncludeHidden() { return includeHidden; }
    public void setIncludeHidden(boolean includeHidden) { this.includeHidden = includeHidden; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
