package com.felicita.service.impl;

import com.felicita.email.ActivityEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.ActivityCategoryResponseDto;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.dto.activity.schedule.ActivityScheduleBasicDetailsDTO;
import com.felicita.model.enums.*;
import com.felicita.model.other.ActivitiesCategoryComparisonResult;
import com.felicita.model.other.ActivitiesComparisonResult;
import com.felicita.model.other.ActivitiesScheduleComparisonResult;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.request.activity.category.ActivityCategoryImageRequest;
import com.felicita.model.request.activity.category.ActivityCategoryImageUpdateRequest;
import com.felicita.model.request.activity.category.ActivityCategoryInsertRequest;
import com.felicita.model.request.activity.category.ActivityCategoryUpdateRequest;
import com.felicita.model.request.activity.schedule.ActivityScheduleUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.activity.category.ActivityCategoryDetailsResponse;
import com.felicita.model.response.common.SortByResponse;
import com.felicita.model.response.statistics.ActivityCategoriesStatisticsResponse;
import com.felicita.model.response.statistics.ActivityScheduleStatisticsResponse;
import com.felicita.repository.ActivitiesRepository;
import com.felicita.repository.WishListRepository;
import com.felicita.security.model.User;
import com.felicita.service.ActivitiesService;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.ActivityValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.*;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesServiceImpl.class);

    private final ActivitiesRepository activitiesRepository;
    private final ActivityValidationService activityValidationService;
    private final CommonService commonService;
    private final WishListRepository wishListRepository;
    private final ActivityEmailHelperService activityEmailHelperService;
    private final EmailService emailService;

    @Autowired
    public ActivitiesServiceImpl(ActivitiesRepository activitiesRepository, ActivityValidationService activityValidationService, CommonService commonService, WishListRepository wishListRepository, ActivityEmailHelperService activityEmailHelperService, EmailService emailService) {
        this.activitiesRepository = activitiesRepository;
        this.activityValidationService = activityValidationService;
        this.commonService = commonService;
        this.wishListRepository = wishListRepository;
        this.activityEmailHelperService = activityEmailHelperService;
        this.emailService = emailService;
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
            LOGGER.error("Error occurred while fetching activity details by activity id : {} , {}", activityId, e.getMessage(), e);
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
            User loggedUser = commonService.getLoggedUser();
            ActivityResponseDto activityResponseDto = getActivityById(activityTerminateRequest.getActivityId()).getData();

            activitiesRepository.terminateActivity(activityTerminateRequest, userId);
            activitiesRepository.termianteActivityCategories(activityTerminateRequest.getActivityId(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Activity Terminated")
                    .message("The activity '" + activityResponseDto.getName() + "' has been terminated.")
                    .actionUrl(VIEW_ACTIVITY_DETAILS + "/" + activityResponseDto.getId())
                    .actionText("View Activity")
                    .icon("ClipboardX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "activityId", activityResponseDto.getId(),
                            "activityName", activityResponseDto.getName(),
                            "status", activityResponseDto.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_TERMINATE.name())
                    .sourceModule(SourceModule.ACTIVITY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = activityEmailHelperService.buildActivityTerminateSuccessfullSubject(loggedUser, activityResponseDto);
            String body = activityEmailHelperService.buildActivityTerminateSuccessfullBody(loggedUser, activityResponseDto);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

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
        LOGGER.info("Start execute insert activity request.");

        try {
            activityValidationService.validateActivityInsertRequest(activityInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long activityId = activitiesRepository.insertActivityDetails(activityInsertRequest, userId);
            activitiesRepository.insertActivityCategories(activityId, activityInsertRequest.getCategories(), userId);
            activitiesRepository.insertActivityImages(activityId, activityInsertRequest.getImages(), userId);
            activitiesRepository.insertActivityRequirements(activityId, activityInsertRequest.getRequirements(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Activity Created")
                    .message("A new activity '" + activityInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_ACTIVITY_DETAILS + "/" + activityId)
                    .actionText("View Activity")
                    .icon("ClipboardList")
                    .color("#10B981")
                    .metadata(Map.of(
                            "activityId", activityId,
                            "activityName", activityInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_CREATE.name())
                    .sourceModule(SourceModule.ACTIVITY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (activityId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = activityEmailHelperService.buildActivityCreateSuccessfullBody(activityInsertRequest, activityId, loggedUser);
                String subject = activityEmailHelperService.buildActivityCreateSuccessfullSubject(activityInsertRequest, activityId, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

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
            User loggedUser = commonService.getLoggedUser();

            ActivityResponseDto previousActivity = getActivityById(activityUpdateRequest.getActivityId()).getData();

            activitiesRepository.updateBasicActivityDetails(activityUpdateRequest, userId);

            if (!activityUpdateRequest.getRemoveCategoryIds().isEmpty()) {
                activitiesRepository.removeActivityCategories(activityUpdateRequest.getRemoveCategoryIds(), userId);
            }
            if (!activityUpdateRequest.getAddCategories().isEmpty()) {
                activitiesRepository.insertActivityCategories(activityUpdateRequest.getActivityId(), activityUpdateRequest.getAddCategories(), userId);
            }
            if (!activityUpdateRequest.getUpdatedCategories().isEmpty()) {
                activitiesRepository.updateActivityCategories(activityUpdateRequest.getActivityId(), activityUpdateRequest.getUpdatedCategories(), userId);
            }

            if (!activityUpdateRequest.getRemoveImagesIds().isEmpty()) {
                activitiesRepository.removeActivityImages(activityUpdateRequest.getRemoveImagesIds(), userId);
            }

            if (!activityUpdateRequest.getAddImages().isEmpty()) {
                activitiesRepository.insertActivityImages(activityUpdateRequest.getActivityId(), activityUpdateRequest.getAddImages(), userId);
            }
            if (!activityUpdateRequest.getUpdatedImages().isEmpty()) {
                activitiesRepository.updateActivityImages(activityUpdateRequest.getActivityId(), activityUpdateRequest.getUpdatedImages(), userId);
            }

            if (!activityUpdateRequest.getRemoveRequirementsIds().isEmpty()) {
                activitiesRepository.removeRequirements(activityUpdateRequest.getRemoveRequirementsIds(), userId);
            }

            if (!activityUpdateRequest.getAddRequirements().isEmpty()) {
                activitiesRepository.insertActivityRequirements(activityUpdateRequest.getActivityId(), activityUpdateRequest.getAddRequirements(), userId);
            }
            if (!activityUpdateRequest.getUpdatedRequirements().isEmpty()) {
                activitiesRepository.updateActivityRequirements(activityUpdateRequest.getActivityId(), activityUpdateRequest.getUpdatedRequirements(), userId);
            }

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Activity Updated")
                    .message("The activity '" + activityUpdateRequest.getName() + "' has been updated.")
                    .actionUrl(VIEW_ACTIVITY_DETAILS + "/" + activityUpdateRequest.getActivityId())
                    .actionText("View Activity")
                    .icon("ClipboardEdit")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "activityId", activityUpdateRequest.getActivityId(),
                            "activityName", activityUpdateRequest.getName(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_UPDATE.name())
                    .sourceModule(SourceModule.ACTIVITY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            ActivitiesComparisonResult comparisonResult = compareActivitiesUpdates(
                    activityUpdateRequest,
                    previousActivity
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = activityEmailHelperService.buildActivityUpdateSuccessfullSubject(loggedUser, activityUpdateRequest.getActivityId());
            String body = activityEmailHelperService.buildActivityUpdateSuccessfullBody(loggedUser, activityUpdateRequest.getActivityId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update activity request", activityUpdateRequest.getActivityId()),
                    Instant.now());

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

    @Override
    public CommonResponse<ActivityScheduleStatisticsResponse> getActivitiesScheduleStatistics() {
        LOGGER.info("Start fetching activities schedule statistics from repository");
        try {
            ActivityScheduleStatisticsResponse activityScheduleStatisticsResponse = new ActivityScheduleStatisticsResponse();
            ActivityScheduleStatisticsResponse.Summary summary = activitiesRepository.getActivitySchduleSummeryStatsitics();
            List<ActivityScheduleStatisticsResponse.ActivityParticipationTrend> activityParticipationTrends = activitiesRepository.getActivityParticipationTrendsStatsitics();
            List<ActivityScheduleStatisticsResponse.PopularActivity> popularActivities = activitiesRepository.getPopularActivitiesStatsitics();
            List<ActivityScheduleStatisticsResponse.ActivityRatingOverview> activityRatingOverviews = activitiesRepository.getActivityRatingOverviewStatsitics();
            List<ActivityScheduleStatisticsResponse.ScheduleTimeline> scheduleTimeline = activitiesRepository.getScheduleTimelineStatsitics();
            List<ActivityScheduleStatisticsResponse.ActivityStatusDistribution> activityStatusDistribution = activitiesRepository.getActivityStatusDistributionStatsitics();

            activityScheduleStatisticsResponse.setSummary(summary);
            activityScheduleStatisticsResponse.setParticipationTrends(activityParticipationTrends);
            activityScheduleStatisticsResponse.setPopularActivities(popularActivities);
            activityScheduleStatisticsResponse.setActivityRatings(activityRatingOverviews);
            activityScheduleStatisticsResponse.setScheduleTimelines(scheduleTimeline);
            activityScheduleStatisticsResponse.setStatusDistributions(activityStatusDistribution);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityScheduleStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities schedule statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities schedule statistics from database");
        } finally {
            LOGGER.info("End fetching activities schedule statistics from repository");
        }
    }

    @Override
    public CommonResponse<ActivityCategoriesStatisticsResponse> getActivityCategoriesStatistics() {
        LOGGER.info("Start fetching activity categories statistics from repository");
        try {
            ActivityCategoriesStatisticsResponse activityCategoriesStatisticsResponse = new ActivityCategoriesStatisticsResponse();
            ActivityCategoriesStatisticsResponse.Summary summary = activitiesRepository.getActivitySummeryStatistics();
            List<ActivityCategoriesStatisticsResponse.CategoryActivityCount> categoryActivityCounts = activitiesRepository.getCategoryActivityCountStatistics();
            List<ActivityCategoriesStatisticsResponse.CategoryParticipationPerformance> categoryParticipationPerformances = activitiesRepository.getCategoryParticipationPerformanceStatistics();
            List<ActivityCategoriesStatisticsResponse.CategoryRatingOverview> categoryRatingOverviews = activitiesRepository.getCategoryRatingOverviewStatistics();
            List<ActivityCategoriesStatisticsResponse.CategoryDistribution> categoryDistributions = activitiesRepository.getCategoryDistributionStatistics();
            List<ActivityCategoriesStatisticsResponse.CategoryPrimarySecondaryUsage> categoryPrimarySecondaryUsages = activitiesRepository.getCategoryPrimarySecondaryUsageStatistics();

            activityCategoriesStatisticsResponse.setSummary(summary);
            activityCategoriesStatisticsResponse.setCategoryActivityCounts(categoryActivityCounts);
            activityCategoriesStatisticsResponse.setCategoryParticipationPerformances(categoryParticipationPerformances);
            activityCategoriesStatisticsResponse.setCategoryRatingOverviews(categoryRatingOverviews);
            activityCategoriesStatisticsResponse.setCategoryDistributions(categoryDistributions);
            activityCategoriesStatisticsResponse.setCategoryPrimarySecondaryUsages(categoryPrimarySecondaryUsages);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityCategoriesStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity categories statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity categories statistics from database");
        } finally {
            LOGGER.info("End fetching activity categories statistics from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityBasicDetailsResponse>> getActivityByDestinationId(ActivitiesByDestinationId activitiesByDestinationId) {
        LOGGER.info("Start fetching activities by destination id from repository");
        try {
            List<ActivityBasicDetailsResponse> activityBasicDetailsResponses = activitiesRepository.getActivityByDestinationId(activitiesByDestinationId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityBasicDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities basic details from database");
        } finally {
            LOGGER.info("End fetching activities basic details from repository");
        }
    }

    @Override
    public CommonResponse<ActivityScheduleWithParamsResponse> getActivitiesScheduleWithParams(ActivityScheduleDataRequest activityScheduleDataRequest) {
        LOGGER.info("Start fetching activities by destination id from repository");
        try {
            ActivityScheduleWithParamsResponse activityScheduleWithParamsResponses = activitiesRepository.getActivitiesScheduleWithParams(activityScheduleDataRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityScheduleWithParamsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities basic details from database");
        } finally {
            LOGGER.info("End fetching activities basic details from repository");
        }
    }

    @Override
    public CommonResponse<ActivityScheduleParamsResponse> getActivitiesScheduleParams() {
        LOGGER.info("Start fetching activities by destination id from repository");
        try {

            ActivityScheduleParamsResponse activityScheduleParamsResponse = new ActivityScheduleParamsResponse();
            List<String> activityDurations = activitiesRepository.getDistinctActivityDurations();
            activityScheduleParamsResponse.setDurations(activityDurations);
            activityScheduleParamsResponse.setActivityIdAndNameResponses(commonService.getActivityIdAndNameResponses());
            activityScheduleParamsResponse.setDestinationIdAndNameResponses(commonService.getDestinationIdAndNameResponses());
            activityScheduleParamsResponse.setTourScheduleIdAndNameResponses(commonService.getTourScheduleIdAndNameResponses());
            activityScheduleParamsResponse.setPackageScheduleIdAndNameResponses(commonService.getPackageScheduleIdAndNameResponses());
            activityScheduleParamsResponse.setSeasonIdAndNameResponses(commonService.getSeasonIdAndNameResponses());

            List<SortByResponse> sortByResponses = List.of(
                    SortByResponse.builder()
                            .sortByDisplayName("Activity Name")
                            .sortBy("activityName")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Activity Schedule Name")
                            .sortBy("activityScheduleName")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Destination Name")
                            .sortBy("destinationName")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Duration Hours")
                            .sortBy("durationHours")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Season")
                            .sortBy("season")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Status")
                            .sortBy("status")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Schedule Assume Start Date")
                            .sortBy("scheduleAssumeStartDate")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Schedule Assume End Date")
                            .sortBy("scheduleAssumeEndDate")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Created At")
                            .sortBy("createdAt")
                            .build(),

                    SortByResponse.builder()
                            .sortByDisplayName("Updated At")
                            .sortBy("updatedAt")
                            .build()
            );

            activityScheduleParamsResponse.setSortByResponses(sortByResponses);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityScheduleParamsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activities basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activities basic details from database");
        } finally {
            LOGGER.info("End fetching activities basic details from repository");
        }
    }

    @Override
    public CommonResponse<ActivityScheduleDetailsResponse> getActivityScheduleDetailsById(CommonIdRequest activityScheduleId) {
        LOGGER.info("Start fetching activity schedule details by id from repository");
        try {
            ActivityScheduleDetailsResponse activityScheduleDetailsResponse = activitiesRepository.getActivityScheduleDetailsById(activityScheduleId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityScheduleDetailsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity schedule details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity schedule details from database");
        } finally {
            LOGGER.info("End fetching activity schedule details from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createActivitySchedule(ActivityScheduleInsertRequest activityScheduleInsertRequest) {
        LOGGER.info("Start creating activity schedule from repository");
        try {
            activityValidationService.validateActivityScheduleInsertRequest(activityScheduleInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long activityScheduleId = activitiesRepository.createActivitySchedule(activityScheduleInsertRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_SCHEDULE_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Activity Schedule Created")
                    .message("A new activity schedule '" + activityScheduleInsertRequest.getActivityScheduleName() + "' has been created.")
                    .actionUrl(VIEW_ACTIVITY_SCHEDULE_DETAILS + "/" + activityScheduleId)
                    .actionText("View Activity Schedule")
                    .icon("CalendarPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "activityScheduleId", activityScheduleId,
                            "activityScheduleName", activityScheduleInsertRequest.getActivityScheduleName(),
                            "activityId", activityScheduleInsertRequest.getActivityId(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_SCHEDULE_CREATE.name())
                    .sourceModule(SourceModule.ACTIVITY_SCHEDULE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (activityScheduleId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_SCHEDULE_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = activityEmailHelperService.buildActivityScheduleCreateSuccessfullBody(activityScheduleId, activityScheduleInsertRequest, loggedUser);
                String subject = activityEmailHelperService.buildActivityScheduleCreateSuccessfullSubject(activityScheduleId, activityScheduleInsertRequest, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Activity Schedule created successfully " + activityScheduleId),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating activity schedule: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to create activity schedule in database");
        } finally {
            LOGGER.info("End creating activity schedule from repository");
        }
    }

    public CommonResponse<ActivityScheduleBasicDetailsDTO> getActivityScheduleBasicDetails(Long packageScheduleId) {
        LOGGER.info("Start fetching activity schedule basic details by package schedule id from repository");
        try {
            ActivityScheduleBasicDetailsDTO activityScheduleBasicDetailsDTO = activitiesRepository.getActivityScheduleBasicDetails(packageScheduleId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityScheduleBasicDetailsDTO,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity schedule basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity schedule basic details from database");
        } finally {
            LOGGER.info("End fetching activity schedule basic details from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateActivitySchedule(ActivityScheduleUpdateRequest activityScheduleUpdateRequest) {
        LOGGER.info("Start updating activity schedule from repository");
        try {
            activityValidationService.validateActivityScheduleUpdateRequest(activityScheduleUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            ActivityScheduleBasicDetailsDTO previousActivityScheduleBasicDetails =
                    getActivityScheduleBasicDetails(activityScheduleUpdateRequest.getActivityScheduleId()).getData();

            activitiesRepository.updateActivitySchedule(activityScheduleUpdateRequest);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_SCHEDULE_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Activity Schedule Updated")
                    .message("The activity schedule '" + activityScheduleUpdateRequest.getActivityScheduleName() + "' has been updated.")
                    .actionUrl(VIEW_ACTIVITY_SCHEDULE_DETAILS + "/" + activityScheduleUpdateRequest.getActivityScheduleId())
                    .actionText("View Activity Schedule")
                    .icon("CalendarClock")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "activityScheduleId", activityScheduleUpdateRequest.getActivityScheduleId(),
                            "activityScheduleName", activityScheduleUpdateRequest.getActivityScheduleName(),
                            "activityId", activityScheduleUpdateRequest.getActivityId(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_SCHEDULE_UPDATE.name())
                    .sourceModule(SourceModule.ACTIVITY_SCHEDULE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            ActivitiesScheduleComparisonResult comparisonResult = compareActivitiesScheduleUpdates(
                    activityScheduleUpdateRequest,
                    previousActivityScheduleBasicDetails,
                    loggedUser
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_SCHEDULE_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = activityEmailHelperService.buildActivityScheduleUpdateSuccessfullSubject(loggedUser, activityScheduleUpdateRequest);
            String body = activityEmailHelperService.buildActivityScheduleUpdateSuccessfullBody(loggedUser, activityScheduleUpdateRequest, comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully updated", activityScheduleUpdateRequest.getActivityId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating activity schedule: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update activity schedule in database");
        } finally {
            LOGGER.info("End updating activity schedule from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> termianteActivityScheduleById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start terminating activity schedule by id from repository");
        try {
            activityValidationService.validateActivityScheduleTerminateRequest(commonIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            ActivityScheduleBasicDetailsDTO activityScheduleResponse = getActivityScheduleBasicDetails(commonIdRequest.getId()).getData();
            activitiesRepository.terminateActivityScheduleById(commonIdRequest);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_SCHEDULE_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Activity Schedule Terminated")
                    .message("The activity schedule '" + activityScheduleResponse.getActivityScheduleName() + "' has been terminated.")
                    .actionUrl(VIEW_ACTIVITY_SCHEDULE_DETAILS + "/" + activityScheduleResponse.getActivityScheduleId())
                    .actionText("View Activity Schedule")
                    .icon("CalendarX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "activityScheduleId", activityScheduleResponse.getActivityScheduleId(),
                            "activityScheduleName", activityScheduleResponse.getActivityScheduleName(),
                            "activityId", activityScheduleResponse.getActivityId(),
                            "status", activityScheduleResponse.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_SCHEDULE_TERMINATE.name())
                    .sourceModule(SourceModule.ACTIVITY_SCHEDULE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_SCHEDULE_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = activityEmailHelperService.buildActivityScheduleTerminateSuccessfullSubject(loggedUser, activityScheduleResponse);
            String body = activityEmailHelperService.buildActivityScheduleTerminateSuccessfullBody(loggedUser, activityScheduleResponse);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Activity Schedule terminated successfully"),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | UpdateFailedErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating activity schedule: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate activity schedule in database");
        } finally {
            LOGGER.info("End terminating activity schedule from repository");
        }
    }

    @Override
    public CommonResponse<ActivityCategoryDetailsResponse> getActivityCategoryDetailsById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start fetching activity category details by id from repository");
        try {
            ActivityCategoryDetailsResponse activityCategoryDetailsResponse = activitiesRepository.getActivityCategoryDetailsById(commonIdRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityCategoryDetailsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching activity category details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity category details from database");
        } finally {
            LOGGER.info("End fetching activity category details from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateActivityCategory(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start terminating activity category by id from repository");
        try {
            activityValidationService.validateCommonIdRequest(commonIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            ActivityCategoryDetailsResponse activityCategoryResponse = getActivityCategoryDetailsById(commonIdRequest).getData();
            activitiesRepository.terminateActivityCategory(commonIdRequest);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_CATEGORY_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Activity Category Terminated")
                    .message("The activity category '" + activityCategoryResponse.getCategoryName() + "' has been terminated.")
                    .actionUrl(VIEW_ACTIVITY_CATEGORY_DETAILS + "/" + activityCategoryResponse.getCategoryId())
                    .actionText("View Activity Category")
                    .icon("FolderX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "activityCategoryId", activityCategoryResponse.getCategoryId(),
                            "activityCategoryName", activityCategoryResponse.getCategoryName(),
                            "status", activityCategoryResponse.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_CATEGORY_TERMINATE.name())
                    .sourceModule(SourceModule.ACTIVITY_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_CATEGORY_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = activityEmailHelperService.buildActivityCategoryTerminateSuccessfullSubject(loggedUser, activityCategoryResponse);
            String body = activityEmailHelperService.buildActivityCategoryTerminateSuccessfullBody(loggedUser, activityCategoryResponse);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Activity Category terminated successfully"),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating activity category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate activity category in database");
        } finally {
            LOGGER.info("End terminating activity category from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertActivityCategory(ActivityCategoryInsertRequest activityCategoryInsertRequest) {
        LOGGER.info("Start inserting activity category from repository");
        try {
            activityValidationService.validateActivityCategoryInsertRequest(activityCategoryInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long activityCategoryId = activitiesRepository.insertActivityCategoryBasicDetails(activityCategoryInsertRequest);
            activitiesRepository.insertActivityCategoryImages(activityCategoryId, activityCategoryInsertRequest.getImages());
            activitiesRepository.addActivityCategoryForActivities(activityCategoryId,activityCategoryInsertRequest.getActivityIds());

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_CATEGORY_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Activity Category Created")
                    .message("A new activity category '" + activityCategoryInsertRequest.getCategoryName() + "' has been created.")
                    .actionUrl(VIEW_ACTIVITY_CATEGORY_DETAILS + "/" + activityCategoryId)
                    .actionText("View Activity Category")
                    .icon("FolderPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "activityCategoryId", activityCategoryId,
                            "activityCategoryName", activityCategoryInsertRequest.getCategoryName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_CATEGORY_CREATE.name())
                    .sourceModule(SourceModule.ACTIVITY_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (activityCategoryId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_CATEGORY_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = activityEmailHelperService.buildActivityCategoryCreateSuccessfullBody(activityCategoryId, activityCategoryInsertRequest, loggedUser);
                String subject = activityEmailHelperService.buildActivityCategoryCreateSuccessfullSubject(activityCategoryId, activityCategoryInsertRequest, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Activity Category created successfully " + activityCategoryId),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while inserting activity category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to insert activity category in database");
        } finally {
            LOGGER.info("End inserting activity category from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateActivityCategory(ActivityCategoryUpdateRequest activityCategoryUpdateRequest) {
        LOGGER.info("Start updating activity category from repository");
        try {
            activityValidationService.validateActivityCategoryUpdateRequest(activityCategoryUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            ActivityCategoryDetailsResponse previousActivityCategoryResponse =
                    getActivityCategoryDetailsById(new CommonIdRequest(activityCategoryUpdateRequest.getCategoryId())).getData();

            activitiesRepository.updateActivityCategorybasicDetails(activityCategoryUpdateRequest);
            activitiesRepository.addActivityCategoryForActivities(activityCategoryUpdateRequest.getCategoryId(), activityCategoryUpdateRequest.getAddActivityIds());
            activitiesRepository.removeActivityCategoryForActivities(activityCategoryUpdateRequest.getCategoryId(), activityCategoryUpdateRequest.getRemoveActivityIds());

            activitiesRepository.insertActivityCategoryImages(activityCategoryUpdateRequest.getCategoryId(), activityCategoryUpdateRequest.getAddImages());
            activitiesRepository.removeActivityCategoryImages(activityCategoryUpdateRequest.getCategoryId(), activityCategoryUpdateRequest.getRemoveImageIds());
            activitiesRepository.updateActivityCategoryImages(activityCategoryUpdateRequest.getCategoryId(), activityCategoryUpdateRequest.getUpdateImages());

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ACTIVITY_CATEGORY_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Activity Category Updated")
                    .message("The activity category '" + previousActivityCategoryResponse.getCategoryName() + "' has been updated.")
                    .actionUrl(VIEW_ACTIVITY_CATEGORY_DETAILS + "/" + previousActivityCategoryResponse.getCategoryId())
                    .actionText("View Activity Category")
                    .icon("FolderEdit")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "activityCategoryId", previousActivityCategoryResponse.getCategoryId(),
                            "activityCategoryName", previousActivityCategoryResponse.getCategoryName(),
                            "status", activityCategoryUpdateRequest.getStatus(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ACTIVITY_CATEGORY_UPDATE.name())
                    .sourceModule(SourceModule.ACTIVITY_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            ActivitiesCategoryComparisonResult comparisonResult = compareActivitiesCategoryUpdates(
                    previousActivityCategoryResponse,
                    activityCategoryUpdateRequest,
                    loggedUser
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_CATEGORY_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = activityEmailHelperService.buildActivityCategoryUpdateSuccessfullSubject(loggedUser, activityCategoryUpdateRequest);
            String body = activityEmailHelperService.buildActivityCategoryUpdateSuccessfullBody(loggedUser, activityCategoryUpdateRequest, comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully updated", activityCategoryUpdateRequest.getCategoryId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating activity category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update activity category in database");
        } finally {
            LOGGER.info("End updating activity category from repository");
        }
    }

    private ActivitiesCategoryComparisonResult compareActivitiesCategoryUpdates(
            ActivityCategoryDetailsResponse previousActivityCategoryResponse,
            ActivityCategoryUpdateRequest activityCategoryUpdateRequest,
            User loggedUser) {

        ActivitiesCategoryComparisonResult.ActivitiesCategoryComparisonResultBuilder resultBuilder =
                ActivitiesCategoryComparisonResult.builder();

        List<ActivitiesCategoryComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        boolean hasChanges = false;
        List<String> warnings = new ArrayList<>();

        // Compare categoryName
        if (activityCategoryUpdateRequest.getCategoryName() != null &&
                previousActivityCategoryResponse.getCategoryName() != null &&
                !activityCategoryUpdateRequest.getCategoryName().equals(previousActivityCategoryResponse.getCategoryName())) {
            changes.add(String.format("Category Name changed from '%s' to '%s'",
                    previousActivityCategoryResponse.getCategoryName(),
                    activityCategoryUpdateRequest.getCategoryName()));
            fieldChanges.add(new ActivitiesCategoryComparisonResult.FieldChange(
                    "categoryName",
                    previousActivityCategoryResponse.getCategoryName(),
                    activityCategoryUpdateRequest.getCategoryName(),
                    "Category Name"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(activityCategoryUpdateRequest.getDescription(), previousActivityCategoryResponse.getDescription())) {
            changes.add(String.format("Description changed from '%s' to '%s'",
                    previousActivityCategoryResponse.getDescription(),
                    activityCategoryUpdateRequest.getDescription()));
            fieldChanges.add(new ActivitiesCategoryComparisonResult.FieldChange(
                    "description",
                    previousActivityCategoryResponse.getDescription(),
                    activityCategoryUpdateRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare color
        if (activityCategoryUpdateRequest.getColor() != null &&
                previousActivityCategoryResponse.getColor() != null &&
                !activityCategoryUpdateRequest.getColor().equals(previousActivityCategoryResponse.getColor())) {
            changes.add(String.format("Color changed from '%s' to '%s'",
                    previousActivityCategoryResponse.getColor(),
                    activityCategoryUpdateRequest.getColor()));
            fieldChanges.add(new ActivitiesCategoryComparisonResult.FieldChange(
                    "color",
                    previousActivityCategoryResponse.getColor(),
                    activityCategoryUpdateRequest.getColor(),
                    "Color"));
            hasChanges = true;
        }

        // Compare hoverColor
        if (activityCategoryUpdateRequest.getHoverColor() != null &&
                previousActivityCategoryResponse.getHoverColor() != null &&
                !activityCategoryUpdateRequest.getHoverColor().equals(previousActivityCategoryResponse.getHoverColor())) {
            changes.add(String.format("Hover Color changed from '%s' to '%s'",
                    previousActivityCategoryResponse.getHoverColor(),
                    activityCategoryUpdateRequest.getHoverColor()));
            fieldChanges.add(new ActivitiesCategoryComparisonResult.FieldChange(
                    "hoverColor",
                    previousActivityCategoryResponse.getHoverColor(),
                    activityCategoryUpdateRequest.getHoverColor(),
                    "Hover Color"));
            hasChanges = true;
        }

        // Compare status
        String oldStatus = previousActivityCategoryResponse.getStatus();
        String newStatus = activityCategoryUpdateRequest.getStatus();
        if (oldStatus != null && newStatus != null && !oldStatus.equals(newStatus)) {
            changes.add(String.format("Status changed from '%s' to '%s'", oldStatus, newStatus));
            fieldChanges.add(new ActivitiesCategoryComparisonResult.FieldChange(
                    "status",
                    oldStatus,
                    newStatus,
                    "Status"));
            hasChanges = true;
        }

        // Handle activities to remove
        List<Long> activitiesToRemove = new ArrayList<>();
        if (activityCategoryUpdateRequest.getRemoveActivityIds() != null &&
                !activityCategoryUpdateRequest.getRemoveActivityIds().isEmpty()) {
            activitiesToRemove.addAll(activityCategoryUpdateRequest.getRemoveActivityIds());
            changes.add(String.format("Activities to remove: %s",
                    activityCategoryUpdateRequest.getRemoveActivityIds()));
            hasChanges = true;
        }

        // Handle activities to add
        List<Long> activitiesToAdd = new ArrayList<>();
        if (activityCategoryUpdateRequest.getAddActivityIds() != null &&
                !activityCategoryUpdateRequest.getAddActivityIds().isEmpty()) {
            activitiesToAdd.addAll(activityCategoryUpdateRequest.getAddActivityIds());
            changes.add(String.format("Activities to add: %s",
                    activityCategoryUpdateRequest.getAddActivityIds()));
            hasChanges = true;
        }

        // Validate activity changes
        if (!activitiesToRemove.isEmpty() && !activitiesToAdd.isEmpty()) {
            // Check for conflicts (same activity in both lists)
            List<Long> conflicts = activitiesToRemove.stream()
                    .filter(activitiesToAdd::contains)
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                warnings.add(String.format("Warning: Activities %s are both being added and removed!", conflicts));
            }
        }

        // Handle images to add
        List<ActivitiesCategoryComparisonResult.ImageChange> imagesToAdd = new ArrayList<>();
        if (activityCategoryUpdateRequest.getAddImages() != null &&
                !activityCategoryUpdateRequest.getAddImages().isEmpty()) {
            for (ActivityCategoryImageRequest imageRequest : activityCategoryUpdateRequest.getAddImages()) {
                ActivitiesCategoryComparisonResult.ImageChange imageChange =
                        ActivitiesCategoryComparisonResult.ImageChange.builder()
                                .name(imageRequest.getName())
                                .description(imageRequest.getDescription())
                                .imageUrl(imageRequest.getImageUrl())
                                .status(imageRequest.getStatus())
                                .build();
                imagesToAdd.add(imageChange);
                changes.add(String.format("Image to add: %s", imageRequest.getName()));
            }
            hasChanges = true;
        }

        // Handle images to remove
        List<Long> imagesToRemove = new ArrayList<>();
        if (activityCategoryUpdateRequest.getRemoveImageIds() != null &&
                !activityCategoryUpdateRequest.getRemoveImageIds().isEmpty()) {
            imagesToRemove.addAll(activityCategoryUpdateRequest.getRemoveImageIds());
            changes.add(String.format("Images to remove IDs: %s",
                    activityCategoryUpdateRequest.getRemoveImageIds()));
            hasChanges = true;
        }

        // Handle images to update
        List<ActivitiesCategoryComparisonResult.ImageUpdateChange> imagesToUpdate = new ArrayList<>();
        if (activityCategoryUpdateRequest.getUpdateImages() != null &&
                !activityCategoryUpdateRequest.getUpdateImages().isEmpty()) {

            // Find existing images in previous response for comparison
            Map<Long, ActivityCategoryDetailsResponse.CategoryImage> existingImagesMap = new HashMap<>();
            if (previousActivityCategoryResponse.getImages() != null) {
                existingImagesMap = previousActivityCategoryResponse.getImages().stream()
                        .collect(Collectors.toMap(
                                ActivityCategoryDetailsResponse.CategoryImage::getImageId,
                                image -> image
                        ));
            }

            for (ActivityCategoryImageUpdateRequest updateRequest : activityCategoryUpdateRequest.getUpdateImages()) {
                ActivityCategoryDetailsResponse.CategoryImage existingImage =
                        existingImagesMap.get(updateRequest.getImageId());

                if (existingImage != null) {
                    ActivitiesCategoryComparisonResult.ImageUpdateChange imageUpdateChange =
                            ActivitiesCategoryComparisonResult.ImageUpdateChange.builder()
                                    .imageId(updateRequest.getImageId())
                                    .oldName(existingImage.getName())
                                    .newName(updateRequest.getName())
                                    .oldDescription(existingImage.getDescription())
                                    .newDescription(updateRequest.getDescription())
                                    .oldImageUrl(existingImage.getImageUrl())
                                    .newImageUrl(updateRequest.getImageUrl())
                                    .oldStatus(existingImage.getStatus())
                                    .newStatus(updateRequest.getStatus())
                                    .build();
                    imagesToUpdate.add(imageUpdateChange);
                    changes.add(String.format("Image to update ID %d: %s -> %s",
                            updateRequest.getImageId(), existingImage.getName(), updateRequest.getName()));
                    hasChanges = true;
                } else {
                    warnings.add(String.format("Warning: Image with ID %d not found for update",
                            updateRequest.getImageId()));
                }
            }
        }

        // Additional warnings for image operations
        if (!imagesToAdd.isEmpty() && imagesToAdd.size() > 5) {
            warnings.add("Warning: Adding more than 5 images at once might impact performance");
        }

        if (!imagesToRemove.isEmpty() && !imagesToAdd.isEmpty()) {
            warnings.add("Warning: Adding and removing images in the same operation");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .activitiesToAdd(activitiesToAdd)
                .activitiesToRemove(activitiesToRemove)
                .imagesToAdd(imagesToAdd)
                .imagesToRemove(imagesToRemove)
                .imagesToUpdate(imagesToUpdate)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(loggedUser != null ?
                        loggedUser.getFirstName() + " " + loggedUser.getLastName() : "Unknown")
                .changedByUserId(loggedUser != null ? loggedUser.getId() : null)
                .changeTimestamp(new Date().toString())
                .build();
    }

    private ActivitiesScheduleComparisonResult compareActivitiesScheduleUpdates(
            ActivityScheduleUpdateRequest activityScheduleUpdateRequest,
            ActivityScheduleBasicDetailsDTO previousActivityScheduleBasicDetails,
            User loggedUser) {

        ActivitiesScheduleComparisonResult.ActivitiesScheduleComparisonResultBuilder resultBuilder =
                ActivitiesScheduleComparisonResult.builder();

        List<String> changes = new ArrayList<>();
        List<ActivitiesScheduleComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        boolean hasChanges = false;

        // Compare activityScheduleId
        if (activityScheduleUpdateRequest.getActivityScheduleId() != null &&
                previousActivityScheduleBasicDetails.getActivityScheduleId() != null &&
                !activityScheduleUpdateRequest.getActivityScheduleId().equals(previousActivityScheduleBasicDetails.getActivityScheduleId())) {
            changes.add(String.format("Activity Schedule ID changed from %d to %d",
                    previousActivityScheduleBasicDetails.getActivityScheduleId(),
                    activityScheduleUpdateRequest.getActivityScheduleId()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "activityScheduleId",
                    previousActivityScheduleBasicDetails.getActivityScheduleId(),
                    activityScheduleUpdateRequest.getActivityScheduleId(),
                    "Activity Schedule ID"));
            hasChanges = true;
        }

        // Compare activityScheduleName
        if (activityScheduleUpdateRequest.getActivityScheduleName() != null &&
                !activityScheduleUpdateRequest.getActivityScheduleName().equals(previousActivityScheduleBasicDetails.getActivityScheduleName())) {
            changes.add(String.format("Activity Schedule Name changed from '%s' to '%s'",
                    previousActivityScheduleBasicDetails.getActivityScheduleName(),
                    activityScheduleUpdateRequest.getActivityScheduleName()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "activityScheduleName",
                    previousActivityScheduleBasicDetails.getActivityScheduleName(),
                    activityScheduleUpdateRequest.getActivityScheduleName(),
                    "Activity Schedule Name"));
            hasChanges = true;
        }

        // Compare activityId
        if (activityScheduleUpdateRequest.getActivityId() != null &&
                previousActivityScheduleBasicDetails.getActivityId() != null &&
                !activityScheduleUpdateRequest.getActivityId().equals(previousActivityScheduleBasicDetails.getActivityId())) {
            changes.add(String.format("Activity ID changed from %d to %d",
                    previousActivityScheduleBasicDetails.getActivityId(),
                    activityScheduleUpdateRequest.getActivityId()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "activityId",
                    previousActivityScheduleBasicDetails.getActivityId(),
                    activityScheduleUpdateRequest.getActivityId(),
                    "Activity ID"));
            hasChanges = true;
        }

        // Compare assumeStartDate
        if (activityScheduleUpdateRequest.getAssumeStartDate() != null &&
                previousActivityScheduleBasicDetails.getAssumeStartDate() != null &&
                !activityScheduleUpdateRequest.getAssumeStartDate().equals(previousActivityScheduleBasicDetails.getAssumeStartDate())) {
            changes.add(String.format("Assume Start Date changed from %s to %s",
                    previousActivityScheduleBasicDetails.getAssumeStartDate(),
                    activityScheduleUpdateRequest.getAssumeStartDate()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "assumeStartDate",
                    previousActivityScheduleBasicDetails.getAssumeStartDate(),
                    activityScheduleUpdateRequest.getAssumeStartDate(),
                    "Assume Start Date"));
            hasChanges = true;
        }

        // Compare assumeEndDate
        if (activityScheduleUpdateRequest.getAssumeEndDate() != null &&
                previousActivityScheduleBasicDetails.getAssumeEndDate() != null &&
                !activityScheduleUpdateRequest.getAssumeEndDate().equals(previousActivityScheduleBasicDetails.getAssumeEndDate())) {
            changes.add(String.format("Assume End Date changed from %s to %s",
                    previousActivityScheduleBasicDetails.getAssumeEndDate(),
                    activityScheduleUpdateRequest.getAssumeEndDate()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "assumeEndDate",
                    previousActivityScheduleBasicDetails.getAssumeEndDate(),
                    activityScheduleUpdateRequest.getAssumeEndDate(),
                    "Assume End Date"));
            hasChanges = true;
        }

        // Compare durationHoursStart
        if (activityScheduleUpdateRequest.getDurationHoursStart() != null &&
                previousActivityScheduleBasicDetails.getDurationHoursStart() != null &&
                !activityScheduleUpdateRequest.getDurationHoursStart().equals(previousActivityScheduleBasicDetails.getDurationHoursStart())) {
            changes.add(String.format("Duration Hours Start changed from %.2f to %.2f",
                    previousActivityScheduleBasicDetails.getDurationHoursStart(),
                    activityScheduleUpdateRequest.getDurationHoursStart()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "durationHoursStart",
                    previousActivityScheduleBasicDetails.getDurationHoursStart(),
                    activityScheduleUpdateRequest.getDurationHoursStart(),
                    "Duration Hours Start"));
            hasChanges = true;
        }

        // Compare durationHoursEnd
        if (activityScheduleUpdateRequest.getDurationHoursEnd() != null &&
                previousActivityScheduleBasicDetails.getDurationHoursEnd() != null &&
                !activityScheduleUpdateRequest.getDurationHoursEnd().equals(previousActivityScheduleBasicDetails.getDurationHoursEnd())) {
            changes.add(String.format("Duration Hours End changed from %.2f to %.2f",
                    previousActivityScheduleBasicDetails.getDurationHoursEnd(),
                    activityScheduleUpdateRequest.getDurationHoursEnd()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "durationHoursEnd",
                    previousActivityScheduleBasicDetails.getDurationHoursEnd(),
                    activityScheduleUpdateRequest.getDurationHoursEnd(),
                    "Duration Hours End"));
            hasChanges = true;
        }

        // Compare specialNotes
        if (!Objects.equals(activityScheduleUpdateRequest.getSpecialNotes(), previousActivityScheduleBasicDetails.getSpecialNotes())) {
            changes.add(String.format("Special Notes changed from '%s' to '%s'",
                    previousActivityScheduleBasicDetails.getSpecialNotes(),
                    activityScheduleUpdateRequest.getSpecialNotes()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "specialNotes",
                    previousActivityScheduleBasicDetails.getSpecialNotes(),
                    activityScheduleUpdateRequest.getSpecialNotes(),
                    "Special Notes"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(activityScheduleUpdateRequest.getDescription(), previousActivityScheduleBasicDetails.getDescription())) {
            changes.add(String.format("Description changed from '%s' to '%s'",
                    previousActivityScheduleBasicDetails.getDescription(),
                    activityScheduleUpdateRequest.getDescription()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "description",
                    previousActivityScheduleBasicDetails.getDescription(),
                    activityScheduleUpdateRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare packageScheduleId
        if (!Objects.equals(activityScheduleUpdateRequest.getPackageScheduleId(), previousActivityScheduleBasicDetails.getPackageScheduleId())) {
            changes.add(String.format("Package Schedule ID changed from %d to %d",
                    previousActivityScheduleBasicDetails.getPackageScheduleId(),
                    activityScheduleUpdateRequest.getPackageScheduleId()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "packageScheduleId",
                    previousActivityScheduleBasicDetails.getPackageScheduleId(),
                    activityScheduleUpdateRequest.getPackageScheduleId(),
                    "Package Schedule ID"));
            hasChanges = true;
        }

        // Compare tourScheduleId
        if (!Objects.equals(activityScheduleUpdateRequest.getTourScheduleId(), previousActivityScheduleBasicDetails.getTourScheduleId())) {
            changes.add(String.format("Tour Schedule ID changed from %d to %d",
                    previousActivityScheduleBasicDetails.getTourScheduleId(),
                    activityScheduleUpdateRequest.getTourScheduleId()));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "tourScheduleId",
                    previousActivityScheduleBasicDetails.getTourScheduleId(),
                    activityScheduleUpdateRequest.getTourScheduleId(),
                    "Tour Schedule ID"));
            hasChanges = true;
        }

        // Compare status
        String oldStatus = previousActivityScheduleBasicDetails.getStatus();
        String newStatus = activityScheduleUpdateRequest.getStatus();
        if (oldStatus != null && newStatus != null && !oldStatus.equals(newStatus)) {
            changes.add(String.format("Status changed from '%s' to '%s'", oldStatus, newStatus));
            fieldChanges.add(new ActivitiesScheduleComparisonResult.FieldChange(
                    "status",
                    oldStatus,
                    newStatus,
                    "Status"));
            hasChanges = true;
        }

        // Add warnings if needed (example: date validation warnings)
        List<String> warnings = new ArrayList<>();
        if (activityScheduleUpdateRequest.getAssumeEndDate() != null &&
                activityScheduleUpdateRequest.getAssumeStartDate() != null &&
                activityScheduleUpdateRequest.getAssumeEndDate().before(activityScheduleUpdateRequest.getAssumeStartDate())) {
            warnings.add("Warning: End date is before start date!");
        }

        if (activityScheduleUpdateRequest.getDurationHoursEnd() != null &&
                activityScheduleUpdateRequest.getDurationHoursStart() != null &&
                activityScheduleUpdateRequest.getDurationHoursEnd() < activityScheduleUpdateRequest.getDurationHoursStart()) {
            warnings.add("Warning: Duration hours end is less than duration hours start!");
        }

        // Build the result
        return resultBuilder
                .changes(changes)
                .fieldChanges(fieldChanges)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(loggedUser != null ? loggedUser.getFirstName() + " " + loggedUser.getLastName() : "Unknown")
                .changedByUserId(loggedUser != null ? loggedUser.getId() : null)
                .changeTimestamp(new Date().toString())
                .build();
    }


    private ActivitiesComparisonResult compareActivitiesUpdates(
            ActivityUpdateRequest activityUpdateRequest,
            ActivityResponseDto previousActivity) {

        ActivitiesComparisonResult.ActivitiesComparisonResultBuilder resultBuilder =
                ActivitiesComparisonResult.builder();

        List<ActivitiesComparisonResult.FieldChange> fieldChanges = new ArrayList<>();

        // Compare basic fields
        if (activityUpdateRequest.getName() != null &&
                !activityUpdateRequest.getName().equals(previousActivity.getName())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "name", previousActivity.getName(), activityUpdateRequest.getName()));
        }

        if (activityUpdateRequest.getDescription() != null &&
                !activityUpdateRequest.getDescription().equals(previousActivity.getDescription())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "description", previousActivity.getDescription(), activityUpdateRequest.getDescription()));
        }

        if (activityUpdateRequest.getDurationHours() != null &&
                !activityUpdateRequest.getDurationHours().equals(previousActivity.getDurationHours())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "durationHours", previousActivity.getDurationHours(), activityUpdateRequest.getDurationHours()));
        }

        if (activityUpdateRequest.getAvailableFrom() != null) {
            LocalTime previousAvailableFrom = previousActivity.getAvailableFrom() != null ?
                    previousActivity.getAvailableFrom().toLocalTime() : null;
            if (!Objects.equals(activityUpdateRequest.getAvailableFrom(), previousAvailableFrom)) {
                fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                        "availableFrom", previousAvailableFrom, activityUpdateRequest.getAvailableFrom()));
            }
        }

        if (activityUpdateRequest.getAvailableTo() != null) {
            LocalTime previousAvailableTo = previousActivity.getAvailableTo() != null ?
                    previousActivity.getAvailableTo().toLocalTime() : null;
            if (!Objects.equals(activityUpdateRequest.getAvailableTo(), previousAvailableTo)) {
                fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                        "availableTo", previousAvailableTo, activityUpdateRequest.getAvailableTo()));
            }
        }

        if (activityUpdateRequest.getPriceLocal() != null &&
                !activityUpdateRequest.getPriceLocal().equals(previousActivity.getPriceLocal())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "priceLocal", previousActivity.getPriceLocal(), activityUpdateRequest.getPriceLocal()));
        }

        if (activityUpdateRequest.getPriceForeigners() != null &&
                !activityUpdateRequest.getPriceForeigners().equals(previousActivity.getPriceForeigners())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "priceForeigners", previousActivity.getPriceForeigners(), activityUpdateRequest.getPriceForeigners()));
        }

        if (activityUpdateRequest.getMinParticipate() != null &&
                !activityUpdateRequest.getMinParticipate().equals(previousActivity.getMinParticipate())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "minParticipate", previousActivity.getMinParticipate(), activityUpdateRequest.getMinParticipate()));
        }

        if (activityUpdateRequest.getMaxParticipate() != null &&
                !activityUpdateRequest.getMaxParticipate().equals(previousActivity.getMaxParticipate())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "maxParticipate", previousActivity.getMaxParticipate(), activityUpdateRequest.getMaxParticipate()));
        }

        if (activityUpdateRequest.getSeasonId() != null &&
                !activityUpdateRequest.getSeasonId().equals(previousActivity.getSeasonId())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "seasonId", previousActivity.getSeasonId(), activityUpdateRequest.getSeasonId()));
        }

        if (activityUpdateRequest.getStatus() != null &&
                !activityUpdateRequest.getStatus().equals(previousActivity.getStatus())) {
            fieldChanges.add(new ActivitiesComparisonResult.FieldChange(
                    "status", previousActivity.getStatus(), activityUpdateRequest.getStatus()));
        }

        resultBuilder.basicFieldChanges(fieldChanges);

        // Category changes
        resultBuilder.categoryIdsToRemove(activityUpdateRequest.getRemoveCategoryIds() != null ?
                activityUpdateRequest.getRemoveCategoryIds() : Collections.emptyList());

        if (activityUpdateRequest.getAddCategories() != null) {
            List<ActivitiesComparisonResult.CategoryChange> categoriesToAdd =
                    activityUpdateRequest.getAddCategories().stream()
                            .map(cat -> ActivitiesComparisonResult.CategoryChange.builder()
                                    .categoryId(cat.getCategoryId())
                                    .isPrimary(cat.getIsPrimary())
                                    .status(cat.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.categoriesToAdd(categoriesToAdd);
        }

        if (activityUpdateRequest.getUpdatedCategories() != null) {
            List<ActivitiesComparisonResult.CategoryChange> categoriesToUpdate =
                    activityUpdateRequest.getUpdatedCategories().stream()
                            .map(cat -> ActivitiesComparisonResult.CategoryChange.builder()
                                    .categoryId(cat.getCategoryId())
                                    .isPrimary(cat.getIsPrimary())
                                    .status(cat.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.categoriesToUpdate(categoriesToUpdate);
        }

        // Image changes
        resultBuilder.imageIdsToRemove(activityUpdateRequest.getRemoveImagesIds() != null ?
                activityUpdateRequest.getRemoveImagesIds() : Collections.emptyList());

        if (activityUpdateRequest.getAddImages() != null) {
            List<ActivitiesComparisonResult.ImageChange> imagesToAdd =
                    activityUpdateRequest.getAddImages().stream()
                            .map(img -> ActivitiesComparisonResult.ImageChange.builder()
                                    .name(img.getName())
                                    .description(img.getDescription())
                                    .imageUrl(img.getImageUrl())
                                    .status(img.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToAdd(imagesToAdd);
        }

        if (activityUpdateRequest.getUpdatedImages() != null) {
            List<ActivitiesComparisonResult.ImageChange> imagesToUpdate =
                    activityUpdateRequest.getUpdatedImages().stream()
                            .map(img -> ActivitiesComparisonResult.ImageChange.builder()
                                    .imageId(img.getImageId())
                                    .name(img.getName())
                                    .description(img.getDescription())
                                    .imageUrl(img.getImageUrl())
                                    .status(img.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToUpdate(imagesToUpdate);
        }

        // Requirement changes
        resultBuilder.requirementIdsToRemove(activityUpdateRequest.getRemoveRequirementsIds() != null ?
                activityUpdateRequest.getRemoveRequirementsIds() : Collections.emptyList());

        if (activityUpdateRequest.getAddRequirements() != null) {
            List<ActivitiesComparisonResult.RequirementChange> requirementsToAdd =
                    activityUpdateRequest.getAddRequirements().stream()
                            .map(req -> ActivitiesComparisonResult.RequirementChange.builder()
                                    .name(req.getName())
                                    .value(req.getValue())
                                    .description(req.getDescription())
                                    .color(req.getColor())
                                    .status(req.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.requirementsToAdd(requirementsToAdd);
        }

        if (activityUpdateRequest.getUpdatedRequirements() != null) {
            List<ActivitiesComparisonResult.RequirementChange> requirementsToUpdate =
                    activityUpdateRequest.getUpdatedRequirements().stream()
                            .map(req -> ActivitiesComparisonResult.RequirementChange.builder()
                                    .requirementId(req.getRequirementId())
                                    .name(req.getName())
                                    .value(req.getValue())
                                    .description(req.getDescription())
                                    .color(req.getColor())
                                    .status(req.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.requirementsToUpdate(requirementsToUpdate);
        }

        // Determine if there are any changes
        boolean hasChanges = !fieldChanges.isEmpty() ||
                (activityUpdateRequest.getRemoveCategoryIds() != null && !activityUpdateRequest.getRemoveCategoryIds().isEmpty()) ||
                (activityUpdateRequest.getAddCategories() != null && !activityUpdateRequest.getAddCategories().isEmpty()) ||
                (activityUpdateRequest.getUpdatedCategories() != null && !activityUpdateRequest.getUpdatedCategories().isEmpty()) ||
                (activityUpdateRequest.getRemoveImagesIds() != null && !activityUpdateRequest.getRemoveImagesIds().isEmpty()) ||
                (activityUpdateRequest.getAddImages() != null && !activityUpdateRequest.getAddImages().isEmpty()) ||
                (activityUpdateRequest.getUpdatedImages() != null && !activityUpdateRequest.getUpdatedImages().isEmpty()) ||
                (activityUpdateRequest.getRemoveRequirementsIds() != null && !activityUpdateRequest.getRemoveRequirementsIds().isEmpty()) ||
                (activityUpdateRequest.getAddRequirements() != null && !activityUpdateRequest.getAddRequirements().isEmpty()) ||
                (activityUpdateRequest.getUpdatedRequirements() != null && !activityUpdateRequest.getUpdatedRequirements().isEmpty());

        resultBuilder.hasChanges(hasChanges);

        // Create summary
        String summary = createSummary(resultBuilder.build());
        resultBuilder.summary(summary);

        return resultBuilder.build();
    }

    private String createSummary(ActivitiesComparisonResult result) {
        StringBuilder summary = new StringBuilder();

        if (!result.isHasChanges()) {
            return "No changes detected";
        }

        if (!result.getBasicFieldChanges().isEmpty()) {
            summary.append("Basic fields updated: ")
                    .append(result.getBasicFieldChanges().stream()
                            .map(ActivitiesComparisonResult.FieldChange::getFieldName)
                            .collect(Collectors.joining(", ")))
                    .append(". ");
        }

        if (!result.getCategoryIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getCategoryIdsToRemove().size()).append(" categories. ");
        }

        if (!result.getCategoriesToAdd().isEmpty()) {
            summary.append("Add ").append(result.getCategoriesToAdd().size()).append(" categories. ");
        }

        if (!result.getCategoriesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getCategoriesToUpdate().size()).append(" categories. ");
        }

        if (!result.getImageIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getImageIdsToRemove().size()).append(" images. ");
        }

        if (!result.getImagesToAdd().isEmpty()) {
            summary.append("Add ").append(result.getImagesToAdd().size()).append(" images. ");
        }

        if (!result.getImagesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getImagesToUpdate().size()).append(" images. ");
        }

        if (!result.getRequirementIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getRequirementIdsToRemove().size()).append(" requirements. ");
        }

        if (!result.getRequirementsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getRequirementsToAdd().size()).append(" requirements. ");
        }

        if (!result.getRequirementsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getRequirementsToUpdate().size()).append(" requirements.");
        }

        return summary.toString().trim();
    }

}
