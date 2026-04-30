package com.internship.tool.repository;

import com.internship.tool.entity.KriRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@EnableJpaAuditing
class KriRecordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KriRecordRepository kriRecordRepository;

    // ================================================================== //
    //  save + findById                                                    //
    // ================================================================== //

    @Test
    @DisplayName("save and findById: persists record and retrieves it by ID")
    void save_andFindById_returnsRecord() {
        KriRecord record = buildRecord("Save Test", "active");
        KriRecord saved = kriRecordRepository.save(record);
        entityManager.flush();
        entityManager.clear();

        Optional<KriRecord> found = kriRecordRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Save Test");
        assertThat(found.get().getCategory()).isEqualTo("Finance");
        assertThat(found.get().getCreatedAt()).isNotNull();
        assertThat(found.get().getUpdatedAt()).isNotNull();
    }

    // ================================================================== //
    //  findByTitle — present                                              //
    // ================================================================== //

    @Test
    @DisplayName("findByTitle: returns record when title exists")
    void findByTitle_titleExists_returnsRecord() {
        entityManager.persistAndFlush(buildRecord("Existing Title", "active"));
        entityManager.clear();

        Optional<KriRecord> result = kriRecordRepository.findByTitle("Existing Title");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Existing Title");
    }

    // ================================================================== //
    //  findByTitle — not found                                            //
    // ================================================================== //

    @Test
    @DisplayName("findByTitle: returns empty Optional when title does not exist")
    void findByTitle_titleMissing_returnsEmpty() {
        Optional<KriRecord> result = kriRecordRepository.findByTitle("Non-existent Title");

        assertThat(result).isEmpty();
    }

    // ------------------------------------------------------------------ //
    //  Helper                                                             //
    // ------------------------------------------------------------------ //

    private KriRecord buildRecord(String title, String status) {
        return KriRecord.builder()
                .title(title)
                .category("Finance")
                .status(status)
                .score(75.0)
                .build();
    }
}
