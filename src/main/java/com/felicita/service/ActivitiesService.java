package com.felicita.service;

import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.request.ActivityDataRequest;
import com.felicita.model.request.ActivityInsertRequest;
import com.felicita.model.request.ActivityTerminateRequest;
import com.felicita.model.request.ActivityUpdateRequest;
import com.felicita.model.response.*;
import java.util.List;

public interface ActivitiesService {

    CommonResponse<List<ActivityResponseDto>> getAllActivities();

    CommonResponse<List<ActivityResponseDto>> getActiveActivities();

    CommonResponse<List<ActivityCategoryResponseDto>> getAllActivityCategories();

    CommonResponse<List<ActivityCategoryResponseDto>> getActiveActivityCategories();

    CommonResponse<List<ActivityReviewDetailsResponse>> getAllActivityReviewDetails();

    CommonResponse<List<ActivityReviewDetailsResponse>> getActivityReviewDetailsById(Long activityId);

    CommonResponse<ActivityResponseDto> getActivityById(Long activityId);

    CommonResponse<List<ActivityHistoryDetailsResponse>> getAllActivityHistoryDetails();

    CommonResponse<List<ActivityHistoryDetailsResponse>> getActivityHistoryDetailsById(Long activityId);

    CommonResponse<List<ActivityHistoryImageResponse>> getAllActivityHistoryImages();

    CommonResponse<List<ActivityHistoryImageResponse>> getActivityHistoryImagesById(Long activityId);

    CommonResponse<ActivityWithParamsResponse> getActivitiesWithParams(ActivityDataRequest activityDataRequest);

    CommonResponse<List<ActivityForTerminateResponse>> getActivitiesForTerminate();

    CommonResponse<TerminateResponse> terminateActivity(ActivityTerminateRequest activityTerminateRequest);

    CommonResponse<InsertResponse> insertActivity(ActivityInsertRequest activityInsertRequest);

    CommonResponse<UpdateResponse> updateActivity(ActivityUpdateRequest activityUpdateRequest);

    CommonResponse<List<ActivityIdAndNameResponse>> getTourIdsAndTourNames();

    CommonResponse<ActivityStatisticsResponse> getActivitiesStatistics();
}
