package com.internship.tool.repository;

import com.internship.tool.entity.KriRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KriRecordRepository extends JpaRepository<KriRecord, Long> {

    Optional<KriRecord> findByTitle(String title);
}
