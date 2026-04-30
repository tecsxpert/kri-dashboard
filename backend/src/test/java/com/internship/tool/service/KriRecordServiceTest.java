package com.internship.tool.service;

import com.internship.tool.entity.KriRecord;
import com.internship.tool.exception.BadRequestException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.KriRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KriRecordServiceTest {

    @Mock
    private KriRecordRepository kriRecordRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private KriRecordService kriRecordService;

    // ================================================================== //
    //  createRecord — happy path                                          //
    // ================================================================== //

    @Test
    @DisplayName("createRecord: saves, returns record, and sends create notification")
    void createRecord_validInput_savesAndNotifies() {
        KriRecord request = buildValidRecord();
        KriRecord saved   = buildValidRecord();
        saved.setId(1L);

        when(kriRecordRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());
        when(kriRecordRepository.save(request)).thenReturn(saved);
        doNothing().when(emailService).sendCreateNotification(saved);

        KriRecord result = kriRecordService.createRecord(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test KRI");
        verify(kriRecordRepository).save(request);
        verify(emailService, times(1)).sendCreateNotification(saved);
    }

    // ================================================================== //
    //  createRecord — error: blank / null inputs                         //
    // ================================================================== //

    @Test
    @DisplayName("createRecord: null title → BadRequestException; no DB interaction")
    void createRecord_nullTitle_throwsBadRequestAndNoRepoCall() {
        KriRecord request = buildValidRecord();
        request.setTitle(null);

        assertThatThrownBy(() -> kriRecordService.createRecord(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Title");

        verifyNoInteractions(kriRecordRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("createRecord: blank title → BadRequestException; no DB interaction")
    void createRecord_blankTitle_throwsBadRequestAndNoRepoCall() {
        KriRecord request = buildValidRecord();
        request.setTitle("   ");

        assertThatThrownBy(() -> kriRecordService.createRecord(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Title");

        verifyNoInteractions(kriRecordRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("createRecord: blank category → BadRequestException; no DB interaction")
    void createRecord_blankCategory_throwsBadRequestAndNoRepoCall() {
        KriRecord request = buildValidRecord();
        request.setCategory("");

        assertThatThrownBy(() -> kriRecordService.createRecord(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Category");

        verifyNoInteractions(kriRecordRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    @DisplayName("createRecord: blank status → BadRequestException; no DB interaction")
    void createRecord_blankStatus_throwsBadRequestAndNoRepoCall() {
        KriRecord request = buildValidRecord();
        request.setStatus("  ");

        assertThatThrownBy(() -> kriRecordService.createRecord(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Status");

        verifyNoInteractions(kriRecordRepository);
        verifyNoInteractions(emailService);
    }

    // ================================================================== //
    //  createRecord — error: duplicate title                             //
    // ================================================================== //

    @Test
    @DisplayName("createRecord: duplicate title → BadRequestException; save() never called")
    void createRecord_duplicateTitle_throwsBadRequestAndNoSave() {
        KriRecord request  = buildValidRecord();
        KriRecord existing = buildValidRecord();
        existing.setId(99L);

        when(kriRecordRepository.findByTitle(request.getTitle()))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> kriRecordService.createRecord(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already exists");

        verify(kriRecordRepository, never()).save(any());
        verifyNoInteractions(emailService);
    }

    // ================================================================== //
    //  getAllRecords                                                       //
    // ================================================================== //

    @Test
    @DisplayName("getAllRecords: returns paginated records from repository")
    void getAllRecords_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        List<KriRecord> records = List.of(buildValidRecord(), buildValidRecord());
        Page<KriRecord> page = new PageImpl<>(records, pageable, records.size());

        when(kriRecordRepository.findAll(pageable)).thenReturn(page);

        Page<KriRecord> result = kriRecordService.getAllRecords(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(kriRecordRepository).findAll(pageable);
    }

    @Test
    @DisplayName("getAllRecords: returns empty page when no records exist")
    void getAllRecords_emptyPage_returnsEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<KriRecord> emptyPage = Page.empty(pageable);

        when(kriRecordRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<KriRecord> result = kriRecordService.getAllRecords(pageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    // ================================================================== //
    //  getRecordById                                                      //
    // ================================================================== //

    @Test
    @DisplayName("getRecordById: returns record when found")
    void getRecordById_found_returnsRecord() {
        KriRecord record = buildValidRecord();
        record.setId(5L);

        when(kriRecordRepository.findById(5L)).thenReturn(Optional.of(record));

        KriRecord result = kriRecordService.getRecordById(5L);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitle()).isEqualTo("Test KRI");
        verify(kriRecordRepository).findById(5L);
    }

    @Test
    @DisplayName("getRecordById: throws ResourceNotFoundException when not found")
    void getRecordById_notFound_throwsResourceNotFound() {
        when(kriRecordRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> kriRecordService.getRecordById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(kriRecordRepository).findById(99L);
    }

    // ================================================================== //
    //  Test helper                                                        //
    // ================================================================== //

    private KriRecord buildValidRecord() {
        return KriRecord.builder()
                .title("Test KRI")
                .description("Test description")
                .category("Finance")
                .status("active")
                .score(85.0)
                .build();
    }
}
