package com.felicita.controller;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsInsertRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsTerminateRequest;
import com.felicita.model.request.whyChooseUs.WhyChooseUsUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.IdAndNameResponse;
import com.felicita.model.response.statistics.WhyChooseUsStatisticsResponse;
import com.felicita.model.response.whyChooseUs.WhyChooseUsDetailsResponse;
import com.felicita.service.WhyChooseUsService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/why-choose-us")
public class WhyChooseUsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhyChooseUsController.class);

    private final WhyChooseUsService whyChooseUsService;

    @Autowired
    public WhyChooseUsController(WhyChooseUsService whyChooseUsService) {
        this.whyChooseUsService = whyChooseUsService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<CommonResponse<List<WhyChooseUsResponse>>> getAllWhyChooseUsData(){
        LOGGER.info("{} Start execute get all why choose us data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<WhyChooseUsResponse>> response = whyChooseUsService.getAllWhyChooseUsData();
        LOGGER.info("{} End execute get all why choose us data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<CommonResponse<List<WhyChooseUsResponse>>> getActiveWhyChooseUsData(){
        LOGGER.info("{} Start execute get active why choose us data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<WhyChooseUsResponse>> response = whyChooseUsService.getActiveWhyChooseUsData();
        LOGGER.info("{} End execute get active why choose us data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // admin
    @GetMapping(path = "/id-and-names")
    public ResponseEntity<CommonResponse<List<IdAndNameResponse>>> getWhyChooseUsDataIdsAndNames() {
        LOGGER.info("{} Start execute get all active destination names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<IdAndNameResponse>> response = whyChooseUsService.getWhyChooseUsDataIdsAndNames();
        LOGGER.info("{} End execute get all active destination names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/statistics")
    public ResponseEntity<CommonResponse<WhyChooseUsStatisticsResponse>> getWhyChooseUsStatistics() {
        LOGGER.info("{} Start execute get destination statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<WhyChooseUsStatisticsResponse> response = whyChooseUsService.getWhyChooseUsStatistics();
        LOGGER.info("{} End execute get destination statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/details")
    public ResponseEntity<CommonResponse<WhyChooseUsDetailsResponse>> getWhyChooseUsDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute insert destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<WhyChooseUsDetailsResponse> response = whyChooseUsService.getWhyChooseUsDetailsById(commonIdRequest);
        LOGGER.info("{} End execute insert destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-why-choose-us")
    public ResponseEntity<CommonResponse<InsertResponse>> insertWhyChooseUs(@RequestBody WhyChooseUsInsertRequest whyChooseUsInsertRequest) {
        LOGGER.info("{} Start execute insert destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = whyChooseUsService.insertWhyChooseUs(whyChooseUsInsertRequest);
        LOGGER.info("{} End execute insert destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-why-choose-us")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateWhyChooseUs(@RequestBody WhyChooseUsUpdateRequest whyChooseUsUpdateRequest) {
        LOGGER.info("{} Start execute update destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = whyChooseUsService.updateWhyChooseUs(whyChooseUsUpdateRequest);
        LOGGER.info("{} End execute update destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-why-choose-us")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateWhyChooseUs(@RequestBody WhyChooseUsTerminateRequest whyChooseUsTerminateRequest) {
        LOGGER.info("{} Start execute terminate destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = whyChooseUsService.terminateWhyChooseUs(whyChooseUsTerminateRequest);
        LOGGER.info("{} End execute terminate destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
