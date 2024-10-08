package com.test.questionsinterviewautomation.service;

import com.test.questionsinterviewautomation.util.ApplicationUtil;
import com.test.questionsinterviewautomation.util.ErrorUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class PromptControllerService {
    private final PromptService promptService;
    private final EmailService emailService;
    private final FileService fileService;
    private final ApplicationUtil applicationUtil;

    public PromptControllerService(PromptService promptService, EmailService emailService, FileService fileService, ApplicationUtil applicationUtil) {
        this.promptService = promptService;
        this.emailService = emailService;
        this.fileService = fileService;
        this.applicationUtil = applicationUtil;
    }

    public String fetchPromptResponseAndSendToMail(List<String> typeList, List<String> mails) {
        try {
            //Reading All Levels from File
            JsonObject levels = fileService.readJsonFromFile("E:/questions-interview-automation/src/main/resources/level.json");

            //Getting Distinct Levels for Prompt Request
            Set<String> distinctLevels;
            if (levels.isEmpty()) {
                fileService.writeJsonToFile("E:/questions-interview-automation/src/main/resources/level.json",
                        new JsonObject().put("levels", mails.stream().map(mail -> new JsonObject().put("mail", mail).put("level", "0")).collect(JsonArray::new, JsonArray::add, JsonArray::addAll)));
                levels = fileService.readJsonFromFile("E:/questions-interview-automation/src/main/resources/level.json");
                distinctLevels = Set.of("1");
            } else {
                distinctLevels = ApplicationUtil.getDistinctLevelsOnMails(mails, levels);
            }

            //Prompt Request Creation Based on Distinct Levels
            JsonArray prompts = distinctLevels.stream()
                    .map(level -> new JsonObject().put("level", level)
                            .put("promptRequest", applicationUtil.createCustomPrompt(typeList, level)))
                    .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);

            //Fetching Prompt Response for each (promptRequest, level) pair
            long startTime = System.currentTimeMillis();
            log.info("Prompt Response fetching Started: {}", startTime);
            Map<String, String> promptResponse = new HashMap<>();
            prompts.stream()
                    .map(JsonObject::mapFrom)
                    .forEach(prompt -> promptResponse.put(prompt.getString("level"),
                            promptService.executePrompt(prompt.getString("promptRequest"))));
            log.info("Prompt response successfully received: {}", promptResponse);
            log.info("Prompt Response fetching Completed: {}", System.currentTimeMillis());

            log.info("Total Time taken for fetching prompt response: {} ms", System.currentTimeMillis() - startTime);

            //Sending Email for each level and updating history response in json file
            JsonObject finalLevels = levels;
            promptResponse.forEach((level, response) -> {
                String emailStructuredMessage = applicationUtil.getMessageForEmailBody(response, level);
                List<String> levelMailList = ApplicationUtil.getMailsForLevels(mails, finalLevels, level);
                try {
                    emailService.sendEmail(levelMailList, "RE: Interview Prep ::: JAVA ::: " + level,
                            emailStructuredMessage);
                } catch (MessagingException e) {
                    ErrorUtil.printStackTraceDepth(PromptControllerService.class.getName(), e);
                    throw new RuntimeException(e);
                }
                log.info("Prompt response sent successfully for level: {}", level);

                levelMailList.forEach(mail -> {
                    applicationUtil.updateHistoryResponse(response, level, mail);
                    log.info("Updating history response successfully completed for level: {}", level);

                    //Updating levels.json file with new response - Use of inheritance and polymorphism
                    for (Object o : finalLevels.getJsonArray("levels")) {
                        JsonObject levelMail = (JsonObject) o;
                        if (levelMail.getString("mail").equals(mail)) {
                            levelMail.put("level", String.valueOf(Integer.parseInt(levelMail.getString("level")) + 1));
                            break;
                        }
                    }
                    fileService.writeJsonToFile("E:/questions-interview-automation/src/main/resources/level.json", finalLevels);
                    log.info("Updating levels.json file with new response successfully completed for mail: {}", mail);
                });
            });

            //Sending Response to Controller/Scheduler
            return promptResponse.toString();
        } catch (Exception ex) {
            //Sending Failure Response to Controller/Scheduler
            log.error("Error occurred while fetching prompt response: {}", ex.getMessage());
            ErrorUtil.printStackTraceDepth(PromptControllerService.class.getName(), ex);
            return "Failure";
        }

    }
}
