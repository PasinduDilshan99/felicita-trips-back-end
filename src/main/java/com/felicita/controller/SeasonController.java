package com.felicita.controller;

import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.SeasonIdAndNameResponse;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.statistics.SeasonStatisticsResponse;
import com.felicita.service.SeasonService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/seasons")
public class SeasonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonController.class);

    private final SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping(path = "/{seasonId}")
    public ResponseEntity<CommonResponse<List<SeasonDetailsResponse>>> getSeasonDetailsBySeasonId(@PathVariable String seasonId) {
        LOGGER.info("{} Start execute get season details by season id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<SeasonDetailsResponse>> response = seasonService.getSeasonDetailsBySeasonId(seasonId);
        LOGGER.info("{} End execute get season details by season id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/basic-details")
    public ResponseEntity<CommonResponse<List<SeasonBasicResponse>>> getActiveSeasonDetails() {
        LOGGER.info("{} Start execute get active season details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<SeasonBasicResponse>> response = seasonService.getActiveSeasonDetails();
        LOGGER.info("{} End execute get active season details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/season-statistics")
    public ResponseEntity<CommonResponse<SeasonStatisticsResponse>> getSeasonsStatistics() {
        LOGGER.info("{} Start execute get season statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<SeasonStatisticsResponse> response = seasonService.getSeasonsStatistics();
        LOGGER.info("{} End execute get season statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/details-by-id")
    public ResponseEntity<CommonResponse<SeasonAllDetailsResponse>> getSeasonAllDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate season {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<SeasonAllDetailsResponse> response = seasonService.getSeasonAllDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate season {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-season")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateSeason(@RequestBody CommonIdRequest seasonTerminateIdRequest) {
        LOGGER.info("{} Start execute terminate season {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = seasonService.terminateSeason(seasonTerminateIdRequest);
        LOGGER.info("{} End execute terminate season {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-season")
    public ResponseEntity<CommonResponse<InsertResponse>> insertSeasons(@RequestBody SeasonInsertRequest seasonInsertRequest) {
        LOGGER.info("{} Start execute insert season {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = seasonService.insertSeasons(seasonInsertRequest);
        LOGGER.info("{} End execute insert season {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-season")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateSeasons(@RequestBody SeasonUpdateRequest seasonUpdateRequest) {
        LOGGER.info("{} Start execute update season {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = seasonService.updateSeasons(seasonUpdateRequest);
        LOGGER.info("{} End execute update season {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/seasonId-and-seasonName")
    public ResponseEntity<CommonResponse<List<SeasonIdAndNameResponse>>> getSeasonsIdsAndSeasonsNames() {
        LOGGER.info("{} Start execute get all active season ids and names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<SeasonIdAndNameResponse>> response = seasonService.getSeasonsIdsAndSeasonsNames();
        LOGGER.info("{} End execute get all active season ids and names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
