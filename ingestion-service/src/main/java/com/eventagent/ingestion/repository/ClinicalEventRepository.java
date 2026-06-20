package com.eventagent.ingestion.repository;

import com.eventagent.ingestion.entity.ClinicalEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicalEventRepository extends JpaRepository<ClinicalEventEntity, UUID> {
    Optional<ClinicalEventEntity> findByIdempotencyKey(String idempotencyKey);
}
