package com.test.questionsinterviewautomation.util;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationUtilTest {

    private String prompt;

    @BeforeEach
    void setUp() {
        try {
            prompt = Files.readString(Path.of(ClassLoader.getSystemResource("response/mock-response-open-ai-java-spring.txt").toURI()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void modifyPromptMessageToList() {
        try {
            JsonObject promptMessageToJson = ApplicationUtil.modifyPromptMessageToJson(prompt);
            assertNotNull(promptMessageToJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMessageForEmailBody() {
        try {
            String emailBody = ApplicationUtil.getMessageForEmailBody(prompt);
            assertNotNull(emailBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateHistoryResponse() {
        try {
            ApplicationUtil.updateHistoryResponse(prompt);
            assertNotNull(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}