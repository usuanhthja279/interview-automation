package com.test.questionsinterviewautomation.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Slf4j
public class PromptScheduler {

    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Kolkata")
    public void scheduledPromptResponse() {
        log.info("Sending prompt to the user");
        // TODO: Implement the logic to send the prompt to the user
    }
}
