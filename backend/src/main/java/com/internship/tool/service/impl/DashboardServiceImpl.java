package com.internship.tool.service.impl;

import com.internship.tool.dto.DashboardSummary;
import com.internship.tool.dto.KriResponse;
import com.internship.tool.entity.Kri;
import com.internship.tool.repository.KriRepository;
import com.internship.tool.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of DashboardService — computes KRI analytics summary.
 * Day 10 — Analytics & Reporting
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final KriRepository kriRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummary getSummary() {
        log.info("[DASHBOARD] Building analytics summary");

        // Total count
        long total = kriRepository.count();

        // Count by status
        List<Object[]> statusRows = kriRepository.countGroupedByStatus();
        Map<String, Long> countByStatus = new LinkedHashMap<>();
        // Pre-fill all possible statuses with 0 so the frontend always gets all keys
        for (String s : List.of("ACTIVE", "INACTIVE", "BREACH", "NEAR_BREACH")) {
            countByStatus.put(s, 0L);
        }
        for (Object[] row : statusRows) {
            countByStatus.put((String) row[0], (Long) row[1]);
        }

        // Average score
        double avgScore = kriRepository.findAverageScore();

        // At-risk count
        long atRisk = kriRepository.countAtRisk();

        // At-risk percentage
        double atRiskPct = (total == 0) ? 0.0 : Math.round((atRisk * 100.0 / total) * 10.0) / 10.0;

        // Top 5 by score
        List<KriResponse> topRisk = kriRepository
                .findTopByScore(PageRequest.of(0, 5))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        // Score distribution: 0-25, 26-50, 51-75, 76-100
        Map<String, Long> scoreDist = buildScoreDistribution();

        DashboardSummary summary = DashboardSummary.builder()
                .totalKris(total)
                .countByStatus(countByStatus)
                .averageScore(Math.round(avgScore * 10.0) / 10.0)
                .atRiskCount(atRisk)
                .atRiskPercentage(atRiskPct)
                .topRiskKris(topRisk)
                .scoreDistribution(scoreDist)
                .build();

        log.info("[DASHBOARD] Summary built: total={}, atRisk={}, avgScore={}", total, atRisk, avgScore);
        return summary;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, Long> buildScoreDistribution() {
        List<Kri> all = kriRepository.findAll();
        Map<String, Long> dist = new LinkedHashMap<>();
        dist.put("0-25",   all.stream().filter(k -> k.getScore() != null && k.getScore() <= 25).count());
        dist.put("26-50",  all.stream().filter(k -> k.getScore() != null && k.getScore() >= 26 && k.getScore() <= 50).count());
        dist.put("51-75",  all.stream().filter(k -> k.getScore() != null && k.getScore() >= 51 && k.getScore() <= 75).count());
        dist.put("76-100", all.stream().filter(k -> k.getScore() != null && k.getScore() >= 76).count());
        return dist;
    }

    private KriResponse toResponse(Kri kri) {
        return KriResponse.builder()
                .id(kri.getId())
                .name(kri.getName())
                .description(kri.getDescription())
                .status(kri.getStatus())
                .score(kri.getScore())
                .createdAt(kri.getCreatedAt())
                .updatedAt(kri.getUpdatedAt())
                .build();
    }
}
