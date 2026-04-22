package com.felicita.service.impl;

import com.felicita.exception.InsertFailedErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.exception.ValidationFailedErrorExceptionHandler;
import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.InsertResponse;
import com.felicita.repository.InquiryRepository;
import com.felicita.service.CommonService;
import com.felicita.service.EmailHelperService;
import com.felicita.service.EmailService;
import com.felicita.service.InquiryService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.InquiryValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class InquiryServiceImpl implements InquiryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryServiceImpl.class);

    private final InquiryRepository inquiryRepository;
    private final InquiryValidationService inquiryValidationService;
    private final CommonService commonService;
    private final EmailService emailService;
    private final EmailHelperService emailHelperService;

    @Autowired
    public InquiryServiceImpl(InquiryRepository inquiryRepository,
                              InquiryValidationService inquiryValidationService,
                              CommonService commonService,
                              EmailService emailService,
                              EmailHelperService emailHelperService) {
        this.inquiryRepository = inquiryRepository;
        this.inquiryValidationService = inquiryValidationService;
        this.commonService = commonService;
        this.emailService = emailService;
        this.emailHelperService = emailHelperService;
    }

    @Override
    public CommonResponse<InsertResponse> createInquiry(CreateInquiryRequest createInquiryRequest) {
        try {
            LOGGER.info("Start execute insert inquiry request");
            inquiryValidationService.validateCreateInquiryRequest(createInquiryRequest);
            Long userId = commonService.getUserIdBySecurityContextWithOutException();
            inquiryRepository.createInquiry(createInquiryRequest, userId);

            String subject = emailHelperService.buildInquirySubject(createInquiryRequest);
            String body = emailHelperService.buildInquiryEmailBody(createInquiryRequest);

            emailService.sendFromDev(
                    "felicitatrips@gmail.com",
                    subject,
                    body
            );

            if (createInquiryRequest.getEmail() != null && !createInquiryRequest.getEmail().isEmpty()) {
                String userSubject = emailHelperService.buildCustomerInquirySubject(createInquiryRequest);
                String userBody = emailHelperService.buildCustomerInquiryEmailBody(createInquiryRequest);
                emailService.sendFromMain(
                        createInquiryRequest.getEmail(),
                        userSubject,
                        userBody
                );
            }
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully create a inquiry"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the create inquiry request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<InsertResponse> chatBotRequest(ChatBotRequest chatBotRequest) {
        try {
            LOGGER.info("Start execute insert chat bot inquiry request");
            inquiryValidationService.validateChatBotRequest(chatBotRequest);
            Long userId = commonService.getUserIdBySecurityContextWithOutException();
            CreateInquiryRequest createInquiryRequest = new CreateInquiryRequest();
            createInquiryRequest.setName(chatBotRequest.getName());
            createInquiryRequest.setEmail(chatBotRequest.getEmail());
            createInquiryRequest.setPhoneNumber(chatBotRequest.getPhone());
            String convertedMessage = convertPreferencesToMessage(chatBotRequest.getPreferences());
            createInquiryRequest.setMessage(convertedMessage);
            inquiryRepository.createInquiry(createInquiryRequest, userId);

            String subject = emailHelperService.buildChatBotInquirySubject(chatBotRequest);
            String body = emailHelperService.buildChatBotInquiryEmailBody(chatBotRequest);

            emailService.sendFromDev(
                    "felicitatrips@gmail.com",
                    subject,
                    body
            );

            if (createInquiryRequest.getEmail() != null && !createInquiryRequest.getEmail().isEmpty()) {
                String customerSubject = "We received your inquiry - Felicita Trips";
                String customerBody = emailHelperService.buildCustomerConfirmationEmailBody(chatBotRequest);
                emailService.sendFromMain(
                        chatBotRequest.getEmail(),
                        customerSubject,
                        customerBody
                );
            }
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully create a inquiry"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the create inquiry request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }


    private String convertPreferencesToMessage(ChatBotRequest.Preferences preferences) {
        StringBuilder message = new StringBuilder();
        message.append("📋 *New Travel Inquiry Received*\n\n");

        Map<String, List<String>> selections = preferences.getSelections();
        String flowType = preferences.getFlowType();

        message.append("🏷️ *Type:* ").append(getFlowTypeDisplayName(flowType)).append("\n\n");

        if ("tours".equals(flowType)) {
            message.append("✈️ *TOUR PREFERENCES*\n");
            message.append("━━━━━━━━━━━━━━━━━━━━━\n\n");

            if (selections.containsKey("category") && !selections.get("category").isEmpty()) {
                message.append("📌 *Categories:*\n");
                for (String category : selections.get("category")) {
                    message.append("   • ").append(category).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("duration") && !selections.get("duration").isEmpty()) {
                message.append("⏱️ *Durations:*\n");
                for (String duration : selections.get("duration")) {
                    message.append("   • ").append(duration).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("tourType") && !selections.get("tourType").isEmpty()) {
                message.append("🎯 *Tour Types:*\n");
                for (String tourType : selections.get("tourType")) {
                    message.append("   • ").append(tourType).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("season") && !selections.get("season").isEmpty()) {
                message.append("🌤️ *Seasons:*\n");
                for (String season : selections.get("season")) {
                    message.append("   • ").append(season).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("budget") && !selections.get("budget").isEmpty()) {
                message.append("💰 *Budgets:*\n");
                for (String budget : selections.get("budget")) {
                    message.append("   • ").append(budget).append("\n");
                }
                message.append("\n");
            }

        } else if ("activities".equals(flowType)) {
            message.append("🎪 *ACTIVITIES PREFERENCES*\n");
            message.append("━━━━━━━━━━━━━━━━━━━━━\n\n");

            if (selections.containsKey("category") && !selections.get("category").isEmpty()) {
                message.append("📌 *Activity Types:*\n");
                for (String category : selections.get("category")) {
                    message.append("   • ").append(category).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("season") && !selections.get("season").isEmpty()) {
                message.append("🌤️ *Preferred Seasons:*\n");
                for (String season : selections.get("season")) {
                    message.append("   • ").append(season).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("duration") && !selections.get("duration").isEmpty()) {
                message.append("⏱️ *Durations:*\n");
                for (String duration : selections.get("duration")) {
                    message.append("   • ").append(duration).append("\n");
                }
                message.append("\n");
            }

        } else if ("destinations".equals(flowType)) {
            message.append("🏝️ *DESTINATIONS PREFERENCES*\n");
            message.append("━━━━━━━━━━━━━━━━━━━━━\n\n");

            if (selections.containsKey("category") && !selections.get("category").isEmpty()) {
                message.append("📌 *Destination Types:*\n");
                for (String category : selections.get("category")) {
                    message.append("   • ").append(category).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("location") && !selections.get("location").isEmpty()) {
                message.append("📍 *Preferred Locations:*\n");
                for (String location : selections.get("location")) {
                    message.append("   • ").append(location).append("\n");
                }
                message.append("\n");
            }

            if (selections.containsKey("rating") && !selections.get("rating").isEmpty()) {
                message.append("⭐ *Minimum Ratings:*\n");
                for (String rating : selections.get("rating")) {
                    message.append("   • ").append(rating).append("\n");
                }
                message.append("\n");
            }
        }

        message.append("━━━━━━━━━━━━━━━━━━━━━\n");
        message.append("📅 *Inquiry Date:* ").append(new java.util.Date()).append("\n");
        message.append("🆔 *Priority:* High\n");
        message.append("⏰ *Response Required:* Within 24 hours\n");

        return message.toString();
    }

    private String getFlowTypeDisplayName(String flowType) {
        if ("tours".equals(flowType)) {
            return "Tour Package Inquiry";
        } else if ("activities".equals(flowType)) {
            return "Activities & Experiences Inquiry";
        } else if ("destinations".equals(flowType)) {
            return "Destinations Inquiry";
        }
        return "General Inquiry";
    }

}
