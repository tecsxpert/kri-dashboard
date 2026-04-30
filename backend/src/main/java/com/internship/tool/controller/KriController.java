package com.internship.tool.controller;

import com.internship.tool.dto.KriRequest;
import com.internship.tool.dto.KriResponse;
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
 * REST Controller for KRI (Key Risk Indicator) CRUD operations.
 * Base URL: /api/v1/kri
 */
@RestController
@RequestMapping("/api/v1/kri")
@RequiredArgsConstructor
@Tag(name = "KRI Management", description = "APIs for managing Key Risk Indicators")
public class KriController {

    private final KriService kriService;

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
}
