package com.felicita.service.impl;

import com.felicita.exception.*;
import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.request.ActivityDataRequest;
import com.felicita.model.request.ActivityInsertRequest;
import com.felicita.model.request.ActivityTerminateRequest;
import com.felicita.model.request.ActivityUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.repository.ActivitiesRepository;
import com.felicita.repository.WishListRepository;
import com.felicita.service.ActivitiesService;
import com.felicita.service.CommonService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.ActivityValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesServiceImpl.class);

    private final ActivitiesRepository activitiesRepository;
    private final ActivityValidationService activityValidationService;
    private final CommonService commonService;
    private final WishListRepository wishListRepository;

    @Autowired
    public ActivitiesServiceImpl(ActivitiesRepository activitiesRepository, ActivityValidationService activityValidationService, CommonService commonService, WishListRepository wishListRepository) {
        this.activitiesRepository = activitiesRepository;
        this.activityValidationService = activityValidationService;
        this.commonService = commonService;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public CommonResponse<List<ActivityResponseDto>> getAllActivities() {
        LOGGER.info("Start fetching all activities from repository");
        try {
            List<ActivityResponseDto> activityResponses = activitiesRepository.getAllActivities();

            if (activityResponses.isEmpty()) {
                LOGGER.warn("No activities found in database");
                throw new DataNotFoundErrorExceptionHandler("No activities found");
            }

            LOGGER.info("Fetched {} activities successfully", activityResponses.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities from database");
        } finally {
            LOGGER.info("End fetching all activities from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityResponseDto>> getActiveActivities() {
        LOGGER.info("Start fetching active activities from repository");
        try {
            List<ActivityResponseDto> activityResponses = getAllActivities().getData();

            List<ActivityResponseDto> activityResponseDtoList = activityResponses.stream()
                    .filter(t -> CommonStatus.ACTIVE.name().equalsIgnoreCase(t.getStatus()))
                    .toList();

            LOGGER.info("Fetched {} active activities successfully", activityResponseDtoList.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityResponseDtoList,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active activities: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active activities from database");
        } finally {
            LOGGER.info("End fetching active activities from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityCategoryResponseDto>> getAllActivityCategories() {
        LOGGER.info("Start fetching all activity categories from repository");
        try {
            List<ActivityCategoryResponseDto> activityCategoryResponseDtos = activitiesRepository.getAllActivityCategories();

            if (activityCategoryResponseDtos.isEmpty()) {
                LOGGER.warn("No activity categories found in database");
                throw new DataNotFoundErrorExceptionHandler("No activities found");
            }

            LOGGER.info("Fetched {} activity categories successfully", activityCategoryResponseDtos.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityCategoryResponseDtos,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity categories from database");
        } finally {
            LOGGER.info("End fetching all activity categories from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityCategoryResponseDto>> getActiveActivityCategories() {
        LOGGER.info("Start fetching active activity categories from repository");
        try {
            List<ActivityCategoryResponseDto> activityCategoryResponseDtos = getAllActivityCategories().getData();

            List<ActivityCategoryResponseDto> activityCategoryResponseDtoList = activityCategoryResponseDtos.stream()
                    .filter(t -> CommonStatus.ACTIVE.name().equalsIgnoreCase(t.getCategoryStatus()))
                    .toList();

            LOGGER.info("Fetched {} active activity categories successfully", activityCategoryResponseDtoList.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityCategoryResponseDtoList,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active activity categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active activity categories from database");
        } finally {
            LOGGER.info("End fetching active activity categories from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityReviewDetailsResponse>> getAllActivityReviewDetails() {
        LOGGER.info("Start fetching all activity reviews from repository");
        try {
            List<ActivityReviewDetailsResponse> activityReviewDetailsResponses = activitiesRepository.getAllActivityReviewDetails();

            if (activityReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No activity reviews found in database");
                throw new DataNotFoundErrorExceptionHandler("No activity reviews found");
            }

            List<ActivityReviewDetailsResponse> activityReviewDetailsResponseList = activityReviewDetailsResponses.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getReviewStatus()))
                    .collect(Collectors.toList());

            LOGGER.info("Fetched {} activity reviews successfully", activityReviewDetailsResponseList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityReviewDetailsResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity reviews: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity reviews from database");
        } finally {
            LOGGER.info("End fetching activity reviews from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityReviewDetailsResponse>> getActivityReviewDetailsById(Long activityId) {
        LOGGER.info("Start fetching activity reviews by activity id : {} from repository", activityId);
        try {
            List<ActivityReviewDetailsResponse> activityReviewDetailsResponseList = activitiesRepository.getActivityReviewDetailsById(activityId);

            if (activityReviewDetailsResponseList.isEmpty()) {
                LOGGER.warn("No activity reviews by activity id : {} found in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No activity reviews by activity id : " + activityId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityReviewDetailsResponseList,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity reviews by activity id : {} , {}", activityId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity reviews by activity id : " + activityId);
        } finally {
            LOGGER.info("End fetching activity reviews by activity id : {} from repository", activityId);
        }
    }

    @Override
    public CommonResponse<ActivityResponseDto> getActivityById(Long activityId) {
        LOGGER.info("Start fetching activity details by activity id : {} from repository", activityId);
        try {
            ActivityResponseDto activityResponseDto = activitiesRepository.getActivityById(activityId);

            if (activityResponseDto == null) {
                LOGGER.warn("No activity details by activity id : {} in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No activity details by activity id : " + activityId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityResponseDto,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity details by activity id : {} , {}", activityId ,e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity details by activity id : " + activityId);
        } finally {
            LOGGER.info("End fetching activity details by activity id : {} from repository", activityId);
        }
    }

    @Override
    public CommonResponse<List<ActivityHistoryDetailsResponse>> getAllActivityHistoryDetails() {
        LOGGER.info("Start fetching activity history details from repository");
        try {
            List<ActivityHistoryDetailsResponse> activityHistoryDetailsResponses = activitiesRepository.getAllActivityHistoryDetails();

            if (activityHistoryDetailsResponses.isEmpty()) {
                LOGGER.warn("No activity history details found in database");
                throw new DataNotFoundErrorExceptionHandler("No activity history details found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityHistoryDetailsResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity history details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity history details from database");
        } finally {
            LOGGER.info("End fetching activity history details from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityHistoryDetailsResponse>> getActivityHistoryDetailsById(Long activityId) {
        LOGGER.info("Start fetching activity history details by activity id : {} from repository", activityId);
        try {
            List<ActivityHistoryDetailsResponse> activityHistoryDetailsResponses = activitiesRepository.getActivityHistoryDetailsById(activityId);

            if (activityHistoryDetailsResponses.isEmpty()) {
                LOGGER.warn("No activity history details by activity id : {} found in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No activity history details by activity id : " + activityId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityHistoryDetailsResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity history details by activity id : {} , {}", activityId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity history details by activity id : " + activityId);
        } finally {
            LOGGER.info("End fetching activity history details by activity id : {} from repository", activityId);
        }
    }

    @Override
    public CommonResponse<List<ActivityHistoryImageResponse>> getAllActivityHistoryImages() {
        LOGGER.info("Start fetching activity history images details from repository");
        try {
            List<ActivityHistoryImageResponse> activityHistoryImageResponses = activitiesRepository.getAllActivityHistoryImages();

            if (activityHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No activity history images details found in database");
                throw new DataNotFoundErrorExceptionHandler("No activity history images details found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityHistoryImageResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity history images details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity history images details from database");
        } finally {
            LOGGER.info("End fetching activity history images details from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityHistoryImageResponse>> getActivityHistoryImagesById(Long activityId) {
        LOGGER.info("Start fetching activity history images details by activity id : {} from repository", activityId);
        try {
            List<ActivityHistoryImageResponse> activityHistoryImageResponses = activitiesRepository.getActivityHistoryImagesById(activityId);

            if (activityHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No activity history images details by activity id : {} found in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No activity history images details by activity id : " + activityId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityHistoryImageResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity history images details by activity id : {} , {}", activityId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity history images details by activity id : {}" + activityId);
        } finally {
            LOGGER.info("End fetching activity history images details by activity id : {} from repository", activityId);
        }
    }

    @Override
    public CommonResponse<ActivityWithParamsResponse> getActivitiesWithParams(ActivityDataRequest activityDataRequest) {
        LOGGER.info("Start fetching all activities for request from repository");
        try {
            ActivityWithParamsResponse activityWithParamsResponse = activitiesRepository.getActivitiesWithParams(activityDataRequest);
            Long userId = commonService.getUserIdBySecurityContextWithOutException();

            Set<Long> activityIdSet = new HashSet<>();
            if (userId != null) {
                LOGGER.info("USER ID : {}, FETCHING WISHLIST ACTIVITY IDS", userId);
                List<Long> activityIds = wishListRepository.getAllActivityWishListByUserId(userId);
                if (activityIds != null) {
                    activityIdSet.addAll(activityIds);
                    LOGGER.info("USER ID : {} , WISHLIST ACTIVITY IDS : {}", userId, activityIdSet);
                }
            }

            if (activityWithParamsResponse != null) {
                List<ActivityResponseDto> activityResponseDtos = activityWithParamsResponse.getActivityResponseDtos();
                if (activityResponseDtos != null) {
                    for (ActivityResponseDto activityResponseDto : activityResponseDtos) {
                        activityResponseDto.setWish(activityIdSet.contains(activityResponseDto.getId()));
                    }
                }
            }


            if (activityWithParamsResponse == null) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                        null,
                        Instant.now()
                );
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityWithParamsResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities for request : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities for request from database");
        } finally {
            LOGGER.info("End fetching all activities for request from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityForTerminateResponse>> getActivitiesForTerminate() {
        LOGGER.info("Start fetching activities for terminate from repository");
        try {
            List<ActivityForTerminateResponse> activityForTerminateResponses =
                    activitiesRepository.getActivitiesForTerminate();

            if (activityForTerminateResponses.isEmpty()) {
                LOGGER.warn("No activities for terminate found in database");
                throw new DataNotFoundErrorExceptionHandler("No activities for terminate found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            activityForTerminateResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities for terminate : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities for terminate from database");
        } finally {
            LOGGER.info("End fetching activities for terminate from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateActivity(ActivityTerminateRequest activityTerminateRequest) {
        LOGGER.info("Start execute terminate destination request.");
        try {
            activityValidationService.validateTerminateActivityRequest(activityTerminateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            activitiesRepository.terminateActivity(activityTerminateRequest, userId);

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                            new TerminateResponse("Successfully terminate activity request"),
                            Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate activity request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertActivity(ActivityInsertRequest activityInsertRequest) {
        try {
            activityValidationService.validateActivityInsertRequest(activityInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            Long activityId = activitiesRepository.insertActivityDetails(activityInsertRequest, userId);
            activitiesRepository.insertActivityImages(activityId, activityInsertRequest.getImages(), userId);
            activitiesRepository.insertActivityRequirements(activityId, activityInsertRequest.getRequirements(), userId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert activity request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert activity request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateActivity(ActivityUpdateRequest activityUpdateRequest) {
        LOGGER.info("Start execute update activity request.");
        try {
            activityValidationService.validateActivityUpdateRequest(activityUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            activitiesRepository.updateBasicActivityDetails(activityUpdateRequest, userId);
            activitiesRepository.removeActivityImages(activityUpdateRequest.getRemoveImagesIds(), userId);
            if (!activityUpdateRequest.getAddImages().isEmpty()) {
                activitiesRepository.insertActivityImages(activityUpdateRequest.getActivityId(), activityUpdateRequest.getAddImages(), userId);
            }
            if (!activityUpdateRequest.getUpdatedImages().isEmpty()) {
                activitiesRepository.updateActivityImages(activityUpdateRequest.getActivityId(), activityUpdateRequest.getUpdatedImages(), userId);
            }
            activitiesRepository.removeRequirements(activityUpdateRequest.getRemoveRequirementsIds(), userId);
            if (!activityUpdateRequest.getAddRequirements().isEmpty()) {
                activitiesRepository.insertActivityRequirements(activityUpdateRequest.getActivityId(), activityUpdateRequest.getAddRequirements(), userId);
            }
            if (!activityUpdateRequest.getUpdatedRequirements().isEmpty()) {
                activitiesRepository.updateActivityRequirements(activityUpdateRequest.getActivityId(), activityUpdateRequest.getUpdatedRequirements(), userId);
            }
            return
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                            new UpdateResponse("Successfully update activity request", activityUpdateRequest.getActivityId()),
                            Instant.now()
                    );
        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert activity request", vfe.getValidationFailedResponses());
        } catch (UpdateFailedErrorExceptionHandler ufe) {
            throw new UpdateFailedErrorExceptionHandler(ufe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<List<ActivityIdAndNameResponse>> getTourIdsAndTourNames() {
        CommonResponse<List<ActivityForTerminateResponse>> activitiesForTerminate = getActivitiesForTerminate();
        List<ActivityIdAndNameResponse> activityIdAndNameResponses = new ArrayList<>();
        for (ActivityForTerminateResponse activityForTerminateResponse : activitiesForTerminate.getData()) {
            activityIdAndNameResponses.add(
                    new ActivityIdAndNameResponse(
                            activityForTerminateResponse.getActivityId(),
                            activityForTerminateResponse.getActivityName()
                    )
            );
        }
        return new CommonResponse<>(
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                activityIdAndNameResponses,
                Instant.now());
    }

    @Override
    public CommonResponse<ActivityStatisticsResponse> getActivitiesStatistics() {
        LOGGER.info("Start fetching activities statistics from repository");
        try {
            ActivityStatisticsResponse activityStatisticsResponse = new ActivityStatisticsResponse();
            ActivityStatisticsResponse.ActivityDetails activityDetails = activitiesRepository.getActivityDetailsStatistics();
            ActivityStatisticsResponse.WishDetails wishDetails = activitiesRepository.getActivityWishStatistics();
            List<ActivityStatisticsResponse.CategoryDetails> categoryDetails = activitiesRepository.getActivityCategoryStatistics();

            activityStatisticsResponse.setActivityDetails(activityDetails);
            activityStatisticsResponse.setWishDetails(wishDetails);
            activityStatisticsResponse.setCategoryDetails(categoryDetails);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities statistics from database");
        } finally {
            LOGGER.info("End fetching activities statistics from repository");
        }
    }

}
