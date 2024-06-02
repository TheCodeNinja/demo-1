package com.example.demo.scheduler;

import com.example.demo.config.EscalationInfoManager;
import com.example.demo.entity.Escalation;
import com.example.demo.model.EscalationInfo;
import com.example.demo.repository.EscalationRepository;
import com.example.demo.service.EscalationService;
import com.example.demo.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class EscalationScheduler {

    @Autowired
    private EscalationRepository escalationRepo;

    @Autowired
    private EscalationService escalationService;

    @Autowired
    private EscalationInfoManager escalationInfoManager;

    @Scheduled(cron = "*/30 * * * * *")
    public void checkPendingRequests() {
        log.info("\u001B[34m**************************************************\u001B[0m");
        log.info("\u001B[34m| Checking the current HashMap every half minute |\u001B[0m");
        log.info("\u001B[34m**************************************************\u001B[0m");
        long currentTime = System.currentTimeMillis();
        Map<String, EscalationInfo> eInfoMap = escalationInfoManager.getEscalationInfoMap();
        Iterator<String> iterator = eInfoMap.keySet().iterator();

        while (iterator.hasNext()) {
            String taskId = iterator.next();
            EscalationInfo eInfo = eInfoMap.get(taskId);
            if (eInfo != null) {
                long sentTimestamp = eInfo.getSentTimestamp();
                String supportCorpId = eInfo.getSupportCorpId();
                int escalationNum = eInfo.getEscalationNum();
                Escalation escalation = escalationRepo.findByTaskId(taskId);
                if (escalation != null) {
                    if (escalation.isAck()) {
                        log.info("\u001B[32mTaskId {} was acked by supporter {}\u001B[0m", taskId, supportCorpId);
                        log.info("\u001B[32m[Task ID]: {}, [Escalation Line]: {}, [Supporter]: {}, [Time Sent]: {}, [Time Passed]: {}, [isAck]: {}\u001B[0m",
                                taskId,
                                escalationNum+1,
                                supportCorpId,
                                DateUtils.formatTimestamp(eInfo.getSentTimestamp()),
                                (currentTime - sentTimestamp) / 60000,
                                escalation.isAck());
                        // Remove the taskId from the map
                        iterator.remove();
                    } else {
                        if (currentTime - sentTimestamp > 240000 && eInfo.getEscalationNum() == 1) {
                            log.info("\u001B[31mTaskId {} not acked by supporter {} within 4 mins\u001B[0m", taskId, supportCorpId);
                            log.info("\u001B[31m[Task ID]: {}, [Escalation Line]: {}, [Supporter]: {}, [Time Sent]: {}, [Time Passed]: {}, [isAck]: {}\u001B[0m",
                                    taskId,
                                    escalationNum+1,
                                    supportCorpId,
                                    DateUtils.formatTimestamp(eInfo.getSentTimestamp()),
                                    (currentTime - sentTimestamp) / 60000,
                                    escalation.isAck());
                            escalationService.sendToCallMyItBackend(taskId, escalation.getThirdSupportCorpId());
                        } else if (currentTime - sentTimestamp > 120000 && eInfo.getEscalationNum() == 0) {
                            log.info("\u001B[31mTaskId {} not acked by supporter {} within 2 mins\u001B[0m", taskId, supportCorpId);
                            log.info("\u001B[31m[Task ID]: {}, [Escalation Line]: {}, [Supporter]: {}, [Time Sent]: {}, [Time Passed]: {}, [isAck]: {}\u001B[0m",
                                    taskId,
                                    escalationNum+1,
                                    supportCorpId,
                                    DateUtils.formatTimestamp(eInfo.getSentTimestamp()),
                                    (currentTime - sentTimestamp) / 60000,
                                    escalation.isAck());
                            escalationService.sendToCallMyItBackend(taskId, escalation.getSecondSupportCorpId());
                        } else {
                            log.info("[Task ID]: {}, [Escalation Line]: {}, [Supporter]: {}, [Time Sent]: {}, [Time Passed]: {}, [isAck]: {}",
                                    taskId,
                                    escalationNum+1,
                                    supportCorpId,
                                    DateUtils.formatTimestamp(eInfo.getSentTimestamp()),
                                    (currentTime - sentTimestamp) / 60000,
                                    escalation.isAck());
                        }
                    }
                }


            }
        }
    }
}
