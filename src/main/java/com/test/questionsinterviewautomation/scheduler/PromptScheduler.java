package com.test.questionsinterviewautomation.scheduler;

import com.test.questionsinterviewautomation.constant.ApplicationConstant;
import com.test.questionsinterviewautomation.service.EmailService;
import com.test.questionsinterviewautomation.service.PromptService;
import com.test.questionsinterviewautomation.util.ApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class PromptScheduler {
    private final PromptService promptService;
    private final EmailService emailService;

    public PromptScheduler(PromptService promptService, EmailService emailService) {
        this.promptService = promptService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void scheduledPromptResponse() {
        try {
            log.info("Sending prompt to the user");
            String prompt = "Generate 10 unique Java + Spring interview questions 3 easy(definition type), 3 medium(definition + analysis type) and 4 hard(analysis type) type in this format: \"javaSpring\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "7 practice problems on Streams and Lambdas in this format: \"streamsLambdas\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "3 DSA interview type problems one easy one medium one hard with descriptive problems in this format: \"dsaProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "2 SQL interview type problems one easy one medium with descriptive problems in this format: \"sqlProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "2 system design interview type problems one low level one high level with descriptive problems in this format: \"systemDesignProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}],, "
                    + "2 Vertx/Completable Future descriptive problems in this format: \"vertxCompletablefuture\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "2 small descriptive project ideas on Spring + Java \"projectIdeas\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}]. "
                    + "Please add key points of answer for the questions. "
                    + "Please provide the response in JSON text format question key will be question and keyPoints key will be keyPoints.";

            String response = promptService.executePrompt(prompt);
            log.info("Response from OpenAI successfully received");

            String emailStructuredMessage = ApplicationUtil.getMessageForEmailBody(response);
            emailService.sendEmail(ApplicationConstant.mailList,
                    "RE: Interview Prep ::: JAVA ::: " + LocalDateTime.now(ZoneId.of("Asia/Kolkata")),
                    emailStructuredMessage);
            log.info("Prompt response sent successfully");
        } catch (Exception e) {
            log.error("Error sending prompt response: {}", e.getMessage());
        }
    }
}
