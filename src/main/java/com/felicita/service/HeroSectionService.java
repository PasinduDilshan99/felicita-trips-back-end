package com.felicita.service;

import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.*;
import com.felicita.model.response.*;
import com.felicita.model.response.heroSection.HeroSectionDataForParamsResponse;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.model.response.heroSection.HeroSectionParamResponse;

import java.util.List;

public interface HeroSectionService {

    CommonResponse<List<HeroSectionResponse>> getAllHomeHeroSectionData();

    CommonResponse<List<HeroSectionResponse>> getHomeHeroSectionDetails();

    CommonResponse<List<AboutUsHeroSectionResponse>> getAboutUsHeroSectionDetails();

    CommonResponse<List<ContactUsHeroSectionResponse>> getContactUsHeroSectionDetails();

    CommonResponse<List<BlogHeroSectionResponse>> getBlogHeroSectionDetails();

    CommonResponse<List<FaqHeroSectionResponse>> getFAQHeroSectionDetails();

    CommonResponse<List<TourHeroSectionResponse>> getTourHeroSectionDetails();

    CommonResponse<List<PackageHeroSectionResponse>> getPackageHeroSectionDetails();

    CommonResponse<List<DestinationHeroSectionResponse>> getDestinationHeroSectionDetails();

    CommonResponse<List<ActivityHeroSectionResponse>> getActivityHeroSectionDetails();

    CommonResponse<List<PackageScheduleHeroSectionResponse>> getPackageScheduleHeroSectionDetails(Long packageScheduleId);

    CommonResponse<List<BookedTourHeroSectionResponse>> getBookedTourHeroSectionDetails(Long bookingId);

    CommonResponse<List<ActivityDetailsHeroSectionResponse>> getActivityHeroSectionDetailsByActivityId(Long activityId);

    CommonResponse<List<VehicleHeroSectionResponse>> getVehicleHeroSectionDetails();

    CommonResponse<List<VehicleSpecificationHeroSectionResponse>> getVehicleSpecificationHeroSectionDetails();

    CommonResponse<List<VehicleTypesHeroSectionResponse>> getVehicleTypesHeroSectionDetails();

    CommonResponse<List<SeasonsHeroSectionResponse>> getSeasonHeroSectionDetails();

    CommonResponse<HeroSectionParamResponse> getHeroSectionDataWithParams(HeroSectionDataRequest heroSectionDataRequest);

    CommonResponse<HeroSectionDataForParamsResponse> getDataForRequestParams(HeroSectionTypeRequest heroSectionTypeRequest);

    CommonResponse<HeroSectionDetailsResponse> getHeroSectionDetailsById(HeroSectionDetailsDataRequest heroSectionDetailsDataRequest);

    CommonResponse<InsertResponse> addHeroSection(HeroSectionInsertRequest heroSectionInsertRequest);

    CommonResponse<UpdateResponse> updateHeroSection(HeroSectionUpdateRequest heroSectionUpdateRequest);

    CommonResponse<TerminateResponse> terminateHeroSection(IdWithTypeRequest idWithTypeRequest);
}
