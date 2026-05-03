package com.felicita.service;

import java.util.List;

public interface EmailService {

    void sendFromDev(String to, String subject, String body);

    void sendFromDev(String to, List<String> cc, String subject, String body);

    void sendFromAdmin(String to, String subject, String body);

    void sendFromMain(String to, String subject, String body);

    void sendPlainTextFromDev(String to, String subject, String body);

    void sendPlainTextFromMain(String to, String subject, String body);
}
