package com.internship.tool.controller;

import com.internship.tool.dto.*;
import com.internship.tool.service.KriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for KRI management with Role-Based Access Control.
 * Base URL: /api/v1/kri
 *
 * Day 9 — RBAC:
 *   ROLE_ADMIN : full access (CREATE, READ, UPDATE, DELETE, EXPORT)
 *   ROLE_USER  : read-only (GET, SEARCH, EXPORT)
 */
@RestController
@RequestMapping("/api/v1/kri")
@RequiredArgsConstructor
@Tag(name = "KRI Management", description = "APIs for managing Key Risk Indicators")
@SecurityRequirement(name = "bearerAuth")
public class KriController {

    private final KriService kriService;

    // ── Write operations — ADMIN only ─────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new KRI  [ADMIN only]")
    public ResponseEntity<KriResponse> create(@Valid @RequestBody KriRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kriService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing KRI  [ADMIN only]")
    public ResponseEntity<KriResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody KriRequest request) {
        return ResponseEntity.ok(kriService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a KRI by ID  [ADMIN only]")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kriService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Read operations — ADMIN or USER ──────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get a KRI by its ID")
    public ResponseEntity<KriResponse> getById(
            @Parameter(description = "KRI ID") @PathVariable Long id) {
        return ResponseEntity.ok(kriService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get all KRIs")
    public ResponseEntity<List<KriResponse>> getAll() {
        return ResponseEntity.ok(kriService.findAll());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get KRIs by status (ACTIVE, INACTIVE, BREACH, NEAR_BREACH)")
    public ResponseEntity<List<KriResponse>> getByStatus(
            @Parameter(description = "KRI Status") @PathVariable String status) {
        return ResponseEntity.ok(kriService.findByStatus(status));
    }

    @GetMapping("/at-risk")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get all at-risk KRIs (BREACH or NEAR_BREACH)")
    public ResponseEntity<List<KriResponse>> getAtRisk() {
        return ResponseEntity.ok(kriService.findAtRisk());
    }

    // ── Day 7: Pagination & Search ────────────────────────────────────────────

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Paginated search with optional name/status/score filters")
    public ResponseEntity<PagedKriResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        KriFilterRequest filter = KriFilterRequest.builder()
                .name(name).status(status)
                .minScore(minScore).maxScore(maxScore)
                .page(page).size(size)
                .sortBy(sortBy).sortDir(sortDir)
                .build();

        return ResponseEntity.ok(kriService.search(filter));
    }

    // ── Day 7: CSV Export ─────────────────────────────────────────────────────

    @GetMapping("/export/csv")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Export KRIs as CSV file")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String status) {

        byte[] csv = kriService.exportCsv(status);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename("kri-export.csv").build());
        headers.setContentLength(csv.length);

        return new ResponseEntity<>(csv, headers, HttpStatus.OK);
    }

    // ── Day 12: Audit History ─────────────────────────────────────────────────

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(summary = "Get the full audit history for a specific KRI",
               description = "Returns all CREATE/UPDATE/DELETE events for the given KRI, newest first.")
    public ResponseEntity<List<com.internship.tool.dto.KriHistoryResponse>> getHistory(
            @Parameter(description = "KRI ID") @PathVariable Long id) {
        return ResponseEntity.ok(kriService.getHistory(id));
    }

    // ── Day 14: Soft Delete / Archive ─────────────────────────────────────────

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft-delete (archive) a KRI  [ADMIN only]",
               description = "Marks the KRI as deleted without removing it from the database. Use DELETE for permanent removal.")
    public ResponseEntity<KriResponse> archive(
            @Parameter(description = "KRI ID") @PathVariable Long id) {
        return ResponseEntity.ok(kriService.archiveKri(id));
    }
}
