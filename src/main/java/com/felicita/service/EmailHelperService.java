package com.felicita.service;

import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.request.TourBookingInquiryRequest;

public interface EmailHelperService {
    String buildAdminTourBookingSubject(TourBookingInquiryRequest tourBookingInquiryRequest);

    String buildAdminTourBookingBody(TourBookingInquiryRequest tourBookingInquiryRequest, Long tourBookingInquiryId);

    String buildCustomerTourBookingSubject();

    String buildCustomerTourBookingBody(TourBookingInquiryRequest tourBookingInquiryRequest);

    String buildChatBotInquirySubject(ChatBotRequest chatBotRequest);

    String buildChatBotInquiryEmailBody(ChatBotRequest chatBotRequest);

    String buildInquirySubject(CreateInquiryRequest createInquiryRequest);

    String buildInquiryEmailBody(CreateInquiryRequest createInquiryRequest);

    String buildCustomerConfirmationEmailBody(ChatBotRequest chatBotRequest);

    String buildCustomerInquirySubject(CreateInquiryRequest createInquiryRequest);

    String buildCustomerInquiryEmailBody(CreateInquiryRequest createInquiryRequest);
}
