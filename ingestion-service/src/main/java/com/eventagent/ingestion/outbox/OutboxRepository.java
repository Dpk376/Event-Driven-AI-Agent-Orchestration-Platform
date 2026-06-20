package com.eventagent.ingestion.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntry, UUID> {
    List<OutboxEntry> findByStatusOrderByCreatedAtAsc(OutboxEntry.Status status);
}
