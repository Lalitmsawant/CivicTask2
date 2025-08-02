package repository;

import entity.Issue;
import enums.IssueCategory;
import enums.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT i FROM Issue i WHERE " +
            "(:includeHidden = true OR i.hidden = false) AND " +
            "(:category IS NULL OR i.category = :category) AND " +
            "(:status IS NULL OR i.status = :status) AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
            "cos(radians(i.longitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(i.latitude)))) <= :radiusKm")
    Page<Issue> findIssuesWithinRadius(
            @Param("userLat") Double userLatitude,
            @Param("userLng") Double userLongitude,
            @Param("radiusKm") Double radiusKm,
            @Param("category") IssueCategory category,
            @Param("status") IssueStatus status,
            @Param("includeHidden") boolean includeHidden,
            Pageable pageable
    );

    @Query("SELECT i FROM Issue i WHERE i.reporter.id = :reporterId")
    Page<Issue> findByReporterId(@Param("reporterId") Long reporterId, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = :status")
    long countByStatus(@Param("status") IssueStatus status);

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.category = :category")
    long countByCategory(@Param("category") IssueCategory category);

    @Query("SELECT COUNT(i) FROM Issue i WHERE i.flagCount >= 3 AND i.hidden = false")
    long countFlaggedIssues();

    @Query("SELECT i FROM Issue i WHERE i.flagCount >= 3 AND i.hidden = false")
    List<Issue> findFlaggedIssues();

    @Query("SELECT i.category, COUNT(i) FROM Issue i GROUP BY i.category")
    List<Object[]> countIssuesByCategory();

    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countIssuesByStatus();

    @Query("SELECT YEAR(i.createdAt), MONTH(i.createdAt), COUNT(i) FROM Issue i " +
            "WHERE i.createdAt >= :startDate GROUP BY YEAR(i.createdAt), MONTH(i.createdAt) " +
            "ORDER BY YEAR(i.createdAt), MONTH(i.createdAt)")
    List<Object[]> countIssuesPerMonth(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT AVG(TIMESTAMPDIFF(i.createdAt, i.resolvedAt)) FROM Issue i " +
            "WHERE i.status = 'RESOLVED' AND i.resolvedAt IS NOT NULL")
    Double getAverageResolutionTimeHours();
}
