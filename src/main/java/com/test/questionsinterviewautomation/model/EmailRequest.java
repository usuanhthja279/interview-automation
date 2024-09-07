package com.test.questionsinterviewautomation.model;

import java.util.List;

public record EmailRequest(List<String> to, String subject, String body) {
}
