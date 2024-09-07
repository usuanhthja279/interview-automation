package com.test.questionsinterviewautomation.service;

import com.test.questionsinterviewautomation.config.OpenAIConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = {PromptService.class, OpenAIConfig.class})
class PromptServiceTest {

    @InjectMocks
    PromptService promptService;

    @Mock
    OpenAiApi openAiApi;

    @Test
    @DisplayName("Test executePrompt")
    void executePrompt() {
        try {
            String mockPromptResponse = Files.readString(Path.of(ClassLoader.getSystemResource("response/mock-response-open-ai-java-spring.txt").toURI()));

            String prompt = Files.readString(Path.of(ClassLoader.getSystemResource("request/mock-request-open-ai-java-spring.txt").toURI()));

            OpenAiApi.ChatCompletion completion = getChatCompletion(mockPromptResponse);
            Mockito.when(openAiApi.chatCompletionEntity(any())).thenReturn(ResponseEntity.ok(completion));

            String response = promptService.executePrompt(prompt);
            Assertions.assertEquals(mockPromptResponse, response);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static OpenAiApi.ChatCompletion getChatCompletion(String promptResponse) {
        OpenAiApi.ChatCompletionMessage chatCompletionMessage = new OpenAiApi.ChatCompletionMessage(promptResponse, OpenAiApi.ChatCompletionMessage.Role.USER);
        OpenAiApi.ChatCompletion.Choice choice = new OpenAiApi.ChatCompletion.Choice(null, null, chatCompletionMessage, null);
        return new OpenAiApi.ChatCompletion(null, List.of(choice), null,
                null, null, null, null);
    }
}