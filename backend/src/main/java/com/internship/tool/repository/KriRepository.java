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
 * Day 7: Added paginated search, name filter, score-range queries.
 */
@Repository
public interface KriRepository extends JpaRepository<Kri, Long> {

    List<Kri> findByStatus(String status);

    List<Kri> findByScoreGreaterThanEqual(int score);

    List<Kri> findByScoreLessThan(int score);

    @Query("SELECT k FROM Kri k WHERE k.status IN ('BREACH', 'NEAR_BREACH') ORDER BY k.score DESC")
    List<Kri> findAtRiskKris();

    // ── Day 7: Pagination & Filtering ─────────────────────────────────────────

    /**
     * Dynamic paginated search supporting optional name (ILIKE), status, and score range filters.
     */
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

    /**
     * Fetch all KRIs for CSV export — no pagination.
     */
    @Query("""
            SELECT k FROM Kri k
            WHERE (:status IS NULL OR k.status = :status)
            ORDER BY k.id ASC
            """)
    List<Kri> findAllForExport(@Param("status") String status);
}
