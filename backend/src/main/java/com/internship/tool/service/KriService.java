package com.internship.tool.service;

import com.internship.tool.dto.*;

import java.util.List;

/**
 * Service interface defining business operations for KRI management.
 * Day 7: Added search with pagination, CSV export.
 */
public interface KriService {

    KriResponse create(KriRequest request);

    KriResponse findById(Long id);

    List<KriResponse> findAll();

    List<KriResponse> findByStatus(String status);

    List<KriResponse> findAtRisk();

    KriResponse update(Long id, KriRequest request);

    void delete(Long id);

    // ── Day 7 ──────────────────────────────────────────────────────────────────

    /** Paginated + filtered search */
    PagedKriResponse search(KriFilterRequest filter);

    /** Export KRIs as CSV bytes (filtered by status, or all if status is null) */
    byte[] exportCsv(String status);
}
