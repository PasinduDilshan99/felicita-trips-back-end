package com.felicita.service;

import com.felicita.model.request.ChatBotRequest;
import com.felicita.model.request.CreateInquiryRequest;
import com.felicita.model.response.CommonResponse;
import com.felicita.model.response.InsertResponse;

public interface InquiryService {

    CommonResponse<InsertResponse> createInquiry(CreateInquiryRequest createInquiryRequest);

    CommonResponse<InsertResponse> chatBotRequest(ChatBotRequest chatBotRequest);
}
