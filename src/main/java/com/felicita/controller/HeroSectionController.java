package com.felicita.controller;

import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.*;
import com.felicita.model.response.*;
import com.felicita.model.response.heroSection.HeroSectionBasicResponse;
import com.felicita.model.response.heroSection.HeroSectionDataForParamsResponse;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.model.response.heroSection.HeroSectionParamResponse;
import com.felicita.model.response.statistics.HeroSectionStatisticsResponse;
import com.felicita.service.HeroSectionService;
import com.felicita.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v0/hero-section")
public class HeroSectionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroSectionController.class);

    private final HeroSectionService heroSectionService;

    @Autowired
    public HeroSectionController(HeroSectionService heroSectionService) {
        this.heroSectionService = heroSectionService;
    }

    @GetMapping(path = "/home-all")
    public ResponseEntity<CommonResponse<List<HeroSectionResponse>>> getAllHomeHeroSectionData(){
        LOGGER.info("{} Start execute get home hero section all data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<HeroSectionResponse>> response = heroSectionService.getAllHomeHeroSectionData();
        LOGGER.info("{} End execute get home hero section all data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/home")
    public ResponseEntity<CommonResponse<List<HeroSectionResponse>>> getHomeHeroSectionDetails(){
        LOGGER.info("{} Start execute get home hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<HeroSectionResponse>> response = heroSectionService.getHomeHeroSectionDetails();
        LOGGER.info("{} End execute get home hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/about-us")
    public ResponseEntity<CommonResponse<List<AboutUsHeroSectionResponse>>> getAboutUsHeroSectionDetails(){
        LOGGER.info("{} Start execute get about us hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<AboutUsHeroSectionResponse>> response = heroSectionService.getAboutUsHeroSectionDetails();
        LOGGER.info("{} End execute get about us hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/contact-us")
    public ResponseEntity<CommonResponse<List<ContactUsHeroSectionResponse>>> getContactUsHeroSectionDetails(){
        LOGGER.info("{} Start execute get contact us hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ContactUsHeroSectionResponse>> response = heroSectionService.getContactUsHeroSectionDetails();
        LOGGER.info("{} End execute get contact us hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/blog")
    public ResponseEntity<CommonResponse<List<BlogHeroSectionResponse>>> getBlogHeroSectionDetails(){
        LOGGER.info("{} Start execute get blog hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BlogHeroSectionResponse>> response = heroSectionService.getBlogHeroSectionDetails();
        LOGGER.info("{} End execute get blog hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/faq")
    public ResponseEntity<CommonResponse<List<FaqHeroSectionResponse>>> getFAQHeroSectionDetails(){
        LOGGER.info("{} Start execute get faq hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<FaqHeroSectionResponse>> response = heroSectionService.getFAQHeroSectionDetails();
        LOGGER.info("{} End execute get faq hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/tour")
    public ResponseEntity<CommonResponse<List<TourHeroSectionResponse>>> getTourHeroSectionDetails(){
        LOGGER.info("{} Start execute get tour hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<TourHeroSectionResponse>> response = heroSectionService.getTourHeroSectionDetails();
        LOGGER.info("{} End execute get tour hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package")
    public ResponseEntity<CommonResponse<List<PackageHeroSectionResponse>>> getPackageHeroSectionDetails(){
        LOGGER.info("{} Start execute get package hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageHeroSectionResponse>> response = heroSectionService.getPackageHeroSectionDetails();
        LOGGER.info("{} End execute get package hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/package-schedule/{packageScheduleId}")
    public ResponseEntity<CommonResponse<List<PackageScheduleHeroSectionResponse>>> getPackageScheduleHeroSectionDetails(@PathVariable Long packageScheduleId){
        LOGGER.info("{} Start execute get package schedule hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<PackageScheduleHeroSectionResponse>> response = heroSectionService.getPackageScheduleHeroSectionDetails(packageScheduleId);
        LOGGER.info("{} End execute get package schedule hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/booked-tour/{bookingId}")
    public ResponseEntity<CommonResponse<List<BookedTourHeroSectionResponse>>> getBookedTourHeroSectionDetails(@PathVariable Long bookingId){
        LOGGER.info("{} Start execute get booked tour hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<BookedTourHeroSectionResponse>> response = heroSectionService.getBookedTourHeroSectionDetails(bookingId);
        LOGGER.info("{} End execute get booked tour hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/destination")
    public ResponseEntity<CommonResponse<List<DestinationHeroSectionResponse>>> getDestinationHeroSectionDetails(){
        LOGGER.info("{} Start execute get destination hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<DestinationHeroSectionResponse>> response = heroSectionService.getDestinationHeroSectionDetails();
        LOGGER.info("{} End execute get destination hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/activity")
    public ResponseEntity<CommonResponse<List<ActivityHeroSectionResponse>>> getActivityHeroSectionDetails(){
        LOGGER.info("{} Start execute get activity hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityHeroSectionResponse>> response = heroSectionService.getActivityHeroSectionDetails();
        LOGGER.info("{} End execute get activity hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/vehicle")
    public ResponseEntity<CommonResponse<List<VehicleHeroSectionResponse>>> getVehicleHeroSectionDetails(){
        LOGGER.info("{} Start execute get vehicle hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<VehicleHeroSectionResponse>> response = heroSectionService.getVehicleHeroSectionDetails();
        LOGGER.info("{} End execute get vehicle hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/vehicle-specification")
    public ResponseEntity<CommonResponse<List<VehicleSpecificationHeroSectionResponse>>> getVehicleSpecificationHeroSectionDetails(){
        LOGGER.info("{} Start execute get vehicle specification hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<VehicleSpecificationHeroSectionResponse>> response = heroSectionService.getVehicleSpecificationHeroSectionDetails();
        LOGGER.info("{} End execute get vehicle specification hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/vehicle-types")
    public ResponseEntity<CommonResponse<List<VehicleTypesHeroSectionResponse>>> getVehicleTypesHeroSectionDetails(){
        LOGGER.info("{} Start execute get vehicle types hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<VehicleTypesHeroSectionResponse>> response = heroSectionService.getVehicleTypesHeroSectionDetails();
        LOGGER.info("{} End execute get vehicle types hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/seasons")
    public ResponseEntity<CommonResponse<List<SeasonsHeroSectionResponse>>> getSeasonHeroSectionDetails(){
        LOGGER.info("{} Start execute get season hero section data {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<SeasonsHeroSectionResponse>> response = heroSectionService.getSeasonHeroSectionDetails();
        LOGGER.info("{} End execute get season hero section data {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/activity/{activityId}")
    public ResponseEntity<CommonResponse<List<ActivityDetailsHeroSectionResponse>>> getActivityHeroSectionDetailsByActivityId(@PathVariable Long activityId){
        LOGGER.info("{} Start execute get activity hero section data by activity id {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<List<ActivityDetailsHeroSectionResponse>> response = heroSectionService.getActivityHeroSectionDetailsByActivityId(activityId);
        LOGGER.info("{} End execute get activity hero section data by activity id {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // admin
    @PostMapping(path = "/basic-details-for-request")
    public ResponseEntity<CommonResponse<HeroSectionParamResponse>> getHeroSectionDataWithParams(@RequestBody HeroSectionDataRequest heroSectionDataRequest) {
        LOGGER.info("{} Start execute get hero section basic details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<HeroSectionParamResponse> response = heroSectionService.getHeroSectionDataWithParams(heroSectionDataRequest);
        LOGGER.info("{} End execute get hero section basic details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/data-for-request-params")
    public ResponseEntity<CommonResponse<HeroSectionDataForParamsResponse>> getDataForRequestParams(@RequestBody HeroSectionTypeRequest heroSectionTypeRequest) {
        LOGGER.info("{} Start execute get hero section basic details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<HeroSectionDataForParamsResponse> response = heroSectionService.getDataForRequestParams(heroSectionTypeRequest);
        LOGGER.info("{} End execute get hero section basic details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/hero-section-details")
    public ResponseEntity<CommonResponse<HeroSectionDetailsResponse>> getHeroSectionDetailsById(@RequestBody HeroSectionDetailsDataRequest heroSectionDetailsDataRequest) {
        LOGGER.info("{} Start execute get hero section details {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<HeroSectionDetailsResponse> response = heroSectionService.getHeroSectionDetailsById(heroSectionDetailsDataRequest);
        LOGGER.info("{} End execute get hero section details {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add-hero-section")
    public ResponseEntity<CommonResponse<InsertResponse>> addHeroSection(@RequestBody HeroSectionInsertRequest heroSectionInsertRequest) {
        LOGGER.info("{} Start execute add hero section {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<InsertResponse> response = heroSectionService.addHeroSection(heroSectionInsertRequest);
        LOGGER.info("{} End execute add hero section {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path = "/update-hero-section")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateHeroSection(@RequestBody HeroSectionUpdateRequest heroSectionUpdateRequest) {
        LOGGER.info("{} Start execute update hero section {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<UpdateResponse> response = heroSectionService.updateHeroSection(heroSectionUpdateRequest);
        LOGGER.info("{} End execute update hero section {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/terminate-hero-section")
    public ResponseEntity<CommonResponse<TerminateResponse>> terminateHeroSection(@RequestBody IdWithTypeRequest idWithTypeRequest) {
        LOGGER.info("{} Start execute terminate hero section {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<TerminateResponse> response = heroSectionService.terminateHeroSection(idWithTypeRequest);
        LOGGER.info("{} End execute terminate hero section {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/hero-section-statistics")
    public ResponseEntity<CommonResponse<HeroSectionStatisticsResponse>> getHeroSectionStatisctisByType(@RequestBody HeroSectionTypeRequest heroSectionTypeRequest) {
        LOGGER.info("{} Start execute get statistics by type of hero section {}", Constant.DOTS, Constant.DOTS);
        CommonResponse<HeroSectionStatisticsResponse> response = heroSectionService.getHeroSectionStatisctisByType(heroSectionTypeRequest);
        LOGGER.info("{} End execute get statistics by type of hero section {}", Constant.DOTS, Constant.DOTS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
