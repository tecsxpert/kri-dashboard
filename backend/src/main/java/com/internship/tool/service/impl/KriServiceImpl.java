package com.internship.tool.service.impl;

import com.internship.tool.dto.*;
import com.internship.tool.entity.Kri;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.KriRepository;
import com.internship.tool.service.KriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Implementation of KriService providing full CRUD + pagination + CSV export.
 * Day 7: Added search(filter) and exportCsv(status).
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KriServiceImpl implements KriService {

    private final KriRepository kriRepository;

    // ── CRUD (unchanged) ──────────────────────────────────────────────────────

    @Override
    @CachePut(value = "kris", key = "#result.id")
    public KriResponse create(KriRequest request) {
        log.info("Creating new KRI: name={}", request.getName());
        Kri kri = Kri.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus())
                .score(request.getScore())
                .build();
        Kri saved = kriRepository.save(kri);
        log.info("KRI created successfully: id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Cacheable(value = "kris", key = "#id")
    @Transactional(readOnly = true)
    public KriResponse findById(Long id) {
        log.debug("Fetching KRI by id={}", id);
        return toResponse(findEntityById(id));
    }

    @Override
    @Cacheable(value = "kris")
    @Transactional(readOnly = true)
    public List<KriResponse> findAll() {
        log.debug("Fetching all KRIs");
        return kriRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KriResponse> findByStatus(String status) {
        log.debug("Fetching KRIs by status={}", status);
        return kriRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KriResponse> findAtRisk() {
        log.debug("Fetching at-risk KRIs (BREACH or NEAR_BREACH)");
        return kriRepository.findAtRiskKris().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @CachePut(value = "kris", key = "#id")
    public KriResponse update(Long id, KriRequest request) {
        log.info("Updating KRI id={}", id);
        Kri kri = findEntityById(id);
        kri.setName(request.getName());
        kri.setDescription(request.getDescription());
        kri.setStatus(request.getStatus());
        kri.setScore(request.getScore());
        return toResponse(kriRepository.save(kri));
    }

    @Override
    @CacheEvict(value = "kris", key = "#id")
    public void delete(Long id) {
        log.info("Deleting KRI id={}", id);
        kriRepository.delete(findEntityById(id));
    }

    // ── Day 7: Pagination & Search ────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PagedKriResponse search(KriFilterRequest filter) {
        log.debug("Searching KRIs with filter: {}", filter);

        Sort sort = filter.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Page<Kri> page = kriRepository.findWithFilters(
                filter.getName(),
                filter.getStatus(),
                filter.getMinScore(),
                filter.getMaxScore(),
                pageable
        );

        return PagedKriResponse.builder()
                .content(page.getContent().stream().map(this::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    // ── Day 7: CSV Export ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public byte[] exportCsv(String status) {
        log.info("Exporting KRIs as CSV, status filter={}", status);
        List<Kri> kris = kriRepository.findAllForExport(status);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintWriter pw = new PrintWriter(baos)) {
            // CSV Header
            pw.println("id,name,description,status,score,createdAt,updatedAt");
            for (Kri k : kris) {
                pw.printf("%d,\"%s\",\"%s\",%s,%d,%s,%s%n",
                        k.getId(),
                        escapeCsv(k.getName()),
                        escapeCsv(k.getDescription()),
                        k.getStatus(),
                        k.getScore() == null ? 0 : k.getScore(),
                        k.getCreatedAt(),
                        k.getUpdatedAt()
                );
            }
        }
        return baos.toByteArray();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Kri findEntityById(Long id) {
        return kriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KRI", id));
    }

    private KriResponse toResponse(Kri kri) {
        return KriResponse.builder()
                .id(kri.getId())
                .name(kri.getName())
                .description(kri.getDescription())
                .status(kri.getStatus())
                .score(kri.getScore())
                .createdAt(kri.getCreatedAt())
                .updatedAt(kri.getUpdatedAt())
                .build();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}
