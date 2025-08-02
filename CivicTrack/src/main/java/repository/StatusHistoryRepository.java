package repository;

import entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StatusHistoryRepository  extends JpaRepository<StatusHistory, Long> {
    @Query("SELECT sh FROM StatusHistory sh WHERE sh.issue.id = :issueId ORDER BY sh.changedAt DESC")
    List<StatusHistory> findByIssueIdOrderByChangedAtDesc(@Param("issueId") Long issueId);

    List<StatusHistory> findByIssueId(Long issueId);
}
