package com.internship.tool;

import com.internship.tool.dto.KriRequest;
import com.internship.tool.dto.KriResponse;
import com.internship.tool.entity.Kri;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.KriRepository;
import com.internship.tool.service.impl.KriServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for KriServiceImpl.
 * Day 6 — Testing & Quality Assurance
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("KriService Unit Tests")
class KriServiceTest {

    @Mock
    private KriRepository kriRepository;

    @InjectMocks
    private KriServiceImpl kriService;

    private Kri sampleKri;
    private KriRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleKri = Kri.builder()
                .id(1L)
                .name("Operational Risk")
                .description("Risk related to operations")
                .status("ACTIVE")
                .score(45)
                .build();

        sampleRequest = KriRequest.builder()
                .name("Operational Risk")
                .description("Risk related to operations")
                .status("ACTIVE")
                .score(45)
                .build();
    }

    @Test
    @DisplayName("Should create a KRI successfully")
    void createKri_Success() {
        when(kriRepository.save(any(Kri.class))).thenReturn(sampleKri);

        KriResponse response = kriService.create(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Operational Risk");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        assertThat(response.getScore()).isEqualTo(45);
        verify(kriRepository, times(1)).save(any(Kri.class));
    }

    @Test
    @DisplayName("Should return KRI by ID")
    void findById_Success() {
        when(kriRepository.findById(1L)).thenReturn(Optional.of(sampleKri));

        KriResponse response = kriService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Operational Risk");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when KRI not found")
    void findById_NotFound_ThrowsException() {
        when(kriRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> kriService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should return all KRIs")
    void findAll_ReturnsList() {
        when(kriRepository.findAll()).thenReturn(List.of(sampleKri));

        List<KriResponse> result = kriService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Operational Risk");
    }

    @Test
    @DisplayName("Should update KRI successfully")
    void updateKri_Success() {
        KriRequest updateRequest = KriRequest.builder()
                .name("Updated Risk").description("Updated").status("BREACH").score(80).build();

        when(kriRepository.findById(1L)).thenReturn(Optional.of(sampleKri));
        when(kriRepository.save(any(Kri.class))).thenReturn(
                Kri.builder().id(1L).name("Updated Risk").description("Updated")
                        .status("BREACH").score(80).build());

        KriResponse response = kriService.update(1L, updateRequest);

        assertThat(response.getName()).isEqualTo("Updated Risk");
        assertThat(response.getStatus()).isEqualTo("BREACH");
        assertThat(response.getScore()).isEqualTo(80);
    }

    @Test
    @DisplayName("Should delete KRI by ID")
    void deleteKri_Success() {
        when(kriRepository.findById(1L)).thenReturn(Optional.of(sampleKri));
        doNothing().when(kriRepository).delete(sampleKri);

        assertThatCode(() -> kriService.delete(1L)).doesNotThrowAnyException();
        verify(kriRepository, times(1)).delete(sampleKri);
    }

    @Test
    @DisplayName("Should return KRIs filtered by status")
    void findByStatus_ReturnsList() {
        when(kriRepository.findByStatus("ACTIVE")).thenReturn(List.of(sampleKri));

        List<KriResponse> result = kriService.findByStatus("ACTIVE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("ACTIVE");
    }
}
