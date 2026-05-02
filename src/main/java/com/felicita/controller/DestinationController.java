package com.felicita.controller;

import com.felicita.model.dto.*;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.service.DestinationService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/destination")
public class DestinationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationController.class);

    private final DestinationService destinationService;

    @Autowired
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<CommonResponse<List<DestinationResponseDto>>> getAllDestinations() {
        LOGGER.info("{} Start execute get all destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationResponseDto>> response = destinationService.getAllDestinations();
        LOGGER.info("{} End execute get all destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/active-destinations")
    public ResponseEntity<CommonResponse<List<DestinationResponseDto>>> getActiveDestinations() {
        LOGGER.info("{} Start execute get all active destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationResponseDto>> response = destinationService.getActiveDestinations();
        LOGGER.info("{} End execute get all active destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @PostMapping(path = "/destinations")
    public ResponseEntity<CommonResponse<DestinationsWithParamsResponse>> getDestinationWithParams(@RequestBody DestinationDataRequest destinationDataRequest) {
        LOGGER.info("{} Start execute get all active destinations with params {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<DestinationsWithParamsResponse> response = destinationService.getDestinationWithParams(destinationDataRequest);
        LOGGER.info("{} End execute get all active destinations with params {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/all-categories")
    public ResponseEntity<CommonResponse<List<DestinationCategoryResponseDto>>> getAllDestinationsCategories() {
        LOGGER.info("{} Start execute get all destinations categories {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationCategoryResponseDto>> response = destinationService.getAllDestinationsCategories();
        LOGGER.info("{} End execute get all destinations categories {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/active-categories")
    public ResponseEntity<CommonResponse<List<DestinationCategoryResponseDto>>> getActiveDestinationsCategories() {
        LOGGER.info("{} Start execute get active destinations categories{}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationCategoryResponseDto>> response = destinationService.getActiveDestinationsCategories();
        LOGGER.info("{} End execute get active destinations categories{}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/popular-destinations")
    public ResponseEntity<CommonResponse<List<PopularDestinationResponseDto>>> getPopularDestinations() {
        LOGGER.info("{} Start execute get popular destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PopularDestinationResponseDto>> response = destinationService.getPopularDestinations();
        LOGGER.info("{} End execute get popular destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/new-destinations")
    public ResponseEntity<CommonResponse<List<PopularDestinationResponseDto>>> getNewDestinations() {
        LOGGER.info("{} Start execute get new destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PopularDestinationResponseDto>> response = destinationService.getNewDestinations();
        LOGGER.info("{} End execute get new destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/trending-destinations")
    public ResponseEntity<CommonResponse<List<TrendingDestinationResponseDto>>> getTrendingDestinations() {
        LOGGER.info("{} Start execute get trending destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TrendingDestinationResponseDto>> response = destinationService.getTrendingDestinations();
        LOGGER.info("{} End execute get trending destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/trending-destinations-add")
    public ResponseEntity<CommonResponse<InsertResponse>> addTrendingDestinations(@RequestBody TrendingDestinationInsertRequest trendingDestinationInsertRequest) {
        LOGGER.info("{} Start execute add trending destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = destinationService.addTrendingDestinations(trendingDestinationInsertRequest);
        LOGGER.info("{} End execute add trending destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/trending-destinations-terminate")
    public ResponseEntity<CommonResponse<TerminateResponse>> termianteTrendingDestination(@RequestBody TrendingDestinationTerminateRequest trendingDestinationTerminateRequest) {
        LOGGER.info("{} Start execute terminate trending destinations {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = destinationService.termianteTrendingDestination(trendingDestinationTerminateRequest);
        LOGGER.info("{} End execute terminate trending destinations {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/tour-map")
    public ResponseEntity<CommonResponse<List<DestinationsForTourMapDto>>> getDestinationsForTourMap() {
        LOGGER.info("{} Start execute get destinations for tour map {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationsForTourMapDto>> response = destinationService.getDestinationsForTourMap();
        LOGGER.info("{} End execute get destinations for tour map {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping(path = "/tour-id/{tourId}")
    public ResponseEntity<CommonResponse<List<DestinationResponseDto>>> getDestinationDetailsByTourId(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get destinations details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationResponseDto>> response = destinationService.getDestinationDetailsByTourId(tourId);
        LOGGER.info("{} End execute get destinations details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{destinationId}")
    public ResponseEntity<CommonResponse<DestinationResponseDto>> getDestinationDetailsById(@PathVariable Long destinationId) {
        LOGGER.info("{} Start execute get destination details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<DestinationResponseDto> response = destinationService.getDestinationDetailsById(destinationId);
        LOGGER.info("{} End execute get destination details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews")
    public ResponseEntity<CommonResponse<List<DestinationReviewDetailsResponse>>> getAllDestinationsReviewDetails() {
        LOGGER.info("{} Start execute get all destination review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationReviewDetailsResponse>> response = destinationService.getAllDestinationsReviewDetails();
        LOGGER.info("{} End execute get all destination review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews/{destinationId}")
    public ResponseEntity<CommonResponse<List<DestinationReviewDetailsResponse>>> getDestinationReviewDetailsById(@PathVariable Long destinationId) {
        LOGGER.info("{} Start execute get all destination review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationReviewDetailsResponse>> response = destinationService.getDestinationReviewDetailsById(destinationId);
        LOGGER.info("{} End execute get all destination review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<CommonResponse<List<DestinationHistoryDetailsResponse>>> getAllDestinationHistoryDetails() {
        LOGGER.info("{} Start execute get all destinations review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationHistoryDetailsResponse>> response = destinationService.getAllDestinationHistoryDetails();
        LOGGER.info("{} End execute get all destinations review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history/{destinationId}")
    public ResponseEntity<CommonResponse<List<DestinationHistoryDetailsResponse>>> getDestinationHistoryDetailsById(@PathVariable Long destinationId) {
        LOGGER.info("{} Start execute get all destinations review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationHistoryDetailsResponse>> response = destinationService.getDestinationHistoryDetailsById(destinationId);
        LOGGER.info("{} End execute get all destinations review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images")
    public ResponseEntity<CommonResponse<List<DestinationHistoryImageResponse>>> getAllDestinationHistoryImages() {
        LOGGER.info("{} Start execute get all destination review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationHistoryImageResponse>> response = destinationService.getAllDestinationHistoryImages();
        LOGGER.info("{} End execute get all destination review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images/{destinationId}")
    public ResponseEntity<CommonResponse<List<DestinationHistoryImageResponse>>> getDestinationHistoryImagesById(@PathVariable Long destinationId) {
        LOGGER.info("{} Start execute get all destination review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationHistoryImageResponse>> response = destinationService.getDestinationHistoryImagesById(destinationId);
        LOGGER.info("{} End execute get all destination review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/destination-for-terminate")
    public ResponseEntity<CommonResponse<List<DestinationForTerminateResponse>>> getDestinationsForTerminate() {
        LOGGER.info("{} Start execute get all active destination for terminate {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationForTerminateResponse>> response = destinationService.getDestinationsForTerminate();
        LOGGER.info("{} End execute get all active destination for terminate {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-destination")
    public ResponseEntity<CommonResponse<InsertResponse>> insertDestination(@RequestBody DestinationInsertRequest destinationInsertRequest) {
        LOGGER.info("{} Start execute insert destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = destinationService.insertDestination(destinationInsertRequest);
        LOGGER.info("{} End execute insert destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-destination")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateDestination(@RequestBody DestinationUpdateRequest destinationUpdateRequest) {
        LOGGER.info("{} Start execute update destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = destinationService.updateDestination(destinationUpdateRequest);
        LOGGER.info("{} End execute update destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-destination")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateDestination(@RequestBody DestinationTerminateRequest destinationTerminateRequest) {
        LOGGER.info("{} Start execute terminate destination {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = destinationService.terminateDestination(destinationTerminateRequest);
        LOGGER.info("{} End execute terminate destination {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/destination-names")
    public ResponseEntity<CommonResponse<List<DestinationForTerminateResponse>>> getDestinationsNames() {
        LOGGER.info("{} Start execute get all active destination names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationForTerminateResponse>> response = destinationService.getDestinationsForTerminate();
        LOGGER.info("{} End execute get all active destination names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/destination-statistics")
    public ResponseEntity<CommonResponse<DestinationStatisticsResponse>> getDestinationsStatistics() {
        LOGGER.info("{} Start execute get destination statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<DestinationStatisticsResponse> response = destinationService.getDestinationsStatistics();
        LOGGER.info("{} End execute get destination statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/destination-categories-statistics")
    public ResponseEntity<CommonResponse<DestinationCategoriesStatisticsResponse>> getDestinationCategoriesStatistics() {
        LOGGER.info("{} Start execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<DestinationCategoriesStatisticsResponse> response = destinationService.getDestinationCategoriesStatistics();
        LOGGER.info("{} End execute get destination categories statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/category-details-by-id")
    public ResponseEntity<CommonResponse<DestinationCategoryDetailsResponseDto>> getDestinationsCategoryDetailsById(@RequestBody DestinationCategoryDetailsRequest destinationCategoryDetailsRequest) {
        LOGGER.info("{} Start execute get active destinations categories{}", Constant.DOTS, Constant.DOTS);
        CommonResponse<DestinationCategoryDetailsResponseDto> response = destinationService.getDestinationsCategoryDetailsById(destinationCategoryDetailsRequest);
        LOGGER.info("{} End execute get active destinations categories{}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/add-destination-category")
    public ResponseEntity<CommonResponse<InsertResponse>> insertDestinationCategory(@RequestBody DestinationCategoryInsertRequest destinationCategoryInsertRequest) {
        LOGGER.info("{} Start execute insert destination category {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = destinationService.insertDestinationCategory(destinationCategoryInsertRequest);
        LOGGER.info("{} End execute insert destination category {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-destination-category")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateDestinationCategory(@RequestBody DestinationCategoryUpdateRequest destinationCategoryUpdateRequest) {
        LOGGER.info("{} Start execute update destination category {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = destinationService.updateDestinationCategory(destinationCategoryUpdateRequest);
        LOGGER.info("{} End execute update destination category {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-destination-category")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateDestinationCategory(@RequestBody DestinationCategoryTerminateRequest destinationCategoryTerminateRequest) {
        LOGGER.info("{} Start execute terminate destination category {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = destinationService.terminateDestinationCategory(destinationCategoryTerminateRequest);
        LOGGER.info("{} End execute terminate destination category {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
