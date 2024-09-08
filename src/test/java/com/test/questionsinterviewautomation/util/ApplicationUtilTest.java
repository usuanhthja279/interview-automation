package com.test.questionsinterviewautomation.util;

import com.test.questionsinterviewautomation.service.FileService;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationUtilTest {

    @InjectMocks
    private ApplicationUtil applicationUtil;
    @Mock
    private FileService fileService;

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
            JsonObject promptMessageToJson = applicationUtil.modifyPromptMessageToJson(prompt);
            assertNotNull(promptMessageToJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getMessageForEmailBody() {
        try {
            String emailBody = applicationUtil.getMessageForEmailBody(prompt);
            assertNotNull(emailBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateHistoryResponse() {
        try {
            applicationUtil.updateHistoryResponse(prompt);
            assertNotNull(prompt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}