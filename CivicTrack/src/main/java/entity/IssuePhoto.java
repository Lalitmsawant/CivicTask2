package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues_photos")
public class IssuePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fileName;


    private String filePath;

    private String contentType;

    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // Constructors
    public IssuePhoto() {}

    public IssuePhoto(String fileName, String filePath, String contentType, Long fileSize, Issue issue) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.issue = issue;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
