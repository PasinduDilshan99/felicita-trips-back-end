package com.felicita.repository;

import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.*;
import com.felicita.model.response.*;
import com.felicita.model.response.common.IdAndNameResponse;
import com.felicita.model.response.heroSection.HeroSectionBasicResponse;
import com.felicita.model.response.heroSection.HeroSectionDataForParamsResponse;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.model.response.statistics.HeroSectionStatisticsResponse;

import java.util.List;

public interface HeroSectionRepository {

    List<HeroSectionResponse> getAllHomeHeroSectionData();

    List<AboutUsHeroSectionResponse> getAboutUsHeroSectionDetails();

    List<ContactUsHeroSectionResponse> getContactUsHeroSectionDetails();

    List<BlogHeroSectionResponse> getBlogHeroSectionDetails();

    List<FaqHeroSectionResponse> getFAQHeroSectionDetails();

    List<TourHeroSectionResponse> getTourHeroSectionDetails();

    List<ActivityHeroSectionResponse> getActivityHeroSectionDetails();

    List<DestinationHeroSectionResponse> getDestinationHeroSectionDetails();

    List<PackageHeroSectionResponse> getPackageHeroSectionDetails();

    List<PackageScheduleHeroSectionResponse> getPackageScheduleHeroSectionDetails(Long packageScheduleId);

    List<BookedTourHeroSectionResponse> getBookedTourHeroSectionDetails(Long bookingId);

    List<ActivityDetailsHeroSectionResponse> getActivityHeroSectionDetailsByActivityId(Long activityId);

    List<VehicleHeroSectionResponse> getVehicleHeroSectionDetails();

    List<VehicleSpecificationHeroSectionResponse> getVehicleSpecificationHeroSectionDetails();

    List<VehicleTypesHeroSectionResponse> getVehicleTypesHeroSectionDetails();

    List<SeasonsHeroSectionResponse> getSeasonHeroSectionDetails();

    List<HeroSectionBasicResponse> getHeroSectionBasicResponseForParms(HeroSectionDataRequest heroSectionDataRequest);

    Integer getHeroSectionBasicResponseCountForParms(HeroSectionDataRequest heroSectionDataRequest);

    HeroSectionDataForParamsResponse getDataForRequestParams(HeroSectionTypeRequest heroSectionTypeRequest);

    HeroSectionDetailsResponse getHeroSectionDetailsById(HeroSectionDetailsDataRequest heroSectionDetailsDataRequest);

    Long insertHeroSectionDetails(HeroSectionInsertRequest heroSectionInsertRequest, Long userId);

    void updateBasicHeroSectionDetails(HeroSectionUpdateRequest heroSectionUpdateRequest, Long userId);

    void terminateHeroSection(IdWithTypeRequest idWithTypeRequest, Long userId);

    HeroSectionStatisticsResponse.Summary getHeroSectionSummaryStatistics(String heroSectionType);

    List<HeroSectionStatisticsResponse.StatusStatistics> getHeroSectionStatusStatistics(String heroSectionType);

    List<HeroSectionStatisticsResponse.MonthlyStatistics> getHeroSectionMonthlyStatistics(String heroSectionType);

    List<HeroSectionStatisticsResponse.ActivityStatistics> getHeroSectionActivityStatistics(String heroSectionType);

    List<HeroSectionStatisticsResponse.TopEditorStatistics> getHeroSectionTopEditorStatistics(String heroSectionType);

    List<IdAndNameResponse> getHeroSectionNameAndIdsForType(HeroSectionTypeRequest heroSectionTypeRequest);
}
