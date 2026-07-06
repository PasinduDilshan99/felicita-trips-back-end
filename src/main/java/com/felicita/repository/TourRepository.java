package com.felicita.repository;

import com.felicita.model.dto.PopularTourResponseDto;
import com.felicita.model.dto.TourAssignUserDto;
import com.felicita.model.dto.TourDayDestinationActivityIdsDto;
import com.felicita.model.dto.TourResponseDto;
import com.felicita.model.request.*;
import com.felicita.model.request.tour.category.TourCategoryImageInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryImageUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.request.tour.schedule.TourScheduleDataRequest;
import com.felicita.model.request.tour.schedule.TourScheduleInsertRequest;
import com.felicita.model.request.tour.schedule.TourScheduleUpdateRequest;
import com.felicita.model.request.tour.type.TourTypeImageInsertRequest;
import com.felicita.model.request.tour.type.TourTypeImageUpdateRequest;
import com.felicita.model.request.tour.type.TourTypeInsertRequest;
import com.felicita.model.request.tour.type.TourTypeUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.TourCategoryStatisticsResponse;
import com.felicita.model.response.statistics.TourScheduleStatisticsResponse;
import com.felicita.model.response.statistics.TourStatisticsResponse;
import com.felicita.model.response.statistics.TourTypeStatisticsResponse;
import com.felicita.model.response.tour.category.TourCategoryAllDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryBasicDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleBasicDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleDetailsResponse;
import com.felicita.model.response.tour.schedule.TourScheduleWithParamsResponse;
import com.felicita.model.response.tour.type.TourTypeAllDetailsResponse;
import com.felicita.model.response.tour.type.TourTypeBasicDetailsResponse;

import java.util.List;

public interface TourRepository {

    List<TourResponseDto> getAllTours();

    List<PopularTourResponseDto> getPopularTours();

    TourResponseDto getTourDetailsById(Long tourId);

    List<TourReviewDetailsResponse> getAllTourReviewDetails();

    List<TourReviewDetailsResponse> getTourReviewDetailsById(Long tourId);

    List<TourDestinationsForMapResponse> getTourDestinationsForMap(Long tourId);

    List<TourHistoryResponse> getAllTourHistoryDetails();

    List<TourHistoryResponse> getTourHistoryDetailsById(Long tourId);

    List<TourHistoryImageResponse> getTourHistoryImagesById(Long tourId);

    List<TourHistoryImageResponse> getAllTourHistoryImages();

    ToursDetailsWithParamResponse getToursToShowWithParam(TourDataRequest tourDataRequest);

    List<TourDayDestinationActivityIdsDto> getTourDayDestinationActivityIds(Long tourId);

    List<TourDetailsWithDayToDayResponse.DestinationDetailsPerDay> getDestinationsDetailsByIds(List<Long> destinationIdList);

    List<TourDetailsWithDayToDayResponse.ActivityPerDayResponse> getActivityDetailsByIds(List<Long> activityIdList);

    List<TourExtrasResponse.TourInclusion> getTourInclusions(Long tourId);

    List<TourExtrasResponse.TourExclusion> getTourExclusions(Long tourId);

    List<TourExtrasResponse.TourCondition> getTourConditions(Long tourId);

    List<TourExtrasResponse.TourTravelTip> getTourTravelTips(Long tourId);

    List<TourSchedulesResponse.TourScheduleDetails> getTourSchedulesById(Long tourId);

    TourSchedulesResponse.TourBasicDetails getTourBasicDetails(Long tourId);

    List<TourBasicDetailsResponse> getAllToursBasicDetails();

    List<TourForTerminateResponse> getToursForTerminate();

    void terminateTour(TourTerminateRequest tourTerminateRequest, Long userId);

    Long insertTourDetails(TourInsertRequest tourInsertRequest, Long userId);

    void insertTourImages(Long tourId, List<TourImageInsertRequest> images, Long userId);

    void insertTourInclusions(Long tourId, List<TourInclusionInsertRequest> inclusions, Long userId);

    void insertTourExclusions(Long tourId, List<TourExclusionInsertRequest> exclusions, Long userId);

    void insertTourConditions(Long tourId, List<TourConditionInsertRequest> conditions, Long userId);

    void insertTourTravelTips(Long tourId, List<TourTravelTipInsertRequest> travelTips, Long userId);

    TourDetailsForAddPackageResponse getTourDetailsForAddPackage(Long tourId);

    List<String> getTourInclusionsNamesOnly(Long tourId);

    List<String> getTourExclusionsNamesOnly(Long tourId);

    List<String> getTourConditionsNamesOnly(Long tourId);

    List<TourDetailsForAddPackageResponse.TravelTip> getTourTravelTipsNamesOnly(Long tourId);

    void updateTourBasicDetails(Long tourId, TourUpdateRequest.TourBasicDetails tourBasicDetails, Long userId);

    void removeTourDestinations(Long tourId, List<Long> removeDestinations, Long userId);

    void updateTourDestinations(Long tourId, List<TourDestinationUpdateRequest> updateDestinations, Long userId);

    void removeTourImages(Long tourId, List<Long> removeImages, Long userId);

    void updateTourImages(Long tourId, List<TourImageUpdateRequest> updateImages, Long userId);

    void removeTourInclusions(Long tourId, List<Long> removeInclusions, Long userId);

    void updateTourInclusions(Long tourId, List<TourInclusionUpdateRequest> updateInclusions, Long userId);

    void removeTourExclusions(Long tourId, List<Long> removeExclusions, Long userId);

    void updateTourExclusions(Long tourId, List<TourExclusionUpdateRequest> updateExclusions, Long userId);

    void removeTourConditions(Long tourId, List<Long> removeConditions, Long userId);

    void updateTourConditions(Long tourId, List<TourConditionUpdateRequest> updateConditions, Long userId);

    void removeTourTravelTips(Long tourId, List<Long> removeTravelTips, Long userId);

    void updateTourTravelTips(Long tourId, List<TourTravelTipUpdateRequest> updateTravelTips, Long userId);

    TourAssignUserDto getTourAssignUserDetailsByTourId(Long tourId);

    TourStatisticsResponse.Summary getToutSummeryStatistics();

    List<TourStatisticsResponse.TourPopularity> getTourPopularityStatistics();

    List<TourStatisticsResponse.BookingStatusDistribution> getBookingStatusDistributionStatistics();

    List<TourStatisticsResponse.CategoryPerformance> getCategoryPerformanceStatistics();

    List<TourStatisticsResponse.TypeDistribution> getTypeDistributionStatistics();

    List<TourStatisticsResponse.LocationDistribution> getLocationDistributionStatistics();

    TourScheduleStatisticsResponse.Summary getTourScheduleSummeryStatistics();

    List<TourScheduleStatisticsResponse.ScheduleTimeline> getScheduleTimelineStatistics();

    List<TourScheduleStatisticsResponse.DurationDistribution> getDurationDistributionStatistics();

    List<TourScheduleStatisticsResponse.ScheduleExecutionPerformance> getScheduleExecutionPerformanceStatistics();

    List<TourScheduleStatisticsResponse.ScheduleRatingOverview> getScheduleRatingOverviewStatistics();

    List<TourScheduleStatisticsResponse.ParticipationTrend> getParticipationTrendStatistics();

    TourCategoryStatisticsResponse.Summary getTourCategorySummaryStatistics();

    List<TourCategoryStatisticsResponse.CategoryDistribution> getCategoryDistributionStatistics();

    List<TourCategoryStatisticsResponse.CategoryBookingPerformance> getCategoryBookingPerformanceStatistics();

    List<TourCategoryStatisticsResponse.CategoryRatingOverview> getCategoryRatingOverviewStatistics();

    List<TourCategoryStatisticsResponse.CategoryPrimarySecondaryUsage> getCategoryPrimarySecondaryUsageStatistics();

    List<TourCategoryStatisticsResponse.CategoryParticipationImpact> getCategoryParticipationImpactStatistics();

    TourTypeStatisticsResponse.Summary getTourTypeSummaryStatistics();

    List<TourTypeStatisticsResponse.TypeDistribution> getTypesDistributionStatistics();

    List<TourTypeStatisticsResponse.TypeBookingPerformance> getTypeBookingPerformanceStatistics();

    List<TourTypeStatisticsResponse.TypeRatingOverview> getTypeRatingOverviewStatistics();

    List<TourTypeStatisticsResponse.TypeParticipationImpact> getTypeParticipationImpactStatistics();

    List<TourTypeStatisticsResponse.TypePrimarySecondaryUsage> getTypePrimarySecondaryUsageStatistics();

    void insertTourTypesToTour(Long tourId, List<Long> tourTypes, Long userId);

    void insertTourCategoriesToTour(Long tourId, List<Long> tourCategories, Long userId);

    void removeTourTypesFromTour(Long tourId, List<Long> removeTourTypes, Long userId);

    void updateTourTypesInTour(Long tourId, List<TourUpdateRequest.TourTypeUpdateRequest> updateTourTypes, Long userId);

    void updateTourCategoriesInTour(Long tourId, List<TourUpdateRequest.TourCategoryUpdateRequest> updateTourCategories, Long userId);

    void removeTourCategoriesFromTour(Long tourId, List<Long> removeTourCategories, Long userId);

    void removeActivitiesFromTourDestinations(Long tourId, List<Long> removeActivities, Long userId);

    void terminateTourDestinations(Long tourId, Long userId);

    void terminateTourTypesAssignToTour(Long tourId, Long userId);

    void terminateTourCategoriesAssignToTour(Long tourId, Long userId);

    void insertTourDestinations(Long tourId, List<TourItineraryDayRequest> itinerary, Long userId);

    List<TourCategoryBasicDetailsResponse> getTourCategories();

    TourCategoryAllDetailsResponse getTourCategoryDetailsById(CommonIdRequest commonIdRequest);

    TourCategoryBasicDetailsResponse getTourCategoryBasicDetailsById(CommonIdRequest commonIdRequest);

    void terminateTourCategory(CommonIdRequest commonIdRequest,Long userId);

    Long insertTourCategoryBasicDetails(TourCategoryInsertRequest tourCategoryInsertRequest, Long userId);

    void insertTourCatgeoryImages(Long tourCategoryId, List<TourCategoryImageInsertRequest> images, Long userId);

    void insertToursForTourCategory(Long tourCategoryId, List<Long> tourIds, Long userId);

    void updateTourCategoryBasicDetails(TourCategoryUpdateRequest tourCategoryUpdateRequest,Long userId);

    void removeToursForTourCategory(Long categoryId, List<Long> removeTourIds, Long userId);

    void removeTourCatgeoryImages(Long categoryId, List<Long> removeImageIds, Long userId);

    void updateTourCatgeoryImages(Long categoryId, List<TourCategoryImageUpdateRequest> updateImages, Long userId);

    List<TourTypeBasicDetailsResponse> getTourTypes();

    TourTypeAllDetailsResponse getTourTypeDetailsById(CommonIdRequest commonIdRequest);

    TourTypeBasicDetailsResponse getTourTypeBasicDetailsById(CommonIdRequest commonIdRequest);

    void terminateTourType(CommonIdRequest commonIdRequest, Long userId);

    Long insertTourTypeBasicDetails(TourTypeInsertRequest tourTypeInsertRequest,Long userId);

    void insertTourTypeImages(Long tourTypeId, List<TourTypeImageInsertRequest> images, Long userId);

    void insertToursForTourType(Long tourTypeId, List<Long> tourIds, Long userId);

    void updateTourTypeBaiscDetails(TourTypeUpdateRequest tourTypeUpdateRequest, Long userId);

    void removeToursForTourType(Long typeId, List<Long> removeTourIds, Long userId);

    void removeTourTypeImages(Long typeId, List<Long> removeImageIds, Long userId);

    void updateTourTypeImages(Long typeId, List<TourTypeImageUpdateRequest> updateImages, Long userId);

    TourScheduleWithParamsResponse getTourScheduleWithParams(TourScheduleDataRequest tourScheduleDataRequest);

    List<String> getDistinctTourDurations();

    TourScheduleDetailsResponse getTourScheduleDetailsById(CommonIdRequest tourScheduleId);

    Long createTourSchedule(TourScheduleInsertRequest tourScheduleInsertRequest);

    TourScheduleBasicDetailsResponse getTourScheduleBasicDetails(Long tourScheduleId);

    void updateTourSchedule(TourScheduleUpdateRequest tourScheduleUpdateRequest,Long userId);

    void terminateTourScheduleById(CommonIdRequest commonIdRequest, Long userId);

    Double getMinPriceForTour();

    Double getMaxPriceForTour();

    List<Integer> getDistnictDurations();

    List<String> getDistinctLocations();
}
