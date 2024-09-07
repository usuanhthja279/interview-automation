package com.test.questionsinterviewautomation.controller;

import com.test.questionsinterviewautomation.model.PromptRequest;
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
    @PostMapping(value = "/promt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResponseFromPrompt(@RequestBody PromptRequest promptRequest) {

        // TODO: Implement the logic to return the response based on the prompt
        log.info("Prompt received: {}", promptRequest.prompt());

        return ResponseEntity.ok("You have entered: " + promptRequest.prompt());
    }
}
