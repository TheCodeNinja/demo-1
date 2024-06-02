package com.example.demo.controller;

import com.example.demo.service.EscalationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/call-escalation")
public class EscalationController {

    @Autowired
    private EscalationService escalationService;

    @PostMapping("/triggerFirstSupport")
    public void triggerFirstSupport(@RequestParam String taskId) {
        log.info("Create task: {}", taskId);
        escalationService.triggerFirstSupport(taskId);
    }

    @PostMapping
    public void handleCallback(@RequestParam String taskId) {
        log.info("Accepting request from CallMyIT: " + taskId);
        // escalationService.handleCallback(taskId);
    }
}
