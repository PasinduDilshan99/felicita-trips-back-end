package com.felicita.repository;

import com.felicita.model.dto.*;
import com.felicita.model.request.DestinationDataRequest;
import com.felicita.model.request.DestinationInsertRequest;
import com.felicita.model.request.DestinationTerminateRequest;
import com.felicita.model.request.DestinationUpdateRequest;
import com.felicita.model.response.*;
import java.util.List;

public interface DestinationRepository {

    List<DestinationResponseDto> getAllDestinations();

    List<DestinationCategoryResponseDto> getAllDestinationsCategories();

    List<PopularDestinationResponseDto> getPopularDestinations();

    List<TrendingDestinationResponseDto> getTrendingDestinations();

    List<DestinationsForTourMapDto> getDestinationsForTourMap();

    List<DestinationResponseDto> getDestinationDetailsByTourId(Long tourId);

    List<DestinationReviewDetailsResponse> getDestinationReviewDetailsById(Long destinationId);

    List<DestinationReviewDetailsResponse> getAllDestinationsReviewDetails();

    DestinationResponseDto getDestinationDetailsById(Long destinationId);

    List<DestinationHistoryDetailsResponse> getAllDestinationHistoryDetails();

    List<DestinationHistoryDetailsResponse> getDestinationHistoryDetailsById(Long destinationId);

    List<DestinationHistoryImageResponse> getAllDestinationHistoryImages();

    List<DestinationHistoryImageResponse> getDestinationHistoryImagesById(Long destinationId);

    DestinationsWithParamsResponse getDestinationWithParams(DestinationDataRequest destinationDataRequest);

    void insertDestination(DestinationInsertRequest destinationInsertRequest, Long userId);

    void terminateDestination(DestinationTerminateRequest destinationTerminateRequest, Long userId);

    List<DestinationForTerminateResponse> getDestinationsForTerminate();

    void updateBasicDestinationDetails(DestinationUpdateRequest destinationUpdateRequest, Long userId);

    void removeDestinationImages(List<Long> removeImages, Long userId);

    void addNewImagesToDestination(List<DestinationInsertRequest.Image> newImages, Long destinationId, Long userId);

    void removeDestinationActivities(List<Long> removeActivities, Long userId);

    void addNewActivitiesToDestination(List<DestinationUpdateRequest.Activity> newActivities, Long destinationId, Long userId);

    DestinationStatisticsResponse.DestinationDetails getDestinationDetailsStatistics();

    DestinationStatisticsResponse.WishDetails getDestinationWishStatistics();

    List<DestinationStatisticsResponse.CategoryDetails> getDestinationCategoryStatistics();
}
