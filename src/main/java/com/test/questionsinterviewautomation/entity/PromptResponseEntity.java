package com.test.questionsinterviewautomation.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;


import java.util.List;

@Getter
@Setter
@ToString
public class PromptResponseEntity {
    @Id
    private String id;
    private List<String> javaQuestions;
    private List<String> streamLambdaQuestions;
    private List<String> dsaQuestions;
    private List<String> dailyProjects;
}
