package com.felicita.service;

import com.felicita.model.dto.*;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import java.util.List;

public interface DestinationService {

    CommonResponse<List<DestinationResponseDto>> getAllDestinations();

    CommonResponse<List<DestinationResponseDto>> getActiveDestinations();

    CommonResponse<List<DestinationCategoryResponseDto>> getAllDestinationsCategories();

    CommonResponse<List<DestinationCategoryResponseDto>> getActiveDestinationsCategories();

    CommonResponse<List<PopularDestinationResponseDto>> getPopularDestinations();

    CommonResponse<List<PopularDestinationResponseDto>> getNewDestinations();

    CommonResponse<List<TrendingDestinationResponseDto>> getTrendingDestinations();

    CommonResponse<List<DestinationsForTourMapDto>> getDestinationsForTourMap();

    CommonResponse<List<DestinationResponseDto>> getDestinationDetailsByTourId(Long tourId);

    CommonResponse<List<DestinationReviewDetailsResponse>> getAllDestinationsReviewDetails();

    CommonResponse<List<DestinationReviewDetailsResponse>> getDestinationReviewDetailsById(Long destinationId);

    CommonResponse<DestinationResponseDto> getDestinationDetailsById(Long destinationId);

    CommonResponse<List<DestinationHistoryDetailsResponse>> getAllDestinationHistoryDetails();

    CommonResponse<List<DestinationHistoryDetailsResponse>> getDestinationHistoryDetailsById(Long destinationId);

    CommonResponse<List<DestinationHistoryImageResponse>> getAllDestinationHistoryImages();

    CommonResponse<List<DestinationHistoryImageResponse>> getDestinationHistoryImagesById(Long destinationId);

    CommonResponse<DestinationsWithParamsResponse> getDestinationWithParams(DestinationDataRequest destinationDataRequest);

    CommonResponse<InsertResponse> insertDestination(DestinationInsertRequest destinationInsertRequest);

    CommonResponse<TerminateResponse> terminateDestination(DestinationTerminateRequest destinationTerminateRequest);

    CommonResponse<List<DestinationForTerminateResponse>> getDestinationsForTerminate();

    CommonResponse<UpdateResponse> updateDestination(DestinationUpdateRequest destinationUpdateRequest);

    CommonResponse<DestinationStatisticsResponse> getDestinationsStatistics();

    CommonResponse<DestinationCategoriesStatisticsResponse> getDestinationCategoriesStatistics();

    CommonResponse<DestinationCategoryDetailsResponseDto> getDestinationsCategoryDetailsById(DestinationCategoryDetailsRequest destinationCategoryDetailsRequest);

    CommonResponse<InsertResponse> insertDestinationCategory(DestinationCategoryInsertRequest destinationCategoryInsertRequest);

    CommonResponse<UpdateResponse> updateDestinationCategory(DestinationCategoryUpdateRequest destinationCategoryUpdateRequest);

    CommonResponse<TerminateResponse> terminateDestinationCategory(DestinationCategoryTerminateRequest destinationCategoryTerminateRequest);

    CommonResponse<InsertResponse> addTrendingDestinations(TrendingDestinationInsertRequest trendingDestinationInsertRequest);

    CommonResponse<TerminateResponse> termianteTrendingDestination(TrendingDestinationTerminateRequest trendingDestinationTerminateRequest);
}
