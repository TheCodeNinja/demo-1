package com.example.demo.repository;

import com.example.demo.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Escalation findByTaskId(String taskId);

    //void deleteByTaskId(String taskId);
}