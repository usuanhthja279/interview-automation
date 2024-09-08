package com.test.questionsinterviewautomation.scheduler;

import com.test.questionsinterviewautomation.constant.ApplicationConstant;
import com.test.questionsinterviewautomation.service.EmailService;
import com.test.questionsinterviewautomation.service.FileService;
import com.test.questionsinterviewautomation.service.PromptService;
import com.test.questionsinterviewautomation.util.ApplicationUtil;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class PromptScheduler {
    private final PromptService promptService;
    private final EmailService emailService;
    private final ApplicationUtil applicationUtil;
    private final FileService fileService;

    public PromptScheduler(PromptService promptService, EmailService emailService, ApplicationUtil applicationUtil, FileService fileService) {
        this.promptService = promptService;
        this.emailService = emailService;
        this.applicationUtil = applicationUtil;
        this.fileService = fileService;
    }

    @Scheduled(cron = "0 0 10 * * * ", zone = "Asia/Kolkata")
    public void scheduledPromptResponse() {
        try {
            log.info("Sending prompt to the user");
            JsonObject level = fileService.readJsonFromFile("E:/questions-interview-automation/src/main/resources/level.json");
            String levelValue = level.getString("level");
            String prompt = "Generate 6 unique Java interview questions with difficulty level: " + levelValue + ", in this format: \"javaQuestions\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "6 unique Spring+SpringBoot interview questions with difficulty level: " + levelValue + ", in this format: \"springQuestions\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "7 practice problems on Streams and Lambdas with difficulty level: " + levelValue + ", in this format: \"streamsLambdas\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "3 DSA interview type problems one easy one medium one hard with descriptive problems with difficulty level: " + levelValue + ", in this format: \"dsaProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "4 SQL interview type problems one easy one medium with descriptive problems with difficulty level: " + levelValue + ", in this format: \"sqlProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "2 system design interview type problems one low level one high level with descriptive problems with difficulty level: " + levelValue + ", in this format: \"systemDesignProblems\": [{\"type\": \"easy\", \"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}],, "
                    + "2 Vertx/Completable Future descriptive problems with difficulty level: " + levelValue + ", in this format: \"vertxCompletablefuture\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}], "
                    + "2 small descriptive project ideas on Spring + Java with difficulty level: " + levelValue + ", in this format: \"projectIdeas\": [{\"question\": \"value\", \"keyPoints\": [\"value1\", \"value2\"]}]. "
                    + "Please add key points of answer for the questions. "
                    + "Please provide the response in JSON text format.";

            String response = promptService.executePrompt(prompt);
            log.info("Response from OpenAI successfully received");

            applicationUtil.updateHistoryResponse(response);
            log.info("Updating history response successfully completed");

            String emailStructuredMessage = applicationUtil.getMessageForEmailBody(response);
            emailService.sendEmail(ApplicationConstant.mailList,
                    "RE: Interview Prep ::: JAVA ::: " + LocalDateTime.now(ZoneId.of("Asia/Kolkata")),
                    emailStructuredMessage);
            log.info("Prompt response sent successfully");
            fileService.writeJsonToFile("E:/questions-interview-automation/src/main/resources/level.json",
                    new JsonObject().put("level", String.valueOf(Integer.parseInt(levelValue) + 1)));
        } catch (Exception e) {
            log.error("Error sending prompt response: {}", e.getMessage());
        }
    }
}
