package com.felicita.service;

public interface EmailService {

    void sendFromDev(String to, String subject, String body);

    void sendFromMain(String to, String subject, String body);

    void sendPlainTextFromDev(String to, String subject, String body);

    void sendPlainTextFromMain(String to, String subject, String body);
}
