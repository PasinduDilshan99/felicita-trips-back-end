package com.felicita.service;

import com.felicita.model.dto.PopularTourResponseDto;
import com.felicita.model.dto.TourResponseDto;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourDataRequest;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.request.TourUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.TourCategoryStatisticsResponse;
import com.felicita.model.response.statistics.TourScheduleStatisticsResponse;
import com.felicita.model.response.statistics.TourStatisticsResponse;
import com.felicita.model.response.statistics.TourTypeStatisticsResponse;
import com.felicita.model.response.tour.category.TourCategoryAllDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryBasicDetailsResponse;

import java.util.List;

public interface TourService {

    CommonResponse<List<TourResponseDto>> getAllTours();

    CommonResponse<List<TourResponseDto>> getActiveTours();

    CommonResponse<List<PopularTourResponseDto>> getPopularTours();

    CommonResponse<TourResponseDto> getTourDetailsById(Long tourId);

    CommonResponse<List<TourReviewDetailsResponse>> getAllTourReviewDetails();

    CommonResponse<List<TourReviewDetailsResponse>> getTourReviewDetailsById(Long tourId);

    CommonResponse<List<TourDestinationsForMapResponse>> getTourDestinationsForMap(Long tourId);

    CommonResponse<List<TourHistoryResponse>> getAllTourHistoryDetails();

    CommonResponse<List<TourHistoryResponse>> getTourHistoryDetailsById(Long tourId);

    CommonResponse<List<TourHistoryImageResponse>> getAllTourHistoryImages();

    CommonResponse<List<TourHistoryImageResponse>> getTourHistoryImagesById(Long tourId);

    CommonResponse<ToursDetailsWithParamResponse> getToursToShowWithParam(TourDataRequest tourDataRequest);

    CommonResponse<List<TourDetailsWithDayToDayResponse>> getTourDetailsDayByDay(Long tourId);

    CommonResponse<TourExtrasResponse> getTourExtraDetailsDayByDay(Long tourId);

    CommonResponse<TourSchedulesResponse> getTourSchedules(Long tourId);

    CommonResponse<List<TourBasicDetailsResponse>> getAllToursBasicDetails();

    CommonResponse<List<TourForTerminateResponse>> getToursForTerminate();

    CommonResponse<TerminateResponse> terminateTour(TourTerminateRequest tourTerminateRequest);

    CommonResponse<InsertResponse> insertTour(TourInsertRequest tourInsertRequest);

    CommonResponse<List<TourIdAndTourNameResponse>> getTourIdsAndTourNames();

    CommonResponse<TourDetailsForAddPackageResponse> getTourDetailsForAddPackage(Long tourId);

    CommonResponse<TourAllDetailsResponse> getTourAllDetailsById(Long tourId);

    CommonResponse<UpdateResponse> updateTour(TourUpdateRequest tourUpdateRequest);

    CommonResponse<TourStatisticsResponse> getTourStatistics();

    CommonResponse<TourScheduleStatisticsResponse> getTourScheduleStatistics();

    CommonResponse<TourCategoryStatisticsResponse> getTourCategoryStatistics();

    CommonResponse<TourTypeStatisticsResponse> getTourTypeStatistics();

    CommonResponse<List<TourCategoryBasicDetailsResponse>> getTourCategories();

    CommonResponse<TourCategoryAllDetailsResponse> getTourCategoryDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<TourCategoryBasicDetailsResponse> getTourCategoryBasicDetailsById(CommonIdRequest commonIdRequest);

    CommonResponse<TerminateResponse> terminateTourCategory(CommonIdRequest commonIdRequest);

    CommonResponse<InsertResponse> insertTourCategory(TourCategoryInsertRequest tourCategoryInsertRequest);

    CommonResponse<UpdateResponse> updateTourCategory(TourCategoryUpdateRequest tourCategoryUpdateRequest);
}
