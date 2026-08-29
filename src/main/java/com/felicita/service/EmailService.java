package com.felicita.service;

import com.felicita.model.request.email.HotelRatesRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.InsertResponse;

import java.util.List;

public interface EmailService {

    void sendFromDev(String to, String subject, String body);

    void sendFromDev(String to, List<String> cc, String subject, String body);

    void sendFromAdmin(String to, String subject, String body);

    void sendFromMain(String to, String subject, String body);

    void sendPlainTextFromDev(String to, String subject, String body);

    void sendPlainTextFromMain(String to, String subject, String body);

    void sendFromInfo(List<String> to,List<String> cc, String subject);

    CommonResponse<InsertResponse> requestHotelRates(HotelRatesRequest hotelRatesRequest);
}
