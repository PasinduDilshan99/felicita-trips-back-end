package com.felicita.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean(name = "devMailSender")
    public JavaMailSender devMailSender() {
        return createMailSender(
                "smtp.gmail.com",
                587,
                "dev.felicitatrips@gmail.com",
                "step rvvg uvsb xpgb"
        );
    }

    @Bean(name = "mainMailSender")
    public JavaMailSender mainMailSender() {
        return createMailSender(
                "smtp.gmail.com",
                587,
                "felicitatrips@gmail.com",
                "jlis otag gjrj jtdb"
        );
    }

    @Bean(name = "adminMailSender")
    public JavaMailSender adminMailSender() {
        return createMailSender(
                "smtp.gmail.com",
                587,
                "felicitatrips@gmail.com",
                "jlis otag gjrj jtdb"
        );
    }

    private JavaMailSender createMailSender(String host, int port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}