package com.test.questionsinterviewautomation.controller;

import com.test.questionsinterviewautomation.model.PromptRequest;
import com.test.questionsinterviewautomation.service.PromptControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class PromtController {
    private final PromptControllerService promptControllerService;

    public PromtController(PromptControllerService promptControllerService) {
        this.promptControllerService = promptControllerService;
    }

    @PostMapping(value = "/prompt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResponseFromPrompt(@RequestBody PromptRequest promptRequest) {
        log.info("Request received for prompt: {}", promptRequest);

        String response = promptControllerService.fetchPromptResponseAndSendToMail(promptRequest.type(), promptRequest.mail());
        log.info("Response from prompt: {}", response);

        return ResponseEntity.ok(response);
    }
}
