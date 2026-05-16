package com.felicita.controller;

import com.felicita.model.dto.PopularTourResponseDto;
import com.felicita.model.dto.TourResponseDto;
import com.felicita.model.request.*;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.request.tour.schedule.TourScheduleDataRequest;
import com.felicita.model.request.tour.schedule.TourScheduleInsertRequest;
import com.felicita.model.request.tour.schedule.TourScheduleUpdateRequest;
import com.felicita.model.request.tour.type.TourTypeInsertRequest;
import com.felicita.model.request.tour.type.TourTypeUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.activity.category.ActivityCategoryDetailsResponse;
import com.felicita.model.response.statistics.TourCategoryStatisticsResponse;
import com.felicita.model.response.statistics.TourScheduleStatisticsResponse;
import com.felicita.model.response.statistics.TourStatisticsResponse;
import com.felicita.model.response.statistics.TourTypeStatisticsResponse;
import com.felicita.model.response.tour.category.TourCategoryAllDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryBasicDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleParamsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleWithParamsResponse;
import com.felicita.model.response.tour.type.TourTypeAllDetailsResponse;
import com.felicita.model.response.tour.type.TourTypeBasicDetailsResponse;
import com.felicita.service.TourService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/tour")
public class TourController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourController.class);

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<CommonResponse<List<TourResponseDto>>> getAllTours() {
        LOGGER.info("{} Start execute get all tours {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourResponseDto>> response = tourService.getAllTours();
        LOGGER.info("{} End execute get all tours {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<CommonResponse<List<TourResponseDto>>> getActiveTours() {
        LOGGER.info("{} Start execute get active tours {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourResponseDto>> response = tourService.getActiveTours();
        LOGGER.info("{} End execute get active tours {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/tours")
    public ResponseEntity<CommonResponse<ToursDetailsWithParamResponse>> getToursToShowWithParam(@RequestBody TourDataRequest tourDataRequest) {
        LOGGER.info("{} Start execute get active tours for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<ToursDetailsWithParamResponse> response = tourService.getToursToShowWithParam(tourDataRequest);
        LOGGER.info("{} End execute get active tours for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/popular")
    public ResponseEntity<CommonResponse<List<PopularTourResponseDto>>> getPopularTours() {
        LOGGER.info("{} Start execute get popular tours {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PopularTourResponseDto>> response = tourService.getPopularTours();
        LOGGER.info("{} End execute get popular tours {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/{tourId}")
    public ResponseEntity<CommonResponse<TourResponseDto>> getTourDetailsById(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourResponseDto> response = tourService.getTourDetailsById(tourId);
        LOGGER.info("{} End execute get tour details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tout-all-details/{tourId}")
    public ResponseEntity<CommonResponse<TourAllDetailsResponse>> getTourAllDetailsById(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour all details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourAllDetailsResponse> response = tourService.getTourAllDetailsById(tourId);
        LOGGER.info("{} End execute get tour all details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews")
    public ResponseEntity<CommonResponse<List<TourReviewDetailsResponse>>> getAllTourReviewDetails() {
        LOGGER.info("{} Start execute get all tour review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourReviewDetailsResponse>> response = tourService.getAllTourReviewDetails();
        LOGGER.info("{} End execute get all tour review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews/{tourId}")
    public ResponseEntity<CommonResponse<List<TourReviewDetailsResponse>>> getTourReviewDetailsById(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourReviewDetailsResponse>> response = tourService.getTourReviewDetailsById(tourId);
        LOGGER.info("{} End execute get tour review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<CommonResponse<List<TourHistoryResponse>>> getAllTourHistoryDetails() {
        LOGGER.info("{} Start execute get all tour history details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourHistoryResponse>> response = tourService.getAllTourHistoryDetails();
        LOGGER.info("{} End execute get all tour history details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history/{tourId}")
    public ResponseEntity<CommonResponse<List<TourHistoryResponse>>> getTourHistoryDetailsById(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour history details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourHistoryResponse>> response = tourService.getTourHistoryDetailsById(tourId);
        LOGGER.info("{} End execute get tour history details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-map/{tourId}")
    public ResponseEntity<CommonResponse<List<TourDestinationsForMapResponse>>> getTourDestinationsForMap(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour details for tour map by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourDestinationsForMapResponse>> response = tourService.getTourDestinationsForMap(tourId);
        LOGGER.info("{} End execute get tour details for tour map by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images")
    public ResponseEntity<CommonResponse<List<TourHistoryImageResponse>>> getAllTourHistoryImages() {
        LOGGER.info("{} Start execute get all tour history images details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourHistoryImageResponse>> response = tourService.getAllTourHistoryImages();
        LOGGER.info("{} End execute get all tour history images details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images/{tourId}")
    public ResponseEntity<CommonResponse<List<TourHistoryImageResponse>>> getTourHistoryImagesById(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour history images details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourHistoryImageResponse>> response = tourService.getTourHistoryImagesById(tourId);
        LOGGER.info("{} End execute get tour history images details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-details/{tourId}")
    public ResponseEntity<CommonResponse<List<TourDetailsWithDayToDayResponse>>> getTourDetailsDayByDay(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourDetailsWithDayToDayResponse>> response = tourService.getTourDetailsDayByDay(tourId);
        LOGGER.info("{} End execute get all tour details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-extra-details/{tourId}")
    public ResponseEntity<CommonResponse<TourExtrasResponse>> getTourExtraDetailsDayByDay(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour extra details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourExtrasResponse> response = tourService.getTourExtraDetailsDayByDay(tourId);
        LOGGER.info("{} End execute get all tour extra details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-schedules/{tourId}")
    public ResponseEntity<CommonResponse<TourSchedulesResponse>> getTourSchedules(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour schedules with given id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourSchedulesResponse> response = tourService.getTourSchedules(tourId);
        LOGGER.info("{} End execute get all tour schedules with given id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/all-tours-basic")
    public ResponseEntity<CommonResponse<List<TourBasicDetailsResponse>>> getAllToursBasicDetails() {
        LOGGER.info("{} Start execute get all tours basic details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourBasicDetailsResponse>> response = tourService.getAllToursBasicDetails();
        LOGGER.info("{} End execute get all tours basic details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-for-terminate")
    public ResponseEntity<CommonResponse<List<TourForTerminateResponse>>> getToursForTerminate() {
        LOGGER.info("{} Start execute get all active tour for terminate {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourForTerminateResponse>> response = tourService.getToursForTerminate();
        LOGGER.info("{} End execute get all active tour for terminate {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-tour")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateTour(@RequestBody TourTerminateRequest tourTerminateRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = tourService.terminateTour(tourTerminateRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-tour")
    public ResponseEntity<CommonResponse<InsertResponse>> insertTour(@RequestBody TourInsertRequest tourInsertRequest) {
        LOGGER.info("{} Start execute insert tour {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = tourService.insertTour(tourInsertRequest);
        LOGGER.info("{} End execute insert tour {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-tour")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateTour(@RequestBody TourUpdateRequest tourUpdateRequest) {
        LOGGER.info("{} Start execute update tour {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = tourService.updateTour(tourUpdateRequest);
        LOGGER.info("{} End execute update tour {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tourId-and-tourName")
    public ResponseEntity<CommonResponse<List<TourIdAndTourNameResponse>>> getTourIdsAndTourNames() {
        LOGGER.info("{} Start execute get all active tour ids and names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourIdAndTourNameResponse>> response = tourService.getTourIdsAndTourNames();
        LOGGER.info("{} End execute get all active tour ids and names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-details-for-add-package/{tourId}")
    public ResponseEntity<CommonResponse<TourDetailsForAddPackageResponse>> getTourDetailsForAddPackage(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get tour details for add package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourDetailsForAddPackageResponse> response = tourService.getTourDetailsForAddPackage(tourId);
        LOGGER.info("{} End execute get tour details for add package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-statistics")
    public ResponseEntity<CommonResponse<TourStatisticsResponse>> getTourStatistics() {
        LOGGER.info("{} Start execute get tour statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourStatisticsResponse> response = tourService.getTourStatistics();
        LOGGER.info("{} End execute get tour statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-schedule-statistics")
    public ResponseEntity<CommonResponse<TourScheduleStatisticsResponse>> getTourScheduleStatistics() {
        LOGGER.info("{} Start execute get tour schedule statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourScheduleStatisticsResponse> response = tourService.getTourScheduleStatistics();
        LOGGER.info("{} End execute get tour schedule statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-category-statistics")
    public ResponseEntity<CommonResponse<TourCategoryStatisticsResponse>> getTourCategoryStatistics() {
        LOGGER.info("{} Start execute get tour category statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourCategoryStatisticsResponse> response = tourService.getTourCategoryStatistics();
        LOGGER.info("{} End execute get tour category statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-type-statistics")
    public ResponseEntity<CommonResponse<TourTypeStatisticsResponse>> getTourTypeStatistics() {
        LOGGER.info("{} Start execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourTypeStatisticsResponse> response = tourService.getTourTypeStatistics();
        LOGGER.info("{} End execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-categories")
    public ResponseEntity<CommonResponse<List<TourCategoryBasicDetailsResponse>>> getTourCategories() {
        LOGGER.info("{} Start execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourCategoryBasicDetailsResponse>> response = tourService.getTourCategories();
        LOGGER.info("{} End execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-category-details")
    public ResponseEntity<CommonResponse<TourCategoryAllDetailsResponse>> getTourCategoryDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourCategoryAllDetailsResponse> response = tourService.getTourCategoryDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-category-basic-details")
    public ResponseEntity<CommonResponse<TourCategoryBasicDetailsResponse>> getTourCategoryBasicDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourCategoryBasicDetailsResponse> response = tourService.getTourCategoryBasicDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-tour-category")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateTourCategory(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = tourService.terminateTourCategory(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-tour-category")
    public ResponseEntity<CommonResponse<InsertResponse>> insertTourCategory(@RequestBody TourCategoryInsertRequest tourCategoryInsertRequest) {
        LOGGER.info("{} Start execute insert activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = tourService.insertTourCategory(tourCategoryInsertRequest);
        LOGGER.info("{} End execute insert activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-tour-category")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateTourCategory(@RequestBody TourCategoryUpdateRequest tourCategoryUpdateRequest) {
        LOGGER.info("{} Start execute update activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = tourService.updateTourCategory(tourCategoryUpdateRequest);
        LOGGER.info("{} End execute update activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-types")
    public ResponseEntity<CommonResponse<List<TourTypeBasicDetailsResponse>>> getTourTypes() {
        LOGGER.info("{} Start execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourTypeBasicDetailsResponse>> response = tourService.getTourTypes();
        LOGGER.info("{} End execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-type-details")
    public ResponseEntity<CommonResponse<TourTypeAllDetailsResponse>> getTourTypeDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourTypeAllDetailsResponse> response = tourService.getTourTypeDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-type-basic-details")
    public ResponseEntity<CommonResponse<TourTypeBasicDetailsResponse>> getTourTypeBasicDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourTypeBasicDetailsResponse> response = tourService.getTourTypeBasicDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-tour-type")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateTourType(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = tourService.terminateTourType(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-tour-type")
    public ResponseEntity<CommonResponse<InsertResponse>> insertTourType(@RequestBody TourTypeInsertRequest tourTypeInsertRequest) {
        LOGGER.info("{} Start execute insert activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = tourService.insertTourType(tourTypeInsertRequest);
        LOGGER.info("{} End execute insert activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-tour-type")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateTourType(@RequestBody TourTypeUpdateRequest tourTypeUpdateRequest) {
        LOGGER.info("{} Start execute update activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = tourService.updateTourType(tourTypeUpdateRequest);
        LOGGER.info("{} End execute update activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-schedule")
    public ResponseEntity<CommonResponse<TourScheduleWithParamsResponse>> getTourScheduleWithParams(@RequestBody TourScheduleDataRequest tourScheduleDataRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourScheduleWithParamsResponse> response = tourService.getTourScheduleWithParams(tourScheduleDataRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour-schedule-params")
    public ResponseEntity<CommonResponse<TourScheduleParamsResponse>> getToursScheduleParams() {
        LOGGER.info("{} Start execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourScheduleParamsResponse> response = tourService.getToursScheduleParams();
        LOGGER.info("{} End execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/tour-schedule-details-by-id")
    public ResponseEntity<CommonResponse<TourScheduleDetailsResponse>> getTourScheduleDetailsById(@RequestBody CommonIdRequest tourScheduleId) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TourScheduleDetailsResponse> response = tourService.getTourScheduleDetailsById(tourScheduleId);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-tour-schedule")
    public ResponseEntity<CommonResponse<InsertResponse>> createTourSchedule(@RequestBody TourScheduleInsertRequest tourScheduleInsertRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = tourService.createTourSchedule(tourScheduleInsertRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-tour-schedule")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateTourSchedule(@RequestBody TourScheduleUpdateRequest tourScheduleUpdateRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = tourService.updateTourSchedule(tourScheduleUpdateRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-activities-schedule")
    public ResponseEntity<CommonResponse<TerminateResponse>> termianteTourScheduleById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = tourService.termianteTourScheduleById(commonIdRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
