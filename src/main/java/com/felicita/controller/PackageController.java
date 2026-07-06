package com.felicita.controller;

import com.felicita.model.dto.PackageResponseDto;
import com.felicita.model.request.*;
import com.felicita.model.request.packages.schedule.PackageScheduleDataRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleInsertRequest;
import com.felicita.model.request.packages.schedule.PackageScheduleUpdateRequest;
import com.felicita.model.request.packages.type.PackageTypeInsertRequest;
import com.felicita.model.request.packages.type.PackageTypeUpdateRequest;
import com.felicita.model.request.tour.schedule.TourScheduleInsertRequest;
import com.felicita.model.request.tour.schedule.TourScheduleUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.PackageScheduleIdAndNameResponse;
import com.felicita.model.response.packages.ParamsForPackageRequestResponse;
import com.felicita.model.response.packages.schedule.PackageScheduleAllDetailsResponse;
import com.felicita.model.response.packages.schedule.PackageScheduleParamsResponse;
import com.felicita.model.response.packages.schedule.PackageScheduleWithParamsResponse;
import com.felicita.model.response.packages.type.PackageTypeAllDetailsResponse;
import com.felicita.model.response.packages.type.PackageTypeBasicDetailsResponse;
import com.felicita.model.response.statistics.PackageScheduleStatisticsResponse;
import com.felicita.model.response.statistics.PackageStatisticsResponse;
import com.felicita.model.response.statistics.PackageTypeStatisticsResponse;
import com.felicita.service.PackageService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/package")
public class PackageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageController.class);

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<CommonResponse<List<PackageResponseDto>>> getAllPackages() {
        LOGGER.info("{} Start execute get all package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageResponseDto>> response = packageService.getAllPackages();
        LOGGER.info("{} End execute get all package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<CommonResponse<List<PackageResponseDto>>> getActivePackages() {
        LOGGER.info("{} Start execute get active package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageResponseDto>> response = packageService.getActivePackages();
        LOGGER.info("{} End execute get active package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/packages")
    public ResponseEntity<CommonResponse<PackageWithParamsResponse>> getPackagesWithParams(@RequestBody PackageDataRequest packageDataRequest) {
        LOGGER.info("{} Start execute get active package for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageWithParamsResponse> response = packageService.getPackagesWithParams(packageDataRequest);
        LOGGER.info("{} End execute get active packages for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/params-for-tour-request")
    public ResponseEntity<CommonResponse<ParamsForPackageRequestResponse>> getParamsForPackageRequest() {
        LOGGER.info("{} Start execute get active package for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<ParamsForPackageRequestResponse> response = packageService.getParamsForPackageRequest();
        LOGGER.info("{} End execute get active packages for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{packageId}")
    public ResponseEntity<CommonResponse<PackageResponseDto>> getPackageDetailsById(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get package details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageResponseDto> response = packageService.getPackageDetailsById(packageId);
        LOGGER.info("{} End execute get package details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews")
    public ResponseEntity<CommonResponse<List<PackageReviewResponse>>> getAllPackageReviewDetails() {
        LOGGER.info("{} Start execute get all package review details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageReviewResponse>> response = packageService.getAllPackageReviewDetails();
        LOGGER.info("{} End execute get all package review details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/reviews/{packageId}")
    public ResponseEntity<CommonResponse<List<PackageReviewResponse>>> getPackageReviewDetailsById(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get package review details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageReviewResponse>> response = packageService.getPackageReviewDetailsById(packageId);
        LOGGER.info("{} End execute get package review details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<CommonResponse<List<PackageHistoryDetailsResponse>>> getAllPackageHistoryDetails() {
        LOGGER.info("{} Start execute get all packages history details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageHistoryDetailsResponse>> response = packageService.getAllPackageHistoryDetails();
        LOGGER.info("{} End execute get all packages history details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history/{packageId}")
    public ResponseEntity<CommonResponse<List<PackageHistoryDetailsResponse>>> getPackageHistoryDetailsById(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get packages history details by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageHistoryDetailsResponse>> response = packageService.getPackageHistoryDetailsById(packageId);
        LOGGER.info("{} End execute get packages history details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images")
    public ResponseEntity<CommonResponse<List<PackageHistoryImageResponse>>> getAllPackageHistoryImages() {
        LOGGER.info("{} Start execute get all packages history images details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageHistoryImageResponse>> response = packageService.getAllPackageHistoryImages();
        LOGGER.info("{} End execute get all packages history images details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/history-images/{packageId}")
    public ResponseEntity<CommonResponse<List<PackageHistoryImageResponse>>> getPackageHistoryImagesById(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get packages review history images by id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageHistoryImageResponse>> response = packageService.getPackageHistoryImagesById(packageId);
        LOGGER.info("{} End execute get packages review history images by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-details/{tourId}")
    public ResponseEntity<CommonResponse<List<PackageDayAccommodationResponse>>> getDayToPackageDetailsByTourId(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get day to day package details by tour Id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageDayAccommodationResponse>> response = packageService.getDayToPackageDetailsByTourId(tourId);
        LOGGER.info("{} End execute get day to day package details by tour Id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-compare/{tourId}")
    public ResponseEntity<CommonResponse<List<PackageComapreResponse>>> getDayToPackageDetailsForComapreByTourId(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get day to day package details to compare by tour Id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageComapreResponse>> response = packageService.getDayToPackageDetailsForComapreByTourId(tourId);
        LOGGER.info("{} End execute get day to day package details to compare by tour Id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-extra-details/{tourId}")
    public ResponseEntity<CommonResponse<List<PackageExtrasResponse>>> getPackageExtraDetailsDayByDay(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get package extra details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageExtrasResponse>> response = packageService.getPackageExtraDetailsDayByDay(tourId);
        LOGGER.info("{} End execute get all package extra details day by day with given id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedules/{tourId}")
    public ResponseEntity<CommonResponse<List<PackageScheduleResponse>>> getPackageSchedulesByTourId(@PathVariable Long tourId) {
        LOGGER.info("{} Start execute get package schedules with given tour id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageScheduleResponse>> response = packageService.getPackageSchedulesByTourId(tourId);
        LOGGER.info("{} End execute get all package schedules with given tour id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedules-details/{packageId}")
    public ResponseEntity<CommonResponse<PackageScheduleDetailsResponse>> getPackageSchedulesForId(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get package schedules with given id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageScheduleDetailsResponse> response = packageService.getPackageSchedulesForId(packageId);
        LOGGER.info("{} End execute get package schedules with given id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-for-terminate")
    public ResponseEntity<CommonResponse<List<PackageForTerminateResponse>>> getPackagesForTerminate() {
        LOGGER.info("{} Start execute get all package for terminate {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageForTerminateResponse>> response = packageService.getPackagesForTerminate();
        LOGGER.info("{} End execute get all package for terminate {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-package")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminatePackage(@RequestBody PackageTerminateRequest packageTerminateRequest) {
        LOGGER.info("{} Start execute terminate package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = packageService.terminatePackage(packageTerminateRequest);
        LOGGER.info("{} End execute terminate package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-package")
    public ResponseEntity<CommonResponse<InsertResponse>> insertPackage(@RequestBody PackageInsertRequest packageInsertRequest) {
        LOGGER.info("{} Start execute insert package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = packageService.insertPackage(packageInsertRequest);
        LOGGER.info("{} End execute insert package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-package-params")
    public ResponseEntity<CommonResponse<AddPackageParamResponse>> getInsertPackageParams(@RequestBody AddPackageParamRequest addPackageParamRequest) {
        LOGGER.info("{} Start execute get insert package params {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<AddPackageParamResponse> response = packageService.getInsertPackageParams(addPackageParamRequest);
        LOGGER.info("{} End execute get insert package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-package")
    public ResponseEntity<CommonResponse<UpdateResponse>> updatePackage(@RequestBody PackageUpdateRequest packageUpdateRequest) {
        LOGGER.info("{} Start execute update package {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = packageService.updatePackage(packageUpdateRequest);
        LOGGER.info("{} End execute update package {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-all-details/{packageId}")
    public ResponseEntity<CommonResponse<PackageAllDetailsResponse>> getPackageAllDetailsById(@PathVariable Long packageId) {
        LOGGER.info("{} Start execute get package all details by id  {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageAllDetailsResponse> response = packageService.getPackageAllDetailsById(packageId);
        LOGGER.info("{} End execute get package all details by id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/packageId-and-packageName")
    public ResponseEntity<CommonResponse<List<PackageIdAndPackageNameResponse>>> getPackageIdsAndPackageNames() {
        LOGGER.info("{} Start execute get all active package ids and names {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageIdAndPackageNameResponse>> response = packageService.getPackageIdsAndPackageNames();
        LOGGER.info("{} End execute get all package tour ids and names {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-statistics")
    public ResponseEntity<CommonResponse<PackageStatisticsResponse>> getPackageStatistics() {
        LOGGER.info("{} Start execute get package statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageStatisticsResponse> response = packageService.getPackageStatistics();
        LOGGER.info("{} End execute get package statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedule-statistics")
    public ResponseEntity<CommonResponse<PackageScheduleStatisticsResponse>> getPackageScheduleStatistics() {
        LOGGER.info("{} Start execute get package schedule statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageScheduleStatisticsResponse> response = packageService.getPackageScheduleStatistics();
        LOGGER.info("{} End execute get package schedule statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-type-statistics")
    public ResponseEntity<CommonResponse<PackageTypeStatisticsResponse>> getPackageTypeStatistics() {
        LOGGER.info("{} Start execute get package type statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageTypeStatisticsResponse> response = packageService.getPackageTypeStatistics();
        LOGGER.info("{} End execute get package type statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-types")
    public ResponseEntity<CommonResponse<List<PackageTypeBasicDetailsResponse>>> getPackageTypes() {
        LOGGER.info("{} Start execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageTypeBasicDetailsResponse>> response = packageService.getPackageTypes();
        LOGGER.info("{} End execute get tour type statistics {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/package-type-details")
    public ResponseEntity<CommonResponse<PackageTypeAllDetailsResponse>> getPackageTypeDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageTypeAllDetailsResponse> response = packageService.getPackageTypeDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/package-type-basic-details")
    public ResponseEntity<CommonResponse<PackageTypeBasicDetailsResponse>> getPackageTypeBasicDetailsById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageTypeBasicDetailsResponse> response = packageService.getPackageTypeBasicDetailsById(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-package-type")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminatePackageType(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = packageService.terminatePackageType(commonIdRequest);
        LOGGER.info("{} End execute terminate activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-package-type")
    public ResponseEntity<CommonResponse<InsertResponse>> insertPackageType(@RequestBody PackageTypeInsertRequest packageTypeInsertRequest) {
        LOGGER.info("{} Start execute insert activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = packageService.insertPackageType(packageTypeInsertRequest);
        LOGGER.info("{} End execute insert activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-package-type")
    public ResponseEntity<CommonResponse<UpdateResponse>> updatePackageType(@RequestBody PackageTypeUpdateRequest packageTypeUpdateRequest) {
        LOGGER.info("{} Start execute update activity {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = packageService.updatePackageType(packageTypeUpdateRequest);
        LOGGER.info("{} End execute update activity {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/package-schedule")
    public ResponseEntity<CommonResponse<PackageScheduleWithParamsResponse>> getPackageScheduleWithParams(@RequestBody PackageScheduleDataRequest packageScheduleDataRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageScheduleWithParamsResponse> response = packageService.getPackageScheduleWithParams(packageScheduleDataRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedule-params")
    public ResponseEntity<CommonResponse<PackageScheduleParamsResponse>> getPackageScheduleParams() {
        LOGGER.info("{} Start execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageScheduleParamsResponse> response = packageService.getPackageScheduleParams();
        LOGGER.info("{} End execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/package-schedule-details-by-id")
    public ResponseEntity<CommonResponse<PackageScheduleAllDetailsResponse>> getPackageScheduleDetailsById(@RequestBody CommonIdRequest packageScheduleId) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<PackageScheduleAllDetailsResponse> response = packageService.getPackageScheduleDetailsById(packageScheduleId);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/create-package-schedule")
    public ResponseEntity<CommonResponse<InsertResponse>> createPackageSchedule(@RequestBody PackageScheduleInsertRequest packageScheduleInsertRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = packageService.createPackageSchedule(packageScheduleInsertRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/update-package-schedule")
    public ResponseEntity<CommonResponse<UpdateResponse>> updatePackageSchedule(@RequestBody PackageScheduleUpdateRequest packageScheduleUpdateRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = packageService.updatePackageSchedule(packageScheduleUpdateRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-package-schedule")
    public ResponseEntity<CommonResponse<TerminateResponse>> termiantePackageScheduleById(@RequestBody CommonIdRequest commonIdRequest) {
        LOGGER.info("{} Start execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = packageService.termiantePackageScheduleById(commonIdRequest);
        LOGGER.info("{} End execute get active activities schedule for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedule-id-and-names")
    public ResponseEntity<CommonResponse<List<PackageScheduleIdAndNameResponse>>> getPackageScheduleIdAndNames() {
        LOGGER.info("{} Start execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageScheduleIdAndNameResponse>> response = packageService.getPackageScheduleIdAndNames();
        LOGGER.info("{} End execute get active activities schedule params for request {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
