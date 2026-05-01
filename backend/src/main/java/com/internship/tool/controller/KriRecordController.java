package com.internship.tool.controller;

import com.internship.tool.dto.KriRecordRequest;
import com.internship.tool.entity.KriRecord;
import com.internship.tool.service.KriRecordService;
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
public class KriRecordController {

    private final KriRecordService kriRecordService;

    // GET /api/kri/all?page=0&size=10
    @GetMapping("/all")
    public Page<KriRecord> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return kriRecordService.getAllRecords(pageable);
    }

    // GET /api/kri/{id}
    @GetMapping("/{id}")
    public KriRecord getRecordById(@PathVariable Long id) {
        return kriRecordService.getRecordById(id);
    }

    // POST /api/kri/create
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
