package com.internship.tool.scheduler;

import com.internship.tool.entity.Kri;
import com.internship.tool.repository.KriRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scheduled tasks for KRI monitoring.
 * - Daily breach check at 8:00 AM
 * - Hourly KRI statistics report
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KriScheduler {

    private final KriRepository kriRepository;

    /**
     * Runs every day at 08:00 AM server time.
     * Logs all KRIs currently in BREACH or NEAR_BREACH status.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void dailyBreachCheck() {
        log.info("=== [SCHEDULER] Daily KRI Breach Check Started ===");

        List<Kri> breached    = kriRepository.findByStatus("BREACH");
        List<Kri> nearBreach  = kriRepository.findByStatus("NEAR_BREACH");

        if (breached.isEmpty() && nearBreach.isEmpty()) {
            log.info("[SCHEDULER] No KRIs in breach or near-breach. All clear.");
        } else {
            breached.forEach(k ->
                log.warn("[SCHEDULER] BREACH     → id={}, name='{}', score={}", k.getId(), k.getName(), k.getScore()));
            nearBreach.forEach(k ->
                log.warn("[SCHEDULER] NEAR_BREACH → id={}, name='{}', score={}", k.getId(), k.getName(), k.getScore()));
        }
        log.info("=== [SCHEDULER] Breach Check Done | BREACH={}, NEAR_BREACH={} ===",
                breached.size(), nearBreach.size());
    }

    /**
     * Runs every hour — logs total KRI count for monitoring dashboards.
     */
    @Scheduled(fixedRate = 3_600_000)
    public void hourlyStatisticsReport() {
        long total = kriRepository.count();
        log.info("[SCHEDULER] Hourly Stats | Total KRIs in system: {}", total);
    }
}
