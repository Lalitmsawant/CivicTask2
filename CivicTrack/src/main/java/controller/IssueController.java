package controller;


import dto.IssueCreateDTO;
import dto.*;
import dto.IssueFilterDTO;
import entity.User;
import enums.IssueStatus;
import jakarta.annotation.Resource;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.FileStorageService;
import service.IssueService;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/issues")
@CrossOrigin(origins = "*")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping

    public ResponseEntity<?> createIssue(
            @Validated @RequestPart("issue") IssueCreateDTO issueDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
             User user) {
        try {
            IssueDTO createdIssue = issueService.createIssue(issueDTO, user, photos);
            return ResponseEntity.ok(createdIssue);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to create issue: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Page<IssueDTO>> getIssues(
            @RequestParam(required = false) Double userLatitude,
            @RequestParam(required = false) Double userLongitude,
            @RequestParam(defaultValue = "3.0") Double radiusKm,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false") boolean includeHidden,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        IssueFilterDTO filterDTO = new IssueFilterDTO();
        filterDTO.setUserLatitude(userLatitude);
        filterDTO.setUserLongitude(userLongitude);
        filterDTO.setRadiusKm(radiusKm);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortDirection(sortDirection);
        filterDTO.setPage(page);
        filterDTO.setSize(size);
        filterDTO.setIncludeHidden(includeHidden);

        if (status != null) {
            try {
                filterDTO.setStatus(IssueStatus.valueOf(status.toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (category != null) {
            try {
//                filterDTO.setCategory(
//                        com.civictrack.enums.IssueCategory.valueOf(category.toUpperCase())
//                );
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Page<IssueDTO> issues = issueService.getIssuesWithinRadius(filterDTO);
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIssue(@PathVariable Long id) {
        try {
            IssueDTO issue = issueService.getIssueById(id);
            return ResponseEntity.ok(issue);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/my")
    public ResponseEntity<Page<IssueDTO>> getMyIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
             User user) {

        Page<IssueDTO> issues = issueService.getUserIssues(user.getId(), page, size);
        return ResponseEntity.ok(issues);
    }

    @PostMapping("/{id}/flag")
    public ResponseEntity<?> flagIssue(
            @PathVariable Long id,
            @RequestBody FlagRequest flagRequest, User user) {
        try {
            issueService.flagIssue(id, flagRequest.getReason(), flagRequest.getDescription(), user);
            return ResponseEntity.ok(Map.of("message", "Issue flagged successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")

    public ResponseEntity<?> updateIssueStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request,
            User user) {
        try {
            IssueStatus newStatus = IssueStatus.valueOf(request.getStatus().toUpperCase());
            IssueDTO updatedIssue = issueService.updateIssueStatus(id, newStatus, request.getComment(), user);
            return ResponseEntity.ok(updatedIssue);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{issueId}/photos/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable Long issueId, @PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (((UrlResource) resource).exists() && ((UrlResource) resource).isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Inner classes for request DTOs
    public static class FlagRequest {
        private String reason;
        private String description;

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class StatusUpdateRequest {
        private String status;
        private String comment;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
