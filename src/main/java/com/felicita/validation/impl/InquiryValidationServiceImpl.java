package com.felicita.validation.impl;

import com.felicita.exception.ValidationFailedErrorExceptionHandler;
import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.response.ValidationFailedResponse;
import com.felicita.model.response.ValidationResultResponse;
import com.felicita.validation.CommonValidationService;
import com.felicita.validation.InquiryValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InquiryValidationServiceImpl implements InquiryValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryValidationServiceImpl.class);

    private final CommonValidationService commonValidationService;

    @Autowired
    public InquiryValidationServiceImpl(CommonValidationService commonValidationService) {
        this.commonValidationService = commonValidationService;
    }

    @Override
    public void validateCreateInquiryRequest(CreateInquiryRequest createInquiryRequest) {
        List<ValidationFailedResponse> validationFailedResponses = new ArrayList<>();
        ValidationResultResponse name = commonValidationService.validateNotNullOrEmpty("name", createInquiryRequest.getName());
        if (!name.isValid()) {
            validationFailedResponses.add(ValidationFailedResponse.builder().field(name.getField()).value(name.getMessage()).build());
        }

        if (createInquiryRequest.getEmail() != null){
            ValidationResultResponse email = commonValidationService.validateEmailFormat("email", createInquiryRequest.getEmail());
            if (!email.isValid()) {
                validationFailedResponses.add(ValidationFailedResponse.builder().field(email.getField()).value(email.getMessage()).build());
            }
        }

        if (createInquiryRequest.getPhoneNumber() != null){
            ValidationResultResponse phoneNumber = commonValidationService.validateOnlyNumbers("phoneNumber", createInquiryRequest.getPhoneNumber());
            if (!phoneNumber.isValid()) {
                validationFailedResponses.add(ValidationFailedResponse.builder().field(phoneNumber.getField()).value(phoneNumber.getMessage()).build());
            }
        }

        if (createInquiryRequest.getAdults() != null){
            ValidationResultResponse adults = commonValidationService.validateOnlyNumbers("adults", createInquiryRequest.getAdults().toString());
            if (!adults.isValid()) {
                validationFailedResponses.add(ValidationFailedResponse.builder().field(adults.getField()).value(adults.getMessage()).build());
            }
        }

        if (createInquiryRequest.getKids() != null){
            ValidationResultResponse kids = commonValidationService.validateOnlyNumbers("kids", createInquiryRequest.getKids().toString());
            if (!kids.isValid()) {
                validationFailedResponses.add(ValidationFailedResponse.builder().field(kids.getField()).value(kids.getMessage()).build());
            }
        }

        if (!validationFailedResponses.isEmpty()) {
            throw new ValidationFailedErrorExceptionHandler("Validation failed : createInquiryRequest", validationFailedResponses);
        }
    }

    @Override
    public void validateChatBotRequest(ChatBotRequest chatBotRequest) {

    }
}
