package com.example.demo.service;

import com.example.demo.config.EscalationInfoManager;
import com.example.demo.entity.Escalation;
import com.example.demo.model.CallMyItRequest;
import com.example.demo.model.EscalationInfo;
import com.example.demo.repository.EscalationRepository;
import com.example.demo.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EscalationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EscalationRepository escalationRepo;

    @Autowired
    private EscalationInfoManager escalationInfoManager;

    public void sendToCallMyItBackend(String taskId, String supportCorpId) {
        String url = "http://localhost:8081/call-my-it";
        CallMyItRequest request = new CallMyItRequest(taskId, supportCorpId);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        Map<String, EscalationInfo> eInfoMap = escalationInfoManager.getEscalationInfoMap();

        if (!eInfoMap.containsKey(taskId)) {
            // Create escalation info
            long sentTimestamp = System.currentTimeMillis();
            eInfoMap.put(taskId, new EscalationInfo(sentTimestamp, supportCorpId,0));
        } else {
            // Update escalation info and update the map
            EscalationInfo eInfo = eInfoMap.get(taskId);
            int currentRequestNum = eInfo.getEscalationNum();
            eInfo.setSupportCorpId(supportCorpId);
            eInfo.setEscalationNum(currentRequestNum + 1); // Increment requestNum by 1
            eInfoMap.put(taskId, eInfo);
        }
    }

    public void triggerFirstSupport(String taskId) {
        Escalation escalation = escalationRepo.findByTaskId(taskId);
        if (escalation != null && !escalation.isAck()) {
            sendToCallMyItBackend(taskId, escalation.getFirstSupportCorpId());
        }
    }

}
