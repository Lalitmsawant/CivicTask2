package service;

import dto.*;
import dto.IssueDTO;
import entity.*;
import enums.IssueStatus;
import repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import repository.FlaggedReportRepository;
import repository.IssuePhotoRepository;
import repository.IssueRepository;
import repository.StatusHistoryRepository;
import util.LocationUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssuePhotoRepository photoRepository;

    @Autowired
    private StatusHistoryRepository statusHistoryRepository;

    @Autowired
    private FlaggedReportRepository flaggedReportRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private LocationService locationService;

//    @Autowired
//    private NotificationService notificationService;

    /**
     * Create a new issue
     */
    public IssueDTO createIssue(IssueCreateDTO createDTO, User reporter, List<MultipartFile> photos) {
        // Validate location
        if (!locationService.isValidLocation(createDTO.getLatitude(), createDTO.getLongitude())) {
            throw new IllegalArgumentException("Invalid location coordinates");
        }

        // Create issue entity
        Issue issue = new Issue();
        issue.setTitle(createDTO.getTitle());
        issue.setDescription(createDTO.getDescription());
        issue.setCategory(createDTO.getCategory());
        issue.setLatitude(createDTO.getLatitude());
        issue.setLongitude(createDTO.getLongitude());
        issue.setAddress(createDTO.getAddress());
        issue.setReporter(createDTO.isAnonymous() ? null : reporter);
        issue.setAnonymous(createDTO.isAnonymous());
        issue.setStatus(IssueStatus.REPORTED);

        Issue savedIssue = issueRepository.save(issue);

        // Create initial status history
        StatusHistory initialStatus = new StatusHistory(
                savedIssue, null, IssueStatus.REPORTED, "Issue reported", reporter
        );
        statusHistoryRepository.save(initialStatus);

        // Handle photo uploads
        if (photos != null && !photos.isEmpty()) {
            uploadPhotos(savedIssue, photos);
        }

        return convertToDTO(savedIssue);
    }

    /**
     * Get issues within radius with filters
     */
    public Page<IssueDTO> getIssuesWithinRadius(IssueFilterDTO filterDTO) {
        // Validate and adjust radius
        double radius = locationService.getValidatedRadius(filterDTO.getRadiusKm());

        // Create pageable
        Sort sort = Sort.by(
                filterDTO.getSortDirection().equalsIgnoreCase("desc") ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                filterDTO.getSortBy()
        );
        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        // Query issues
        Page<Issue> issues = issueRepository.findIssuesWithinRadius(
                filterDTO.getUserLatitude(),
                filterDTO.getUserLongitude(),
                radius,
                filterDTO.getCategory(),
                filterDTO.getStatus(),
                filterDTO.isIncludeHidden(),
                pageable
        );

        // Convert to DTOs and calculate distances
        return issues.map(issue -> {
            IssueDTO dto = convertToDTO(issue);
            if (filterDTO.getUserLatitude() != null && filterDTO.getUserLongitude() != null) {
                double distance = LocationUtils.calculateDistance(
                        filterDTO.getUserLatitude(), filterDTO.getUserLongitude(),
                        issue.getLatitude(), issue.getLongitude()
                );
                dto.setDistanceKm(Math.round(distance * 100.0) / 100.0); // Round to 2 decimal places
            }
            return dto;
        });
    }

    /**
     * Get issue by ID with full details
     */
    public IssueDTO getIssueById(Long id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        if (issue.isHidden()) {
            throw new IllegalArgumentException("Issue is not available");
        }

        return convertToDTO(issue);
    }

    /**
     * Update issue status
     */
    public IssueDTO updateIssueStatus(Long issueId, IssueStatus newStatus, String comment, User updatedBy) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        IssueStatus previousStatus = issue.getStatus();
        issue.setStatus(newStatus);
        Issue updatedIssue = issueRepository.save(issue);

        // Create status history
        StatusHistory statusHistory = new StatusHistory(
                updatedIssue, previousStatus, newStatus, comment, updatedBy
        );
        statusHistoryRepository.save(statusHistory);

        // Send notification to reporter
        if (issue.getReporter() != null) {
            //notificationService.sendStatusUpdateNotification(issue, previousStatus, newStatus, comment);
        }

        return convertToDTO(updatedIssue);
    }

    /**
     * Flag an issue as spam or inappropriate
     */
    public void flagIssue(Long issueId, String reason, String description, User flaggedBy) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found"));

        // Check if user already flagged this issue
        if (flaggedReportRepository.existsByIssueIdAndFlaggedById(issueId, flaggedBy.getId())) {
            throw new IllegalArgumentException("You have already flagged this issue");
        }

        // Create flag report
        FlaggedReport flaggedReport = new FlaggedReport(issue, flaggedBy, reason, description);
        flaggedReportRepository.save(flaggedReport);

        // Update flag count
        issue.setFlagCount(issue.getFlagCount() + 1);

        // Auto-hide if flagged by multiple users (threshold: 3)
        if (issue.getFlagCount() >= 3) {
            issue.setHidden(true);
        }

        issueRepository.save(issue);
    }

    /**
     * Get user's reported issues
     */
    public Page<IssueDTO> getUserIssues(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Issue> issues = issueRepository.findByReporterId(userId, pageable);
        return issues.map(this::convertToDTO);
    }

    /**
     * Upload photos for an issue
     */
    private void uploadPhotos(Issue issue, List<MultipartFile> photos) {
        if (photos.size() > 3) {
            throw new IllegalArgumentException("Maximum 3 photos allowed per issue");
        }

        for (MultipartFile photo : photos) {
            try {
                String filename = fileStorageService.storeFile(photo);
                IssuePhoto issuePhoto = new IssuePhoto(
                        photo.getOriginalFilename(),
                        filename,
                        photo.getContentType(),
                        photo.getSize(),
                        issue
                );
                photoRepository.save(issuePhoto);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload photo: " + e.getMessage());
            }
        }
    }

    /**
     * Convert Issue entity to DTO
     */
    private IssueDTO convertToDTO(Issue issue) {
        IssueDTO dto = new IssueDTO();
        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setCategory(issue.getCategory());
        dto.setStatus(issue.getStatus());
        dto.setLatitude(issue.getLatitude());
        dto.setLongitude(issue.getLongitude());
        dto.setAddress(issue.getAddress());
        dto.setAnonymous(issue.isAnonymous());
        dto.setFlagCount(issue.getFlagCount());
        dto.setHidden(issue.isHidden());
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        dto.setResolvedAt(issue.getResolvedAt());

        // Set reporter name (if not anonymous)
        if (!issue.isAnonymous() && issue.getReporter() != null) {
            dto.setReporterName(issue.getReporter().getFullName());
        } else {
            dto.setReporterName("Anonymous");
        }

        // Get photo URLs
        List<IssuePhoto> photos = photoRepository.findByIssueIdOrderByUploadedAt(issue.getId());
        List<String> photoUrls = photos.stream()
                .map(photo -> "/api/v1/issues/" + issue.getId() + "/photos/" + photo.getFileName())
                .collect(Collectors.toList());
        dto.setPhotoUrls(photoUrls);

        // Get status history


        return dto;
    }

/**
 * Check if coordinates are within the specified radius

public static boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
    return calculateDistance(lat1, lon1, lat2, lon2) <= radiusKm;
}*/

/**
 * Validate latitude value
 */
public static boolean isValidLatitude(Double latitude) {
    return latitude != null && latitude >= -90.0 && latitude <= 90.0;
}

/**
 * Validate longitude value
 */
public static boolean isValidLongitude(Double longitude) {
    return longitude != null && longitude >= -180.0 && longitude <= 180.0;
}
}
