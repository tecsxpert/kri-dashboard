package com.internship.tool.service;

import com.internship.tool.dto.KriRequest;
import com.internship.tool.dto.KriResponse;

import java.util.List;

/**
 * Service interface defining business operations for KRI management.
 */
public interface KriService {

    KriResponse create(KriRequest request);

    KriResponse findById(Long id);

    List<KriResponse> findAll();

    List<KriResponse> findByStatus(String status);

    List<KriResponse> findAtRisk();

    KriResponse update(Long id, KriRequest request);

    void delete(Long id);
}
