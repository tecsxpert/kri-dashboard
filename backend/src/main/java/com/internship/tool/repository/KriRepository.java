package com.internship.tool.repository;

import com.internship.tool.entity.Kri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for KRI entities.
 */
@Repository
public interface KriRepository extends JpaRepository<Kri, Long> {

    List<Kri> findByStatus(String status);

    List<Kri> findByScoreGreaterThanEqual(int score);

    List<Kri> findByScoreLessThan(int score);

    @Query("SELECT k FROM Kri k WHERE k.status IN ('BREACH', 'NEAR_BREACH') ORDER BY k.score DESC")
    List<Kri> findAtRiskKris();
}
