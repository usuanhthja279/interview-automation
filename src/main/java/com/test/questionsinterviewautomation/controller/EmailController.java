package com.test.questionsinterviewautomation.controller;

import com.test.questionsinterviewautomation.model.EmailRequest;
import com.test.questionsinterviewautomation.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@Slf4j
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(value = "/send", consumes = "application/json")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest.to(), emailRequest.subject(), emailRequest.body());
            log.info("Email sent to: {}", emailRequest.to());
            return "Email sent successfully";
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            return "Error sending email";
        }
    }
}
