package com.test.questionsinterviewautomation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@Slf4j
public class QuestionsInterviewAutomationApplication {

    public static void main(String[] args) {
        log.info("Starting the application");
        SpringApplication.run(QuestionsInterviewAutomationApplication.class, args);
    }

}
