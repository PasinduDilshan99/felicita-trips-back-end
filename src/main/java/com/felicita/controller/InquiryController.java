package com.felicita.controller;

import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.InsertResponse;
import com.felicita.service.InquiryService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v0/inquiry")
public class InquiryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryController.class);

    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CommonResponse<InsertResponse>> createInquiry(@RequestBody CreateInquiryRequest createInquiryRequest) {
        LOGGER.info("{} Start execute create inquiry {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = inquiryService.createInquiry(createInquiryRequest);
        LOGGER.info("{} End execute create inquiry {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/chat-bot-request")
    public ResponseEntity<CommonResponse<InsertResponse>> chatBotRequest(@RequestBody ChatBotRequest chatBotRequest) {
        LOGGER.info("{} Start execute create chat bot inquiry {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = inquiryService.chatBotRequest(chatBotRequest);
        LOGGER.info("{} End execute create chat bot inquiry {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
