package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EscalationInfo {

    private long sentTimestamp;
    private String supportCorpId;
    private int escalationNum;

}
