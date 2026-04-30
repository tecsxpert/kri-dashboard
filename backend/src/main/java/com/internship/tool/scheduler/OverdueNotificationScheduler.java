package com.internship.tool.scheduler;

import com.internship.tool.entity.KriRecord;
import com.internship.tool.repository.KriRecordRepository;
import com.internship.tool.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class OverdueNotificationScheduler {

    private final KriRecordRepository kriRecordRepository;
    private final EmailService emailService;

    public OverdueNotificationScheduler(KriRecordRepository kriRecordRepository,
                                        EmailService emailService) {
        this.kriRecordRepository = kriRecordRepository;
        this.emailService = emailService;
    }

    /**
     * Runs every hour. Fetches all records, filters those that are overdue
     * (dueDate < today AND status != "completed"), and sends an overdue
     * notification email for each.
     */
    @Scheduled(fixedRateString = "PT1H")   // ISO-8601 duration — every 60 minutes
    public void checkAndNotifyOverdueRecords() {
        log.info("Running overdue KRI check...");

        List<KriRecord> allRecords = kriRecordRepository.findAll();
        LocalDate today = LocalDate.now();

        List<KriRecord> overdue = allRecords.stream()
                .filter(r -> r.getDueDate() != null)
                .filter(r -> r.getDueDate().isBefore(today))
                .filter(r -> !"completed".equalsIgnoreCase(r.getStatus()))
                .toList();

        if (overdue.isEmpty()) {
            log.info("No overdue KRI records found.");
            return;
        }

        log.info("Found {} overdue KRI record(s). Sending notifications...", overdue.size());
        overdue.forEach(record -> {
            emailService.sendOverdueNotification(record);
            log.info("Overdue notification sent for record id={} title='{}'",
                    record.getId(), record.getTitle());
        });
    }
}
