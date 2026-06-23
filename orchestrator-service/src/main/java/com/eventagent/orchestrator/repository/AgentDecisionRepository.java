package com.eventagent.orchestrator.repository;

import com.eventagent.orchestrator.domain.AgentDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentDecisionRepository extends JpaRepository<AgentDecisionEntity, Long> {
    Optional<AgentDecisionEntity> findByDecisionId(String decisionId);
}
