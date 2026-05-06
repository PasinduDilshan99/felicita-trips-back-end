package com.felicita.service.impl;

import com.felicita.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceimpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceimpl.class);

    @Autowired
    @Qualifier("devMailSender")
    private JavaMailSender devMailSender;

    @Autowired
    @Qualifier("mainMailSender")
    private JavaMailSender mainMailSender;

    @Autowired
    @Qualifier("adminMailSender")
    private JavaMailSender adminMailSender;

    @Override
    public void sendFromDev(String to, String subject, String body) {
        sendHtmlEmail(devMailSender, to, subject, body);
    }

    @Override
    public void sendFromDev(String to, List<String> cc, String subject, String body) {
        sendHtmlEmail(devMailSender, to, cc, subject, body);
    }

    @Override
    public void sendFromAdmin(String to, String subject, String body) {
        sendHtmlEmail(adminMailSender, to, subject, body);
    }

    @Override
    public void sendFromMain(String to, String subject, String body) {
        sendHtmlEmail(mainMailSender, to, subject, body);
    }

    @Override
    public void sendPlainTextFromDev(String to, String subject, String body) {
        sendPlainTextEmail(devMailSender, to, subject, body);
    }

    @Override
    public void sendPlainTextFromMain(String to, String subject, String body) {
        sendPlainTextEmail(mainMailSender, to, subject, body);
    }

    /**
     * Send HTML email
     */
    private void sendHtmlEmail(JavaMailSender sender, String to, String subject, String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML content

            sender.send(message);
            LOGGER.info("HTML email sent successfully to: {}", to);

        } catch (Exception e) {
            LOGGER.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    private void sendHtmlEmail(JavaMailSender sender,
                               String to,
                               List<String> cc,
                               String subject,
                               String htmlBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }

            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            sender.send(message);

            LOGGER.info("HTML email sent successfully to: {}", to);

        } catch (Exception e) {
            LOGGER.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Send plain text email
     */
    private void sendPlainTextEmail(JavaMailSender sender, String to, String subject, String textBody) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textBody, false); // false indicates plain text

            sender.send(message);
            LOGGER.info("Plain text email sent successfully to: {}", to);

        } catch (Exception e) {
            LOGGER.error("Failed to send plain text email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send plain text email", e);
        }
    }
}