package com.internship.tool.controller;

import com.internship.tool.dto.*;
import com.internship.tool.service.KriService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for KRI (Key Risk Indicator) CRUD, pagination, and export.
 * Base URL: /api/v1/kri
 * Day 7: Added /search (paginated) and /export/csv endpoints.
 */
@RestController
@RequestMapping("/api/v1/kri")
@RequiredArgsConstructor
@Tag(name = "KRI Management", description = "APIs for managing Key Risk Indicators")
public class KriController {

    private final KriService kriService;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new KRI")
    public ResponseEntity<KriResponse> create(@Valid @RequestBody KriRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kriService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a KRI by its ID")
    public ResponseEntity<KriResponse> getById(
            @Parameter(description = "KRI ID") @PathVariable Long id) {
        return ResponseEntity.ok(kriService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Get all KRIs")
    public ResponseEntity<List<KriResponse>> getAll() {
        return ResponseEntity.ok(kriService.findAll());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get KRIs by status (ACTIVE, INACTIVE, BREACH, NEAR_BREACH)")
    public ResponseEntity<List<KriResponse>> getByStatus(
            @Parameter(description = "KRI Status") @PathVariable String status) {
        return ResponseEntity.ok(kriService.findByStatus(status));
    }

    @GetMapping("/at-risk")
    @Operation(summary = "Get all at-risk KRIs (BREACH or NEAR_BREACH)")
    public ResponseEntity<List<KriResponse>> getAtRisk() {
        return ResponseEntity.ok(kriService.findAtRisk());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing KRI")
    public ResponseEntity<KriResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody KriRequest request) {
        return ResponseEntity.ok(kriService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a KRI by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kriService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Day 7: Pagination & Search ────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Paginated search with optional name/status/score filters",
               description = "Query params: name, status, minScore, maxScore, page (0-based), size, sortBy, sortDir")
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
                .name(name)
                .status(status)
                .minScore(minScore)
                .maxScore(maxScore)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .build();

        return ResponseEntity.ok(kriService.search(filter));
    }

    // ── Day 7: CSV Export ─────────────────────────────────────────────────────

    @GetMapping("/export/csv")
    @Operation(summary = "Export KRIs as CSV file",
               description = "Optional query param: status (ACTIVE, INACTIVE, BREACH, NEAR_BREACH). Omit to export all.")
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
}
