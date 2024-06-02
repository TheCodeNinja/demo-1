package com.example.demo.config;

import com.example.demo.model.EscalationInfo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EscalationInfoManager {
    private final ConcurrentHashMap<String, EscalationInfo> escalationInfoMap = new ConcurrentHashMap<>();

    public Map<String, EscalationInfo> getEscalationInfoMap() {
        return escalationInfoMap;
    }
}