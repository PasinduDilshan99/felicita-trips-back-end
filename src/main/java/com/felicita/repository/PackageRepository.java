package com.felicita.repository;

import com.felicita.model.dto.*;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.PackageScheduleStatisticsResponse;
import com.felicita.model.response.statistics.PackageStatisticsResponse;
import com.felicita.model.response.statistics.PackageTypeStatisticsResponse;

import java.util.List;

public interface PackageRepository {

    List<PackageResponseDto> getAllPackages();

    PackageResponseDto getPackageDetailsById(Long packageId);

    List<PackageReviewResponse> getAllPackageReviewDetails();

    List<PackageReviewResponse> getPackageReviewDetailsById(Long packageId);

    List<PackageHistoryImageResponse> getPackageHistoryImagesById(Long packageId);

    List<PackageHistoryImageResponse> getAllPackageHistoryImages();

    List<PackageHistoryDetailsResponse> getPackageHistoryDetailsById(Long packageId);

    List<PackageHistoryDetailsResponse> getAllPackageHistoryDetails();

    PackageWithParamsResponse getPackagesWithParams(PackageDataRequest packageDataRequest);

    List<PackageDetailsDto> getDayToPackageDetailsById(Long tourId);

    List<PackageDayByDayDto> getPackagesAccoamdationsByIds(List<Long> packgeIds);

    List<Long> getPackageIdsByTourId(Long tourId);

    List<PackageExtrasResponse.PackageExclusion> getPackageExclusions(Long packageId);

    List<PackageExtrasResponse.PackageInclusion> getPackageInclusions(Long packageId);

    List<PackageExtrasResponse.PackageCondition> getPackageConditions(Long packageId);

    List<PackageExtrasResponse.PackageTravelTip> getPackageTravelTips(Long packageId);

    List<Long> getPackageSchedulesIdsByTourId(Long tourId);

    List<PackageScheduleResponse.PackageScheduleDetails> getPackageSchedulesById(Long packageId);

    List<PackageScheduleDetailsResponse.PackageScheduleDetails> getPackageSchedulesForId(Long packageId);

    PackageScheduleDetailsResponse.PackageBasicDetails getPackageBasicDetails(Long packageId);

    List<PackageComapreResponse.PackageImages> getAllPackagesImages(Long tourId);

    PackageBasicDetailsDto getPackageBasicDetailsByScheduleId(Long packageScheduleId);

    List<PackageDayAccommodationPriceDto> getPackageDayAccommodationPriceByScheduleId(Long packageScheduleId);

    List<PackageDestinationExtraPriceDto> getPackageDestinationExtraPriceByScheduleId(Long packageScheduleId);

    List<PackageActivityPriceDto> getPackageActivityPriceByScheduleId(Long packageScheduleId);

    List<PackageForTerminateResponse> getPackagesForTerminate();

    void terminatePackage(PackageTerminateRequest packageTerminateRequest, Long userId);

    Long insertPackageDeails(PackageInsertRequest packageInsertRequest, Long userId);

    void insertPackageImages(Long packageId, List<PackageImageInsertRequest> images, Long userId);

    void insertPackageInclusions(Long packageId, List<PackageInclusionInsertRequest> inclusions, Long userId);

    void insertPackageExclusions(Long packageId, List<PackageExclusionInsertRequest> exclusions, Long userId);

    void insertPackageConditions(Long packageId, List<PackageConditionInsertRequest> conditions, Long userId);

    void insertPackageTravelTips(Long packageId, List<PackageTravelTipInsertRequest> travelTips, Long userId);

    void insertDayByDayAccommodations(Long packageId, List<PackageDayAccommodationInsertRequest> dayAccommodations, Long userId);

    void updatePackageBasicDetails(Long packageId, PackageUpdateRequest.PackageBasicDetails packageBasicDetails, Long userId);

    void removePackageImages(Long packageId, List<Long> removedImageIds, Long userId);

    void updatePackageImages(Long packageId, List<PackageImageUpdateRequest> updatedImages, Long userId);

    void removeDayByDayAccommodations(Long packageId, List<Long> removeDayAccommodationIds, Long userId);

    void updateDayByDayAccommodations(Long packageId, List<PackageDayAccommodationUpdateRequest> updatedDayAccommodations, Long userId);

    void removePcakageInclusions(Long packageId, List<Long> removeInclusionIds, Long userId);

    void updatePackageInclusions(Long packageId, List<PackageInclusionUpdateRequest> updatedInclusions, Long userId);

    void removePackageExclusions(Long packageId, List<Long> removeExclusionIds, Long userId);

    void updatePackageExclusions(Long packageId, List<PackageExclusionUpdateRequest> updatedExclusions, Long userId);

    void removePcakageConditions(Long packageId, List<Long> removeConditionIds, Long userId);

    void updatePackageConditions(Long packageId, List<PackageConditionUpdateRequest> updatedConditions, Long userId);

    void removePcakageTravelTips(Long packageId, List<Long> removeTravelTipIds, Long userId);

    void updatePackageTravelTips(Long packageId, List<PackageTravelTipUpdateRequest> updatedTravelTips, Long userId);

    void insertPackageFeatures(Long packageId, List<PackageFeaturesInsertRequest> addFeatures, Long userId);

    void removePackageFeatures(Long packageId, List<Long> removeFeatureIds, Long userId);

    void updatePackageFeatures(Long packageId, List<PackageFeaturesUpdateRequest> updatedFeatures, Long userId);

    PackageStatisticsResponse.Summary getPackageSummaryStatistics();

    List<PackageStatisticsResponse.PackagePopularity> getPackagePopularityStatistics();

    List<PackageStatisticsResponse.PackageRatingOverview> getPackageRatingOverviewStatistics();

    List<PackageStatisticsResponse.PackageCapacityUtilization> getPackageCapacityUtilizationStatistics();

    List<PackageStatisticsResponse.PackageTypeDistribution> getPackageTypeDistributionStatistics();

    List<PackageStatisticsResponse.PackagePriceDistribution> getPackagePriceDistributionStatistics();

    PackageScheduleStatisticsResponse.Summary getPackageScheduleSummaryStatistics();

    List<PackageScheduleStatisticsResponse.ScheduleTimeline> getPackageScheduleTimelineStatistics();

    List<PackageScheduleStatisticsResponse.ScheduleStatusDistribution> getPackageScheduleStatusDistributionStatistics();

    List<PackageScheduleStatisticsResponse.DurationDistribution> getPackageScheduleDurationDistributionStatistics();

    List<PackageScheduleStatisticsResponse.ScheduleParticipationPerformance> getPackageScheduleParticipationPerformanceStatistics();

    List<PackageScheduleStatisticsResponse.ScheduleRatingOverview> getPackageScheduleRatingOverviewStatistics();

    PackageTypeStatisticsResponse.Summary getPackageTypeSummaryStatistics();

    List<PackageTypeStatisticsResponse.TypeDistribution> getPackageTypesDistributionStatistics();

    List<PackageTypeStatisticsResponse.TypeRevenuePerformance> getPackageTypeRevenuePerformanceStatistics();

    List<PackageTypeStatisticsResponse.TypeParticipationImpact> getPackageTypeParticipationImpactStatistics();

    List<PackageTypeStatisticsResponse.TypePrimarySecondaryUsage> getPackageTypePrimarySecondaryUsageStatistics();

    List<PackageTypeStatisticsResponse.TypeBookingPerformance> getPackageTypeBookingPerformanceStatistics();

    List<PackageTypeStatisticsResponse.TypeRatingOverview> getPackageTypeRatingOverviewStatistics();

    void removeAllPackageImages(Long packageId, Long userId);

    void removeAllPackageFeatures(Long packageId, Long userId);

    void removeAllDayByDayAccommodations(Long packageId, Long userId);

    void removeAllPcakageInclusions(Long packageId, Long userId);

    void removeAllPackageExclusions(Long packageId, Long userId);

    void removeAllPcakageConditions(Long packageId, Long userId);

    void removeAllPcakageTravelTips(Long packageId, Long userId);

    List<HotelsNamesAndIdsDto> getHotelNamesAndIds(AddPackageParamRequest addPackageParamRequest);

    List<VehicleNumberIdTypeDto> getVehicleNumberIdType(AddPackageParamRequest addPackageParamRequest);

    List<String> getTourInclusionsNames(AddPackageParamRequest addPackageParamRequest);

    List<String> getTourExclusionsNames(AddPackageParamRequest addPackageParamRequest);

    List<String> getTourConditions(AddPackageParamRequest addPackageParamRequest);

    List<AddPackageParamResponse.TravelTips> getTourTravelTips(AddPackageParamRequest addPackageParamRequest);
}
