package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallMyItRequest {

    private String taskId;
    private String supportCorpId;

}
