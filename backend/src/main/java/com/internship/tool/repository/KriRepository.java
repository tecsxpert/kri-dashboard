package com.internship.tool.repository;

import com.internship.tool.entity.Kri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for KRI entities.
 * Day 7 : Added paginated search, name filter, score-range queries.
 * Day 10: Added analytics aggregate queries.
 */
@Repository
public interface KriRepository extends JpaRepository<Kri, Long> {

    List<Kri> findByStatus(String status);

    List<Kri> findByScoreGreaterThanEqual(int score);

    List<Kri> findByScoreLessThan(int score);

    @Query("SELECT k FROM Kri k WHERE k.status IN ('BREACH', 'NEAR_BREACH') ORDER BY k.score DESC")
    List<Kri> findAtRiskKris();

    // ── Day 7: Pagination & Filtering ─────────────────────────────────────────

    @Query("""
            SELECT k FROM Kri k
            WHERE (:name   IS NULL OR LOWER(k.name)   LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:status  IS NULL OR k.status        =  :status)
              AND (:minScore IS NULL OR k.score        >= :minScore)
              AND (:maxScore IS NULL OR k.score        <= :maxScore)
            """)
    Page<Kri> findWithFilters(
            @Param("name")     String name,
            @Param("status")   String status,
            @Param("minScore") Integer minScore,
            @Param("maxScore") Integer maxScore,
            Pageable pageable
    );

    @Query("""
            SELECT k FROM Kri k
            WHERE (:status IS NULL OR k.status = :status)
            ORDER BY k.id ASC
            """)
    List<Kri> findAllForExport(@Param("status") String status);

    // ── Day 10: Analytics Queries ─────────────────────────────────────────────

    /** Count KRIs grouped by status → returns Object[]{status, count} rows */
    @Query("SELECT k.status, COUNT(k) FROM Kri k GROUP BY k.status")
    List<Object[]> countGroupedByStatus();

    /** Average score of all KRIs */
    @Query("SELECT COALESCE(AVG(k.score), 0) FROM Kri k")
    double findAverageScore();

    /** Count KRIs with status BREACH or NEAR_BREACH */
    @Query("SELECT COUNT(k) FROM Kri k WHERE k.status IN ('BREACH', 'NEAR_BREACH')")
    long countAtRisk();

    /** Top N KRIs by score descending */
    @Query("SELECT k FROM Kri k ORDER BY k.score DESC")
    List<Kri> findTopByScore(Pageable pageable);
}
