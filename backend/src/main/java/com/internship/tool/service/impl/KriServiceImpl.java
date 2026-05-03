package com.internship.tool.service.impl;

import com.internship.tool.dto.*;
import com.internship.tool.entity.Kri;
import com.internship.tool.entity.KriHistory;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.KriHistoryRepository;
import com.internship.tool.repository.KriRepository;
import com.internship.tool.service.KriService;
import com.internship.tool.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Implementation of KriService providing CRUD + pagination + CSV export + audit history.
 * Day 7  : Pagination & Filtering, CSV Export
 * Day 12 : KRI Audit History — records every change to kri_history table
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KriServiceImpl implements KriService {

    private final KriRepository        kriRepository;
    private final KriHistoryRepository historyRepository;
    private final NotificationService  notificationService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

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

        // Day 12 — record history
        recordHistory(saved.getId(), "CREATE", null, saved);

        // Day 16 — fire breach notification if status warrants it
        if (Set.of("BREACH", "NEAR_BREACH").contains(saved.getStatus())) {
            notificationService.createBreachAlert(saved);
        }

        log.info("KRI created: id={}", saved.getId());
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
        return kriRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KriResponse> findByStatus(String status) {
        log.debug("Fetching KRIs by status={}", status);
        return kriRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KriResponse> findAtRisk() {
        log.debug("Fetching at-risk KRIs");
        return kriRepository.findAtRiskKris().stream().map(this::toResponse).toList();
    }

    @Override
    @CachePut(value = "kris", key = "#id")
    public KriResponse update(Long id, KriRequest request) {
        log.info("Updating KRI id={}", id);
        Kri old = findEntityById(id);
        Kri snapshot = Kri.builder()
                .name(old.getName()).status(old.getStatus()).score(old.getScore()).build();

        old.setName(request.getName());
        old.setDescription(request.getDescription());
        old.setStatus(request.getStatus());
        old.setScore(request.getScore());
        Kri saved = kriRepository.save(old);

        // Day 12 — record history
        recordHistory(saved.getId(), "UPDATE", snapshot, saved);

        // Day 16 — fire breach notification if status just became BREACH/NEAR_BREACH
        if (Set.of("BREACH", "NEAR_BREACH").contains(saved.getStatus())) {
            notificationService.createBreachAlert(saved);
        }

        return toResponse(saved);
    }

    @Override
    @CacheEvict(value = "kris", key = "#id")
    public void delete(Long id) {
        log.info("Deleting KRI id={}", id);
        Kri kri = findEntityById(id);
        recordHistory(kri.getId(), "DELETE", kri, null);
        kriRepository.delete(kri);
    }

    // ── Day 7: Pagination & Search ────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public PagedKriResponse search(KriFilterRequest filter) {
        log.debug("Searching KRIs: {}", filter);
        Sort sort = filter.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        Page<Kri> page = kriRepository.findWithFilters(
                filter.getName(), filter.getStatus(),
                filter.getMinScore(), filter.getMaxScore(), pageable);

        return PagedKriResponse.builder()
                .content(page.getContent().stream().map(this::toResponse).toList())
                .page(page.getNumber()).size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    // ── Day 7: CSV Export ─────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public byte[] exportCsv(String status) {
        log.info("Exporting KRIs as CSV, status={}", status);
        var kris = kriRepository.findAllForExport(status);
        var baos = new ByteArrayOutputStream();
        try (var pw = new PrintWriter(baos)) {
            pw.println("id,name,description,status,score,createdAt,updatedAt");
            for (Kri k : kris) {
                pw.printf("%d,\"%s\",\"%s\",%s,%d,%s,%s%n",
                        k.getId(),
                        escapeCsv(k.getName()), escapeCsv(k.getDescription()),
                        k.getStatus(),
                        k.getScore() == null ? 0 : k.getScore(),
                        k.getCreatedAt(), k.getUpdatedAt());
            }
        }
        return baos.toByteArray();
    }

    // ── Day 17: Excel Export ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public byte[] exportExcel(String status) {
        log.info("Exporting KRIs as Excel, status={}", status);
        var kris = kriRepository.findAllForExport(status);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("KRIs");
            
            // Header Row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Description");
            headerRow.createCell(3).setCellValue("Status");
            headerRow.createCell(4).setCellValue("Score");
            headerRow.createCell(5).setCellValue("Created At");
            headerRow.createCell(6).setCellValue("Updated At");

            // Data Rows
            int rowIdx = 1;
            for (Kri k : kris) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(k.getId());
                row.createCell(1).setCellValue(k.getName());
                row.createCell(2).setCellValue(k.getDescription() == null ? "" : k.getDescription());
                row.createCell(3).setCellValue(k.getStatus());
                if (k.getScore() != null) {
                    row.createCell(4).setCellValue(k.getScore());
                } else {
                    row.createCell(4).setCellValue(0);
                }
                row.createCell(5).setCellValue(k.getCreatedAt() != null ? k.getCreatedAt().toString() : "");
                row.createCell(6).setCellValue(k.getUpdatedAt() != null ? k.getUpdatedAt().toString() : "");
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to export Excel", e);
            throw new RuntimeException("Failed to export Excel data", e);
        }
    }

    // ── Day 12: History ───────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<KriHistoryResponse> getHistory(Long kriId) {
        log.debug("Fetching history for KRI id={}", kriId);
        return historyRepository.findByKriIdOrderByChangedAtDesc(kriId)
                .stream().map(this::toHistoryResponse).toList();
    }

    // ── Day 18: Bulk Operations ───────────────────────────────────────────────

    @Override
    @Transactional
    public List<KriResponse> bulkCreate(List<KriRequest> requests) {
        log.info("Bulk creating {} KRIs", requests.size());
        return requests.stream().map(this::create).toList();
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(List<Long> ids, String newStatus) {
        log.info("Bulk updating status to {} for KRIs: {}", newStatus, ids);
        List<Kri> kris = kriRepository.findAllById(ids);
        for (Kri kri : kris) {
            Kri snapshot = Kri.builder()
                    .name(kri.getName()).status(kri.getStatus()).score(kri.getScore()).build();
            kri.setStatus(newStatus);
            Kri saved = kriRepository.save(kri);
            
            recordHistory(saved.getId(), "UPDATE_STATUS", snapshot, saved);
            
            if (Set.of("BREACH", "NEAR_BREACH").contains(saved.getStatus())) {
                notificationService.createBreachAlert(saved);
            }
        }
    }

    // ── Day 14: Soft Delete / Archive ─────────────────────────────────────────

    @Override
    @CacheEvict(value = "kris", key = "#id")
    public KriResponse archiveKri(Long id) {
        log.info("Archiving (soft-deleting) KRI id={}", id);
        Kri kri = findEntityById(id);
        kri.setDeleted(true);
        kri.setDeletedAt(java.time.LocalDateTime.now());
        kri.setDeletedBy(getCurrentUsername());
        Kri saved = kriRepository.save(kri);
        recordHistory(saved.getId(), "ARCHIVE", kri, null);
        return toResponse(saved);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Kri findEntityById(Long id) {
        return kriRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KRI", id));
    }

    private KriResponse toResponse(Kri kri) {
        return KriResponse.builder()
                .id(kri.getId()).name(kri.getName())
                .description(kri.getDescription())
                .status(kri.getStatus()).score(kri.getScore())
                .createdAt(kri.getCreatedAt()).updatedAt(kri.getUpdatedAt())
                .build();
    }

    private KriHistoryResponse toHistoryResponse(KriHistory h) {
        return KriHistoryResponse.builder()
                .id(h.getId()).kriId(h.getKriId())
                .action(h.getAction()).changedBy(h.getChangedBy())
                .oldName(h.getOldName()).newName(h.getNewName())
                .oldStatus(h.getOldStatus()).newStatus(h.getNewStatus())
                .oldScore(h.getOldScore()).newScore(h.getNewScore())
                .changedAt(h.getChangedAt())
                .build();
    }

    /** Records a KRI change into kri_history. */
    private void recordHistory(Long kriId, String action, Kri before, Kri after) {
        String currentUser = getCurrentUsername();
        KriHistory history = KriHistory.builder()
                .kriId(kriId)
                .action(action)
                .changedBy(currentUser)
                .oldName(before != null ? before.getName() : null)
                .newName(after  != null ? after.getName()  : null)
                .oldStatus(before != null ? before.getStatus() : null)
                .newStatus(after  != null ? after.getStatus()  : null)
                .oldScore(before != null ? before.getScore() : null)
                .newScore(after  != null ? after.getScore()  : null)
                .build();
        historyRepository.save(history);
        log.debug("[HISTORY] Recorded {} for KRI id={} by {}", action, kriId, currentUser);
    }

    /** Gets the currently authenticated username (or 'system' if unauthenticated). */
    private String getCurrentUsername() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) return auth.getName();
        } catch (Exception ignored) { }
        return "system";
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}
