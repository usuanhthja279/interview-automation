package com.test.questionsinterviewautomation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class OpenAIConfig {
    private final Environment environment;

    public OpenAIConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAiApi openAiApi() {
        OpenAiApi openAiApi = new OpenAiApi(environment.getProperty("spring.ai.openai.api-key"));
        log.info("OpenAI API bean created: {}", openAiApi);
        return openAiApi;
    }
}
