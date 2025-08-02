package dto;

import enums.IssueCategory;
import enums.IssueStatus;

import java.time.LocalDateTime;
import java.util.List;

public class IssueDTO {
    private Long id;
    private String title;
    private String description;
    private IssueCategory category;
    private IssueStatus status;
    private Double latitude;
    private Double longitude;
    private String address;
    private String reporterName;
    private boolean anonymous;
    private List<String> photoUrls;

    private int flagCount;
    private boolean hidden;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private Double distanceKm;

    // Constructors
    public IssueDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public IssueCategory getCategory() { return category; }
    public void setCategory(IssueCategory category) { this.category = category; }

    public IssueStatus getStatus() { return status; }
    public void setStatus(IssueStatus status) { this.status = status; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }


    public int getFlagCount() { return flagCount; }
    public void setFlagCount(int flagCount) { this.flagCount = flagCount; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

}
