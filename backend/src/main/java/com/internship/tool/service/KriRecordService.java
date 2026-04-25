package com.internship.tool.service;

import com.internship.tool.entity.KriRecord;
import com.internship.tool.exception.BadRequestException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.KriRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KriRecordService {

    private final KriRecordRepository kriRecordRepository;

    public KriRecordService(KriRecordRepository kriRecordRepository) {
        this.kriRecordRepository = kriRecordRepository;
    }

    public KriRecord createRecord(KriRecord request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new BadRequestException("Title must not be blank");
        }
        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new BadRequestException("Category must not be blank");
        }
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new BadRequestException("Status must not be blank");
        }

        kriRecordRepository.findByTitle(request.getTitle()).ifPresent(existing -> {
            throw new BadRequestException("A record with title '" + request.getTitle() + "' already exists");
        });

        return kriRecordRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Page<KriRecord> getAllRecords(Pageable pageable) {
        return kriRecordRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public KriRecord getRecordById(Long id) {
        return kriRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("KRI record not found with id: " + id));
    }
}
