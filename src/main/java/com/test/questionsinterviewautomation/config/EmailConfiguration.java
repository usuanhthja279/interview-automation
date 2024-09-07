package com.test.questionsinterviewautomation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

@Configuration
@Slf4j
public class EmailConfiguration {
    private final Environment env;

    public EmailConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public JavaMailSender javaMailSenderPersonal() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.mail.port"))));
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
        properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));

        mailSender.setJavaMailProperties(properties);

        log.info("JavaMailSender bean created: {}", mailSender);
        return mailSender;
    }
}
