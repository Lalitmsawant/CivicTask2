package repository;

import entity.FlaggedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlaggedReportRepository  extends JpaRepository<FlaggedReport, Long> {
    List<FlaggedReport> findByIssueId(Long issueId);

    @Query("SELECT fr FROM FlaggedReport fr WHERE fr.reviewed = false ORDER BY fr.flaggedAt DESC")
    List<FlaggedReport> findUnreviewedFlags();

    @Query("SELECT COUNT(fr) FROM FlaggedReport fr WHERE fr.issue.id = :issueId AND fr.flaggedBy.id = :userId")
    long countByIssueIdAndFlaggedById(@Param("issueId") Long issueId, @Param("userId") Long userId);

    boolean existsByIssueIdAndFlaggedById(Long issueId, Long flaggedById);

}
