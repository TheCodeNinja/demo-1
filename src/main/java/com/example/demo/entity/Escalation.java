package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Escalation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskId;

    private String firstSupportCorpId;

    private String secondSupportCorpId;

    private String thirdSupportCorpId;

    @Column(columnDefinition = "BIT(1) DEFAULT 0")
    private boolean isAck;

    @Column(nullable = true)
    private LocalDateTime ackAt;

    @Column(nullable = true)
    private String ackBy;
}