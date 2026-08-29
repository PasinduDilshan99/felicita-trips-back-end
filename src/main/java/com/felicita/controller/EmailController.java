package com.felicita.controller;

import com.felicita.model.request.email.HotelRatesRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.ContactMethodResponse;
import com.felicita.model.response.InsertResponse;
import com.felicita.service.EmailService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/email")
public class EmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping(path = "/hotel-rates-email")
    public ResponseEntity<Void> getContactMethods() {
        LOGGER.info("{} Start execute get active contact methods {}", Constant.DOTS, Constant.DOTS);
        try {
            emailService.sendFromAdmin("pd.dimbulana@gmail.com", "Test Email", "Test Body");
            LOGGER.info("Email sent successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to send email: {}", e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("{} End execute get active contact methods {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(path = "/request-hotel-rates")
    public ResponseEntity<CommonResponse<InsertResponse>> requestHotelRates(@RequestBody HotelRatesRequest hotelRatesRequest) {
        LOGGER.info("{} Start execute get active contact methods {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = emailService.requestHotelRates(hotelRatesRequest);
        LOGGER.info("{} End execute get active contact methods {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
