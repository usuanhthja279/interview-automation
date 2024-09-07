package com.test.questionsinterviewautomation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PromptService {
    private final OpenAiApi openAiApi;

    public PromptService(OpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
    }

    public String executePrompt(String prompt) {
        List<OpenAiApi.ChatCompletionMessage> chatCompletionMessages = new ArrayList<>();
        chatCompletionMessages.add(new OpenAiApi.ChatCompletionMessage(prompt, OpenAiApi.ChatCompletionMessage.Role.USER));

        OpenAiApi.ChatCompletionRequest completionRequest =
                new OpenAiApi.ChatCompletionRequest(chatCompletionMessages, "gpt-4o-mini", 0.8f);
        ResponseEntity<OpenAiApi.ChatCompletion> completionResponseEntity =
                openAiApi.chatCompletionEntity(completionRequest);
        log.info("Response from OpenAI: {}", completionResponseEntity.getBody());

        return Objects.requireNonNull(completionResponseEntity.getBody()).choices().get(0).message().content();
    }
}
