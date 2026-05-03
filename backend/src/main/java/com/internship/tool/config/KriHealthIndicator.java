package com.internship.tool.config;

import com.internship.tool.repository.KriRepository;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.*;
import org.springframework.stereotype.Component;

/**
 * Custom Spring Boot Actuator health indicator.
 * Day 14 — Custom Health Indicator & Production Readiness
 *
 * Accessible at: GET /actuator/health
 * Reports KRI count, user count, and at-risk count so ops can monitor DB health.
 */
@Component("kriDashboard")
@RequiredArgsConstructor
public class KriHealthIndicator implements HealthIndicator {

    private final KriRepository  kriRepository;
    private final UserRepository userRepository;

    @Override
    public Health health() {
        try {
            long totalKris  = kriRepository.count();
            long totalUsers = userRepository.count();
            long atRisk     = kriRepository.countAtRisk();

            // Warn if more than 50% of KRIs are at risk
            boolean highRisk = totalKris > 0 && (atRisk * 100 / totalKris) > 50;

            Health.Builder builder = highRisk ? Health.status("WARNING") : Health.up();

            return builder
                    .withDetail("totalKris",  totalKris)
                    .withDetail("atRiskKris", atRisk)
                    .withDetail("totalUsers", totalUsers)
                    .withDetail("status",     highRisk ? "HIGH_RISK_THRESHOLD_EXCEEDED" : "NORMAL")
                    .build();

        } catch (Exception ex) {
            return Health.down()
                    .withDetail("error", ex.getMessage())
                    .build();
        }
    }
}
