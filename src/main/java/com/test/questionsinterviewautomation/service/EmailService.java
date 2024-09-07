package com.test.questionsinterviewautomation.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender javaMailSenderPersonal;

    public EmailService(JavaMailSender javaMailSenderPersonal) {
        this.javaMailSenderPersonal = javaMailSenderPersonal;
    }

    public void sendEmail(List<String> to, String subject, String body) throws MessagingException {
        // Create a MimeMessage
        MimeMessage message = javaMailSenderPersonal.createMimeMessage();

        // Use MimeMessageHelper for formatting
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


        for (String s : to) {
            // Set email parameters
            helper.setFrom("usuanhthja279@gmail.com");
            helper.setTo(new InternetAddress(s));
            helper.setSubject(subject);
            helper.setText(body, true); // `true` enables HTML

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//
//        simpleMailMessage.setFrom("usuanhthja279@gmail.com");
//        simpleMailMessage.setTo(to.toArray(String[]::new));
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage
//        simpleMailMessage.setText(body);

            javaMailSenderPersonal.send(message);
            log.info("Email sent to: {}", to);
        }
    }
}
