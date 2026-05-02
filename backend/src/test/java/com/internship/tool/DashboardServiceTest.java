package com.internship.tool;

import com.internship.tool.dto.DashboardSummary;
import com.internship.tool.repository.KriRepository;
import com.internship.tool.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DashboardServiceImpl.
 * Day 15 — Final Testing & Quality Assurance
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService Unit Tests")
class DashboardServiceTest {

    @Mock
    private KriRepository kriRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        // Count queries
        when(kriRepository.count()).thenReturn(10L);
        when(kriRepository.countAtRisk()).thenReturn(3L);
        when(kriRepository.findAverageScore()).thenReturn(62.5);
        when(kriRepository.countGroupedByStatus()).thenReturn(List.of(
                new Object[]{"ACTIVE",     6L},
                new Object[]{"BREACH",     2L},
                new Object[]{"NEAR_BREACH",1L},
                new Object[]{"INACTIVE",   1L}
        ));
        when(kriRepository.findTopByScore(any(Pageable.class))).thenReturn(List.of());
        when(kriRepository.findAll()).thenReturn(List.of());
    }

    @Test
    @DisplayName("getSummary — should return correct totalKris")
    void getSummary_TotalKris() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getTotalKris()).isEqualTo(10L);
    }

    @Test
    @DisplayName("getSummary — should return correct atRiskCount")
    void getSummary_AtRiskCount() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getAtRiskCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("getSummary — should return correct averageScore")
    void getSummary_AverageScore() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getAverageScore()).isEqualTo(62.5);
    }

    @Test
    @DisplayName("getSummary — atRiskPercentage should be 30.0")
    void getSummary_AtRiskPercentage() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getAtRiskPercentage()).isEqualTo(30.0);
    }

    @Test
    @DisplayName("getSummary — countByStatus should include all 4 statuses")
    void getSummary_CountByStatus_HasAllKeys() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getCountByStatus()).containsKeys(
                "ACTIVE", "INACTIVE", "BREACH", "NEAR_BREACH");
    }

    @Test
    @DisplayName("getSummary — countByStatus ACTIVE should be 6")
    void getSummary_CountByStatus_Active() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getCountByStatus().get("ACTIVE")).isEqualTo(6L);
    }

    @Test
    @DisplayName("getSummary — scoreDistribution should contain all 4 buckets")
    void getSummary_ScoreDistribution_HasAllBuckets() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getScoreDistribution()).containsKeys(
                "0-25", "26-50", "51-75", "76-100");
    }

    @Test
    @DisplayName("getSummary — topRiskKris should be empty list when no KRIs")
    void getSummary_TopRiskKris_EmptyList() {
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getTopRiskKris()).isEmpty();
    }

    @Test
    @DisplayName("getSummary — should return 0 percentage when no KRIs exist")
    void getSummary_AtRiskPercentage_ZeroWhenNoKris() {
        when(kriRepository.count()).thenReturn(0L);
        when(kriRepository.countAtRisk()).thenReturn(0L);
        DashboardSummary summary = dashboardService.getSummary();
        assertThat(summary.getAtRiskPercentage()).isEqualTo(0.0);
    }
}
