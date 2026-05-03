package com.internship.tool.repository;

import com.internship.tool.entity.KriHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for KRI audit history.
 * Day 12 — KRI Audit History
 */
@Repository
public interface KriHistoryRepository extends JpaRepository<KriHistory, Long> {

    /** Find all history entries for a specific KRI, newest first */
    List<KriHistory> findByKriIdOrderByChangedAtDesc(Long kriId);

    /** Find all history entries for a specific user */
    List<KriHistory> findByChangedByOrderByChangedAtDesc(String changedBy);
}
