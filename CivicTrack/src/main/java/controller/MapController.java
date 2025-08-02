package controller;


import dto.IssueDTO;
import dto.IssueFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.IssueService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/map")
@CrossOrigin(origins = "*")
public class MapController {


    @Autowired
    private IssueService issueService;

    @GetMapping("/issues")

    public ResponseEntity<List<MapIssueDTO>> getMapIssues(
            @RequestParam Double userLatitude,
            @RequestParam Double userLongitude,
            @RequestParam(defaultValue = "5.0") Double radiusKm,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {

        IssueFilterDTO filterDTO = new IssueFilterDTO();
        filterDTO.setUserLatitude(userLatitude);
        filterDTO.setUserLongitude(userLongitude);
        filterDTO.setRadiusKm(radiusKm);
        filterDTO.setSize(1000); // Get more issues for map view
        filterDTO.setIncludeHidden(false);

        if (status != null) {
            try {

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (category != null) {
            try {

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Page<IssueDTO> issues = issueService.getIssuesWithinRadius(filterDTO);

        // Convert to map-specific DTOs (lighter payload)
        List<MapIssueDTO> mapIssues = issues.getContent().stream()
                .map(this::convertToMapDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(mapIssues);
    }

    private MapIssueDTO convertToMapDTO(IssueDTO issue) {
        MapIssueDTO mapDTO = new MapIssueDTO();
        mapDTO.setId(issue.getId());
        mapDTO.setTitle(issue.getTitle());

        mapDTO.setLatitude(issue.getLatitude());
        mapDTO.setLongitude(issue.getLongitude());
        mapDTO.setCreatedAt(issue.getCreatedAt());

        return mapDTO;
    }

    // Map-specific DTO for lighter payload
    public static class MapIssueDTO {
        private Long id;
        private String title;
        private enums.IssueCategory category;
        private enums.IssueStatus status;
        private Double latitude;
        private Double longitude;
        private java.time.LocalDateTime createdAt;
        private Double distanceKm;

        // Getters and setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public enums.IssueCategory getCategory() {
            return category;
        }

        public void setCategory(enums.IssueCategory category) {
            this.category = category;
        }

        public enums.IssueStatus getStatus() {
            return status;
        }

        public void setStatus(enums.IssueStatus status) {
            this.status = status;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public java.time.LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public Double getDistanceKm() {
            return distanceKm;
        }
    }
}
