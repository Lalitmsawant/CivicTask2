package dto;

import enums.IssueCategory;
import org.antlr.v4.runtime.misc.NotNull;

public class IssueCreateDTO {

    private String title;

    private String description;

    private IssueCategory category;

    private Double latitude;

    private Double longitude;

    private String address;

    private boolean anonymous = false;

    // Constructors
    public IssueCreateDTO() {}

    public IssueCreateDTO(String title, String description, IssueCategory category,
                          Double latitude, Double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public IssueCategory getCategory() { return category; }
    public void setCategory(IssueCategory category) { this.category = category; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }
}
