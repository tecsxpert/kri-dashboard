package com.internship.tool.config;

import com.internship.tool.entity.KriRecord;
import com.internship.tool.repository.KriRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final KriRecordRepository kriRecordRepository;

    @Override
    public void run(String... args) {
        if (kriRecordRepository.count() > 0) {
            log.info("Database already seeded — skipping DataLoader.");
            return;
        }

        log.info("Seeding database with demo KRI records...");
        kriRecordRepository.saveAll(buildSeedRecords());
        log.info("Seeded {} KRI records successfully.", kriRecordRepository.count());
    }

    private List<KriRecord> buildSeedRecords() {
        LocalDate today = LocalDate.now();

        return List.of(
            // ── ACTIVE (future due dates) ─────────────────────────────────
            record("Credit Risk Exposure",       "Credit",      "ACTIVE",    92.0, today.plusDays(30)),
            record("Liquidity Coverage Ratio",   "Liquidity",   "ACTIVE",    88.5, today.plusDays(45)),
            record("Capital Adequacy Ratio",     "Capital",     "ACTIVE",    76.0, today.plusDays(60)),
            record("Operational Loss Events",    "Operations",  "ACTIVE",    65.5, today.plusDays(15)),
            record("Market Volatility Index",    "Market",      "ACTIVE",    83.0, today.plusDays(90)),
            record("Counterparty Default Risk",  "Credit",      "ACTIVE",    71.0, today.plusDays(20)),
            record("FX Exposure Limit",          "Market",      "ACTIVE",    95.0, today.plusDays(10)),
            record("Net Stable Funding Ratio",   "Liquidity",   "ACTIVE",    80.0, today.plusDays(75)),
            record("Large Exposure Threshold",   "Credit",      "ACTIVE",    68.0, today.plusDays(35)),
            record("Stress Test Result Q4",      "Capital",     "ACTIVE",    74.5, today.plusDays(50)),

            // ── COMPLETED (past due dates, resolved) ──────────────────────
            record("Q1 Capital Review",          "Capital",     "COMPLETED", 98.0, today.minusDays(10)),
            record("Annual Liquidity Audit",     "Liquidity",   "COMPLETED", 96.0, today.minusDays(20)),
            record("Credit Portfolio Review",    "Credit",      "COMPLETED", 89.0, today.minusDays(5)),
            record("Q2 Operational Review",      "Operations",  "COMPLETED", 91.0, today.minusDays(30)),
            record("Market Risk Assessment",     "Market",      "COMPLETED", 87.5, today.minusDays(15)),
            record("Compliance Gap Analysis",    "Compliance",  "COMPLETED", 93.0, today.minusDays(45)),
            record("IT Risk Review H1",          "Technology",  "COMPLETED", 85.0, today.minusDays(60)),
            record("Vendor Risk Assessment",     "Operations",  "COMPLETED", 90.0, today.minusDays(25)),
            record("Fraud Risk Evaluation",      "Compliance",  "COMPLETED", 94.0, today.minusDays(40)),
            record("Basel III Compliance Check", "Regulatory",  "COMPLETED", 99.0, today.minusDays(90)),

            // ── OVERDUE (past due dates, not completed) ───────────────────
            record("Overdue Credit Limit Review","Credit",      "OVERDUE",   55.0, today.minusDays(3)),
            record("Pending Liquidity Report",   "Liquidity",   "OVERDUE",   48.0, today.minusDays(7)),
            record("Late Capital Submission",    "Capital",     "OVERDUE",   60.0, today.minusDays(2)),
            record("Operational Risk Backlog",   "Operations",  "OVERDUE",   42.0, today.minusDays(14)),
            record("Overdue Market Stress Test", "Market",      "OVERDUE",   50.0, today.minusDays(5)),
            record("Delayed Compliance Filing",  "Compliance",  "OVERDUE",   38.0, today.minusDays(21)),
            record("IT Audit Overdue",           "Technology",  "OVERDUE",   45.0, today.minusDays(10)),
            record("Vendor SLA Breach Review",   "Operations",  "OVERDUE",   52.0, today.minusDays(18)),
            record("Fraud Incident Follow-up",   "Compliance",  "OVERDUE",   35.0, today.minusDays(28)),
            record("Regulatory Deadline Missed", "Regulatory",  "OVERDUE",   30.0, today.minusDays(35))
        );
    }

    private KriRecord record(String title, String category, String status,
                             double score, LocalDate dueDate) {
        return KriRecord.builder()
                .title(title)
                .description("Auto-seeded record for demo purposes.")
                .category(category)
                .status(status)
                .score(score)
                .dueDate(dueDate)
                .build();
    }
}
