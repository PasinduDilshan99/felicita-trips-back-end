package com.felicita.controller;

import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.service.ActivitiesService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/activity")
public class ActivitiesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesController.class);

    private final ActivitiesService activitiesService;

    @Autowired
    public ActivitiesController(ActivitiesService activitiesService) {
        this.activitiesService = activitiesService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<CommonResponse<List<ActivityResponseDto>>> getAllActivities() {
        LOGGER.info("{} Start execute get all activities {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityResponseDto>> response = activitiesService.getAllActivities();
        LOGGER.info("{} End execute get all activities {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/activities")
    public ResponseEntity<CommonResponse<ActivityWithParamsResponse>> getActivitiesWithParams(@RequestBody ActivityDataRequest activityDataRequest) {
        LOGGER.info("{} Start execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<ActivityWithParamsResponse> response = activitiesService.getActivitiesWithParams(activityDataRequest);
        LOGGER.info("{} End execute get active activities for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<CommonResponse<List<ActivityResponseDto>>> getActiveActivities() {
        LOGGER.info("{} Start execute get active activities {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityResponseDto>> response = activitiesService.getActiveActivities();
        LOGGER.info("{} End execute get active activities {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/category")
    public ResponseEntity<CommonResponse<List<ActivityCategoryResponseDto>>> getAllActivityCategories() {
        LOGGER.info("{} Start execute get all activity categories {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityCategoryResponseDto>> response = activitiesService.getAllActivityCategories();
        LOGGER.info("{} End execute get all activity categories {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active-category")
    public ResponseEntity<CommonResponse<List<ActivityCategoryResponseDto>>> getActiveActivityCategories() {
        LOGGER.info("{} Start execute get active activity categories {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityCategoryResponseDto>> response = activitiesService.getActiveActivityCategories();
        LOGGER.info("{} End execute get active activity categories {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{activityId}")
    public ResponseEntity<CommonResponse<ActivityResponseDto>> getActivityById(@PathVariable Long activityId) {
        LOGGER.info("{} Start execute get activity by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<ActivityResponseDto> response = activitiesService.getActivityById(activityId);
        LOGGER.info("{} End execute get activity by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews")
    public ResponseEntity<CommonResponse<List<ActivityReviewDetailsResponse>>> getAllActivityReviewDetails() {
        LOGGER.info("{} Start execute get all activity review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityReviewDetailsResponse>> response = activitiesService.getAllActivityReviewDetails();
        LOGGER.info("{} End execute get all activity review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews/{activityId}")
    public ResponseEntity<CommonResponse<List<ActivityReviewDetailsResponse>>> getActivityReviewDetailsById(@PathVariable Long activityId) {
        LOGGER.info("{} Start execute get all activity review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityReviewDetailsResponse>> response = activitiesService.getActivityReviewDetailsById(activityId);
        LOGGER.info("{} End execute get all activity review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<CommonResponse<List<ActivityHistoryDetailsResponse>>> getAllActivityHistoryDetails() {
        LOGGER.info("{} Start execute get all activities history details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityHistoryDetailsResponse>> response = activitiesService.getAllActivityHistoryDetails();
        LOGGER.info("{} End execute get all activities history details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history/{activityId}")
    public ResponseEntity<CommonResponse<List<ActivityHistoryDetailsResponse>>> getActivityHistoryDetailsById(@PathVariable Long activityId) {
        LOGGER.info("{} Start execute get activities history details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityHistoryDetailsResponse>> response = activitiesService.getActivityHistoryDetailsById(activityId);
        LOGGER.info("{} End execute get history details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images")
    public ResponseEntity<CommonResponse<List<ActivityHistoryImageResponse>>> getAllActivityHistoryImages() {
        LOGGER.info("{} Start execute get all activities history images details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityHistoryImageResponse>> response = activitiesService.getAllActivityHistoryImages();
        LOGGER.info("{} End execute get all activities history images details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images/{activityId}")
    public ResponseEntity<CommonResponse<List<ActivityHistoryImageResponse>>> getActivityHistoryImagesById(@PathVariable Long activityId) {
        LOGGER.info("{} Start execute get activities history images details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityHistoryImageResponse>> response = activitiesService.getActivityHistoryImagesById(activityId);
        LOGGER.info("{} End execute get activities history images details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/activity-for-terminate")
    public ResponseEntity<CommonResponse<List<ActivityForTerminateResponse>>> getActivitiesForTerminate() {
        LOGGER.info("{} Start execute get all active activities for terminate {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityForTerminateResponse>> response = activitiesService.getActivitiesForTerminate();
        LOGGER.info("{} End execute get all active activities for terminate {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-activity")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateActivity(@RequestBody ActivityTerminateRequest activityTerminateRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = activitiesService.terminateActivity(activityTerminateRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-activity")
    public ResponseEntity<CommonResponse<InsertResponse>> insertActivity(@RequestBody ActivityInsertRequest activityInsertRequest) {
        LOGGER.info("{} Start execute insert activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = activitiesService.insertActivity(activityInsertRequest);
        LOGGER.info("{} End execute insert activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-activity")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateActivity(@RequestBody ActivityUpdateRequest activityUpdateRequest) {
        LOGGER.info("{} Start execute update activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = activitiesService.updateActivity(activityUpdateRequest);
        LOGGER.info("{} End execute update activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/activityId-and-activityName")
    public ResponseEntity<CommonResponse<List<ActivityIdAndNameResponse>>> getTourIdsAndTourNames() {
        LOGGER.info("{} Start execute get all active activity ids and names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityIdAndNameResponse>> response = activitiesService.getTourIdsAndTourNames();
        LOGGER.info("{} End execute get all active activity ids and names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/activities-statistics")
    public ResponseEntity<CommonResponse<ActivityStatisticsResponse>> getActivitiesStatistics() {
        LOGGER.info("{} Start execute get activities statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<ActivityStatisticsResponse> response = activitiesService.getActivitiesStatistics();
        LOGGER.info("{} End execute get activities statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
