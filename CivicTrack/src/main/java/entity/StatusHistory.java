package entity;

import enums.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "staatus_history")
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Enumerated(EnumType.STRING)
    private IssueStatus previousStatus;

    @Enumerated(EnumType.STRING)
    private IssueStatus newStatus;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_id")
    private User changedBy;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        changedAt = LocalDateTime.now();
    }

    // Constructors
    public StatusHistory() {}

    public StatusHistory(Issue issue, IssueStatus previousStatus, IssueStatus newStatus, String comment, User changedBy) {
        this.issue = issue;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.comment = comment;
        this.changedBy = changedBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }

    public IssueStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(IssueStatus previousStatus) { this.previousStatus = previousStatus; }

    public IssueStatus getNewStatus() { return newStatus; }
    public void setNewStatus(IssueStatus newStatus) { this.newStatus = newStatus; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public User getChangedBy() { return changedBy; }
    public void setChangedBy(User changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

}
