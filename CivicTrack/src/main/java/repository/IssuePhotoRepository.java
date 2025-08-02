package repository;

import entity.IssuePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IssuePhotoRepository extends JpaRepository<IssuePhoto, Long> {
    List<IssuePhoto> findByIssueId(Long issueId);

    @Query("SELECT p FROM IssuePhoto p WHERE p.issue.id = :issueId ORDER BY p.uploadedAt")
    List<IssuePhoto> findByIssueIdOrderByUploadedAt(@Param("issueId") Long issueId);

    void deleteByIssueId(Long issueId);

}
