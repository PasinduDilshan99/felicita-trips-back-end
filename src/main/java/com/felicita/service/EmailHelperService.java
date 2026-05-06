package com.felicita.service;

import com.felicita.model.dto.DestinationResponseDto;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.TourBookingInquiryRequest;
import com.felicita.security.model.User;

import java.util.List;

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
    
    String buildDestinationCreateSuccessfullBody(DestinationInsertRequest destinationInsertRequest, List<String> destinationCategories, User loggedUser);

    String buildDestinationCreateSuccessfullSubject(DestinationInsertRequest destinationInsertRequest, User loggedUser);

    String buildDestinationUpdateSuccessfullSubject(User loggedUser);

    String buildDestinationUpdateSuccessfullBody(User loggedUser, Long destinationId, DestinationUpdateComparisonResult comparisonResult);

    String buildDestinationTerminateSuccessfullSubject(User loggeduser, DestinationResponseDto destinationDetailsById);

    String buildDestinationTerminateSuccessfullBody(User loggeduser, DestinationResponseDto destinationDetailsById);
}
