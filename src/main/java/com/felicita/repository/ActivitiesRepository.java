package com.felicita.repository;

import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.ActivityCategoriesStatisticsResponse;
import com.felicita.model.response.statistics.ActivityScheduleStatisticsResponse;

import java.util.List;

public interface ActivitiesRepository {

    List<ActivityResponseDto> getAllActivities();

    List<ActivityCategoryResponseDto> getAllActivityCategories();

    List<ActivityReviewDetailsResponse> getActivityReviewDetailsById(Long activityId);

    List<ActivityReviewDetailsResponse> getAllActivityReviewDetails();

    ActivityResponseDto getActivityById(Long activityId);

    List<ActivityHistoryDetailsResponse> getActivityHistoryDetailsById(Long activityId);

    List<ActivityHistoryDetailsResponse> getAllActivityHistoryDetails();

    List<ActivityHistoryImageResponse> getAllActivityHistoryImages();

    List<ActivityHistoryImageResponse> getActivityHistoryImagesById(Long activityId);

    ActivityWithParamsResponse getActivitiesWithParams(ActivityDataRequest activityDataRequest);

    List<ActivityForTerminateResponse> getActivitiesForTerminate();

    void terminateActivity(ActivityTerminateRequest activityTerminateRequest, Long userId);

    Long insertActivityDetails(ActivityInsertRequest activityInsertRequest, Long userId);

    void insertActivityImages(Long activityId, List<ActivityImageInsertRequest> images, Long userId);

    void insertActivityRequirements(Long activityId, List<ActivityRequirementInsertRequest> requirements, Long userId);

    void updateBasicActivityDetails(ActivityUpdateRequest activityUpdateRequest, Long userId);

    void removeActivityImages(List<Long> removeImagesIds, Long userId);

    void removeRequirements(List<Long> removeRequirementsIds, Long userId);


    void updateActivityImages(Long activityId, List<ActivityImageUpdateRequest> updatedImages, Long userId);

    void updateActivityRequirements(Long activityId, List<ActivityRequirementsUpdateRequest> updatedRequirements, Long userId);

    ActivityStatisticsResponse.ActivityDetails getActivityDetailsStatistics();

    ActivityStatisticsResponse.WishDetails getActivityWishStatistics();

    List<ActivityStatisticsResponse.CategoryDetails> getActivityCategoryStatistics();

    ActivityScheduleStatisticsResponse.Summary getActivitySchduleSummeryStatsitics();

    List<ActivityScheduleStatisticsResponse.ActivityParticipationTrend> getActivityParticipationTrendsStatsitics();

    List<ActivityScheduleStatisticsResponse.ActivityRatingOverview> getActivityRatingOverviewStatsitics();

    List<ActivityScheduleStatisticsResponse.PopularActivity> getPopularActivitiesStatsitics();

    List<ActivityScheduleStatisticsResponse.ScheduleTimeline> getScheduleTimelineStatsitics();

    List<ActivityScheduleStatisticsResponse.ActivityStatusDistribution> getActivityStatusDistributionStatsitics();

    ActivityCategoriesStatisticsResponse.Summary getActivitySummeryStatistics();

    List<ActivityCategoriesStatisticsResponse.CategoryActivityCount> getCategoryActivityCountStatistics();

    List<ActivityCategoriesStatisticsResponse.CategoryParticipationPerformance> getCategoryParticipationPerformanceStatistics();

    List<ActivityCategoriesStatisticsResponse.CategoryRatingOverview> getCategoryRatingOverviewStatistics();

    List<ActivityCategoriesStatisticsResponse.CategoryDistribution> getCategoryDistributionStatistics();

    List<ActivityCategoriesStatisticsResponse.CategoryPrimarySecondaryUsage> getCategoryPrimarySecondaryUsageStatistics();
}
