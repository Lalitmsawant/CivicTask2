package controller;


import dto.UserDTO;
import entity.FlaggedReport;
import entity.User;
import enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.FlaggedReportRepository;
import service.AnalyticsService;
import service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private FlaggedReportRepository flaggedReportRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        try {
            UserRole role = UserRole.valueOf(request.getRole().toUpperCase());
            User updatedUser = userService.updateUserRole(id, role);
            UserDTO userDTO = userService.convertToDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        try {
            User updatedUser = userService.toggleUserStatus(id);
            UserDTO userDTO = userService.convertToDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/flagged-reports")
    public ResponseEntity<List<FlaggedReport>> getFlaggedReports() {
        List<FlaggedReport> flaggedReports = flaggedReportRepository.findUnreviewedFlags();
        return ResponseEntity.ok(flaggedReports);
    }

    @PutMapping("/flagged-reports/{id}/review")
    public ResponseEntity<?> reviewFlaggedReport(@PathVariable Long id, @RequestBody ReviewRequest request) {
        try {
            FlaggedReport flaggedReport = flaggedReportRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Flagged report not found"));

            flaggedReport.setReviewed(true);
            flaggedReport.setReviewedAt(java.time.LocalDateTime.now());
            // Set reviewed by user if needed

            flaggedReportRepository.save(flaggedReport);

            return ResponseEntity.ok(Map.of("message", "Flagged report reviewed successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Inner classes for request DTOs
    public static class RoleUpdateRequest {
        private String role;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class ReviewRequest {
        private String action;
        private String comment;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }

}
