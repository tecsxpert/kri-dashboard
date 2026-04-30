package com.internship.tool.controller;

import com.internship.tool.dto.KriRecordRequest;
import com.internship.tool.entity.KriRecord;
import com.internship.tool.service.KriRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kri")
@RequiredArgsConstructor
@Tag(name = "KRI Records", description = "CRUD operations for Key Risk Indicator records")
@SecurityRequirement(name = "bearerAuth")
public class KriRecordController {

    private final KriRecordService kriRecordService;

    @Operation(summary = "Get all KRI records (paginated)",
               description = "Returns a paginated list of all KRI records. Use page and size query params.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Records retrieved successfully")
    })
    @GetMapping("/all")
    public Page<KriRecord> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return kriRecordService.getAllRecords(pageable);
    }

    @Operation(summary = "Get KRI record by ID",
               description = "Returns a single KRI record by its numeric ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Record found"),
            @ApiResponse(responseCode = "404", description = "Record not found")
    })
    @GetMapping("/{id}")
    public KriRecord getRecordById(@PathVariable Long id) {
        return kriRecordService.getRecordById(id);
    }

    @Operation(summary = "Create a new KRI record",
               description = "Creates a new KRI record. Title, category, and status are required.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Record created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate title")
    })
    @PostMapping("/create")
    public ResponseEntity<KriRecord> createRecord(@Valid @RequestBody KriRecordRequest request) {
        KriRecord entity = KriRecord.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(request.getStatus())
                .score(request.getScore())
                .dueDate(request.getDueDate())
                .build();

        KriRecord saved = kriRecordService.createRecord(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
