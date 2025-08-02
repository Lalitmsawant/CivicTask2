package entity;


import enums.IssueCategory;
import enums.IssueStatus;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IssueCategory category;

    @Enumerated(EnumType.STRING)
    private IssueStatus status = IssueStatus.REPORTED;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    private boolean anonymous = false;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IssuePhoto> photos = new ArrayList<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlaggedReport> flags = new ArrayList<>();

    private boolean hidden = false;

    private int flagCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == IssueStatus.RESOLVED && resolvedAt == null) {
            resolvedAt = LocalDateTime.now();
        }
    }

    // Constructors
    public Issue() {}

    public Issue(String title, String description, IssueCategory category,
                 Double latitude, Double longitude, User reporter) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reporter = reporter;
    }

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

    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }

    public List<IssuePhoto> getPhotos() { return photos; }
    public void setPhotos(List<IssuePhoto> photos) { this.photos = photos; }

    public List<StatusHistory> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<StatusHistory> statusHistory) { this.statusHistory = statusHistory; }

    public List<FlaggedReport> getFlags() { return flags; }
    public void setFlags(List<FlaggedReport> flags) { this.flags = flags; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }

    public int getFlagCount() { return flagCount; }
    public void setFlagCount(int flagCount) { this.flagCount = flagCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}
