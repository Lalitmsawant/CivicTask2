package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flagged_reports")
public class FlaggedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flagged_by_id")
    private User flaggedBy;


    private String reason;

    private String description;

    private boolean reviewed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @Column(name = "flagged_at")
    private LocalDateTime flaggedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        flaggedAt = LocalDateTime.now();
    }

    // Constructors
    public FlaggedReport() {}

    public FlaggedReport(Issue issue, User flaggedBy, String reason, String description) {
        this.issue = issue;
        this.flaggedBy = flaggedBy;
        this.reason = reason;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }

    public User getFlaggedBy() { return flaggedBy; }
    public void setFlaggedBy(User flaggedBy) { this.flaggedBy = flaggedBy; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isReviewed() { return reviewed; }
    public void setReviewed(boolean reviewed) { this.reviewed = reviewed; }

    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }

    public LocalDateTime getFlaggedAt() { return flaggedAt; }
    public void setFlaggedAt(LocalDateTime flaggedAt) { this.flaggedAt = flaggedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}
