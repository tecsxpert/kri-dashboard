package com.internship.tool.service;

import com.internship.tool.dto.*;

import java.util.List;

/**
 * Service interface for KRI business operations.
 * Day 7  : Pagination, CSV export
 * Day 12 : Audit history
 */
public interface KriService {

    KriResponse create(KriRequest request);

    KriResponse findById(Long id);

    List<KriResponse> findAll();

    List<KriResponse> findByStatus(String status);

    List<KriResponse> findAtRisk();

    KriResponse update(Long id, KriRequest request);

    void delete(Long id);

    // Day 7
    PagedKriResponse search(KriFilterRequest filter);

    byte[] exportCsv(String status);

    // Day 12
    List<KriHistoryResponse> getHistory(Long kriId);

    // Day 14: Soft Delete — marks as deleted but keeps DB record
    KriResponse archiveKri(Long id);
}
