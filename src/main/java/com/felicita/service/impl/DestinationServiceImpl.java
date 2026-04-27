package com.felicita.service.impl;

import com.felicita.comparator.DestinationComparator;
import com.felicita.email.DestinationCategoryEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.CommonStatus;
import com.felicita.model.enums.NotificationType;
import com.felicita.model.enums.Priority;
import com.felicita.model.enums.Privileges;
import com.felicita.model.other.DestinationCategoryUpdateComparisonResult;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.repository.DestinationRepository;
import com.felicita.repository.WishListRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.DestinationService;
import com.felicita.service.EmailHelperService;
import com.felicita.service.EmailService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.DestinationValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl implements DestinationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationServiceImpl.class);

    private final DestinationRepository destinationRepository;
    private final DestinationValidationService destinationValidationService;
    private final CommonService commonService;
    private final WishListRepository wishListRepository;
    private final EmailService emailService;
    private final EmailHelperService emailHelperService;
    private final DestinationComparator destinationComparator;
    private final DestinationCategoryEmailHelperService destinationCategoryEmailHelperService;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository, DestinationValidationService destinationValidationService, CommonService commonService, WishListRepository wishListRepository, EmailService emailService, EmailHelperService emailHelperService, DestinationComparator destinationComparator, DestinationCategoryEmailHelperService destinationCategoryEmailHelperService) {
        this.destinationRepository = destinationRepository;
        this.destinationValidationService = destinationValidationService;
        this.commonService = commonService;
        this.wishListRepository = wishListRepository;
        this.emailService = emailService;
        this.emailHelperService = emailHelperService;
        this.destinationComparator = destinationComparator;
        this.destinationCategoryEmailHelperService = destinationCategoryEmailHelperService;
    }

    @Override
    public CommonResponse<List<DestinationResponseDto>> getAllDestinations() {
        LOGGER.info("Start fetching all destinations from repository");
        try {
            List<DestinationResponseDto> destinationResponseDtos = destinationRepository.getAllDestinations();

            if (destinationResponseDtos.isEmpty()) {
                LOGGER.warn("No destinations found in database");
                throw new DataNotFoundErrorExceptionHandler("No destinations found");
            }

            LOGGER.info("Fetched {} destinations successfully", destinationResponseDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations from database");
        } finally {
            LOGGER.info("End fetching all destinations from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationResponseDto>> getActiveDestinations() {
        LOGGER.info("Start fetching all active destinations from repository");
        try {
            List<DestinationResponseDto> destinationResponseDtos = getAllDestinations().getData();

            List<DestinationResponseDto> destinationResponseDtoList = destinationResponseDtos.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getStatusName())).toList();

            LOGGER.info("Fetched {} active destinations successfully", destinationResponseDtoList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationResponseDtoList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active destinations: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active destinations from database");
        } finally {
            LOGGER.info("End fetching all active destinations from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationCategoryResponseDto>> getAllDestinationsCategories() {
        LOGGER.info("Start fetching all destinations categories from repository");
        try {
            List<DestinationCategoryResponseDto> destinationCategoryResponseDtos = destinationRepository.getAllDestinationsCategories();

            if (destinationCategoryResponseDtos.isEmpty()) {
                LOGGER.warn("No destinations categories found in database");
                throw new DataNotFoundErrorExceptionHandler("No destinations categories found");
            }

            LOGGER.info("Fetched {} all destinations categories successfully", destinationCategoryResponseDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationCategoryResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations categories from database");
        } finally {
            LOGGER.info("End fetching all destinations categories from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationCategoryResponseDto>> getActiveDestinationsCategories() {
        LOGGER.info("Start fetching active destinations categories from repository");
        try {
            List<DestinationCategoryResponseDto> destinationCategoryResponseDtos = getAllDestinationsCategories().getData();

            List<DestinationCategoryResponseDto> destinationCategoryResponseDtoList = destinationCategoryResponseDtos.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getCategoryStatus()))
                    .toList();

            LOGGER.info("Fetched {} active destinations categories successfully", destinationCategoryResponseDtoList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationCategoryResponseDtoList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active destinations categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active destinations categories from database");
        } finally {
            LOGGER.info("End fetching active destinations categories from repository");
        }
    }

    @Override
    public CommonResponse<List<PopularDestinationResponseDto>> getPopularDestinations() {
        LOGGER.info("Start fetching all popular destinations from repository");
        try {
            List<PopularDestinationResponseDto> popularDestinationResponseDtos = destinationRepository.getPopularDestinations();

            if (popularDestinationResponseDtos.isEmpty()) {
                LOGGER.warn("No popular destinations found in database");
                throw new DataNotFoundErrorExceptionHandler("No popular destinations found");
            }

            LOGGER.info("Fetched {} popular destinations successfully", popularDestinationResponseDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    popularDestinationResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching popular destinations : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch popular destinations  from database");
        } finally {
            LOGGER.info("End fetching all popular destinations  from repository");
        }
    }

    @Override
    public CommonResponse<List<PopularDestinationResponseDto>> getNewDestinations() {
        LOGGER.info("Start fetching new destinations from repository");
        try {
            List<PopularDestinationResponseDto> popularDestinationResponseDtos =
                    destinationRepository.getPopularDestinations();

            List<PopularDestinationResponseDto> lastMonthDestinations = popularDestinationResponseDtos.stream()
                    .filter(d -> d.getPopularCreatedAt() != null &&
                            d.getPopularCreatedAt().isAfter(LocalDateTime.now().minusMonths(6)))
                    .toList();


            if (lastMonthDestinations.isEmpty()) {
                LOGGER.warn("No new destinations found in database");
                throw new DataNotFoundErrorExceptionHandler("No new destinations found");
            }

            LOGGER.info("Fetched {} new destinations successfully", lastMonthDestinations.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    lastMonthDestinations,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching new destinations : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch new destinations  from database");
        } finally {
            LOGGER.info("End fetching new destinations  from repository");
        }
    }

    @Override
    public CommonResponse<List<TrendingDestinationResponseDto>> getTrendingDestinations() {
        LOGGER.info("Start fetching trending destinations from repository");
        try {
            List<TrendingDestinationResponseDto> trendingDestinationResponseDtos = destinationRepository.getTrendingDestinations();

            if (trendingDestinationResponseDtos.isEmpty()) {
                LOGGER.warn("No trending destinations found in database");
                throw new DataNotFoundErrorExceptionHandler("No trending destinations found");
            }

            LOGGER.info("Fetched {} trending destinations successfully", trendingDestinationResponseDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    trendingDestinationResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching trending destinations : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch trending destinations  from database");
        } finally {
            LOGGER.info("End fetching trending destinations  from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationsForTourMapDto>> getDestinationsForTourMap() {
        LOGGER.info("Start fetching get destinations for tour map from repository");
        try {
            List<DestinationsForTourMapDto> destinationsForTourMapDtos = destinationRepository.getDestinationsForTourMap();

            if (destinationsForTourMapDtos.isEmpty()) {
                LOGGER.warn("No destinations for tour map  found in database");
                throw new DataNotFoundErrorExceptionHandler("No destinations for tour map found");
            }

            LOGGER.info("Fetched {} destinations for tour map  successfully", destinationsForTourMapDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationsForTourMapDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations for tour map : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations for tour map  from database");
        } finally {
            LOGGER.info("End fetching all destinations for tour map  from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationResponseDto>> getDestinationDetailsByTourId(Long tourId) {
        LOGGER.info("Start fetching destinations details by tour id from repository");
        try {
            List<DestinationResponseDto> destinationResponseDtos = destinationRepository.getDestinationDetailsByTourId(tourId);

            if (destinationResponseDtos.isEmpty()) {
                LOGGER.warn("No destinations details found in database for tour id : {} ", tourId);
                throw new DataNotFoundErrorExceptionHandler("No destinations details found in database for tour id : " + tourId);
            }

            LOGGER.info("Fetched {} destinations details for tour id {} successfully", destinationResponseDtos.size(), tourId);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations details for tour id :{} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations details from database");
        } finally {
            LOGGER.info("End fetching destinations details for tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<DestinationReviewDetailsResponse>> getAllDestinationsReviewDetails() {
        LOGGER.info("Start fetching all destinations reviews from repository");
        try {
            List<DestinationReviewDetailsResponse> destinationReviewDetailsResponses = destinationRepository.getAllDestinationsReviewDetails();

            if (destinationReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No destinations reviews found in database");
                throw new DataNotFoundErrorExceptionHandler("No destinations reviews found");
            }

            List<DestinationReviewDetailsResponse> destinationReviewDetailsResponseList = destinationReviewDetailsResponses.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getReviewStatus()))
                    .toList();

            LOGGER.info("Fetched {} destinations reviews successfully", destinationReviewDetailsResponseList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationReviewDetailsResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations reviews : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations reviews from database");
        } finally {
            LOGGER.info("End fetching destinations reviews from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationReviewDetailsResponse>> getDestinationReviewDetailsById(Long destinationId) {
        LOGGER.info("Start fetching destinations reviews by destination id : {} from repository", destinationId);
        try {
            List<DestinationReviewDetailsResponse> destinationReviewDetailsResponses = destinationRepository.getDestinationReviewDetailsById(destinationId);

            if (destinationReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No destinations reviews by destination id : {} found in database", destinationId);
                throw new DataNotFoundErrorExceptionHandler("No destinations reviews by destination id : found in database" + destinationId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationReviewDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations reviews by destination id : {} , {}", destinationId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations reviews by destination id : " + destinationId);
        } finally {
            LOGGER.info("End fetching destinations reviews by destination id : {} from repository", destinationId);
        }
    }

    @Override
    public CommonResponse<DestinationResponseDto> getDestinationDetailsById(Long destinationId) {
        LOGGER.info("Start fetching destination details by destination id : {} from repository", destinationId);
        try {
            DestinationResponseDto destinationDetailsById = destinationRepository.getDestinationDetailsById(destinationId);

            if (destinationDetailsById == null) {
                LOGGER.warn("No destination details by destination id : {} found in database", destinationId);
                throw new DataNotFoundErrorExceptionHandler("No destination details by destination id : {} found in database" + destinationId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationDetailsById,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination details by destination id :{}, {}", destinationId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination details by destination id from database");
        } finally {
            LOGGER.info("End fetching destination details by destination id : {} from repository", destinationId);
        }
    }

    @Override
    public CommonResponse<List<DestinationHistoryDetailsResponse>> getAllDestinationHistoryDetails() {
        LOGGER.info("Start fetching destination history details from repository");
        try {
            List<DestinationHistoryDetailsResponse> destinationReviewDetailsResponses = destinationRepository.getAllDestinationHistoryDetails();

            if (destinationReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No destination history details found in database");
                throw new DataNotFoundErrorExceptionHandler("No destination history details found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationReviewDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination history details : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination history details from database");
        } finally {
            LOGGER.info("End fetching destination history details from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationHistoryDetailsResponse>> getDestinationHistoryDetailsById(Long destinationId) {
        LOGGER.info("Start fetching destination history details by destination id : {} from repository", destinationId);
        try {
            List<DestinationHistoryDetailsResponse> destinationReviewDetailsResponses = destinationRepository.getDestinationHistoryDetailsById(destinationId);

            if (destinationReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No destination history details by destination id : {} found in database", destinationId);
                throw new DataNotFoundErrorExceptionHandler("No destination history details by found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationReviewDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination history details by destination id: {} , {}", destinationId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination history details from database");
        } finally {
            LOGGER.info("End fetching destination history details by destination id : {} from repository", destinationId);
        }
    }

    @Override
    public CommonResponse<List<DestinationHistoryImageResponse>> getAllDestinationHistoryImages() {
        LOGGER.info("Start fetching destination history images from repository");
        try {
            List<DestinationHistoryImageResponse> destinationHistoryImageResponses = destinationRepository.getAllDestinationHistoryImages();

            if (destinationHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No destination history images found in database");
                throw new DataNotFoundErrorExceptionHandler("No destination history images found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationHistoryImageResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination history images : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination history images from database");
        } finally {
            LOGGER.info("End fetching destination history images from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationHistoryImageResponse>> getDestinationHistoryImagesById(Long destinationId) {
        LOGGER.info("Start fetching destination history images by destination id : {} from repository", destinationId);
        try {
            List<DestinationHistoryImageResponse> destinationHistoryImageResponses = destinationRepository.getDestinationHistoryImagesById(destinationId);

            if (destinationHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No destination history images by destination id : {} found in database", destinationId);
                throw new DataNotFoundErrorExceptionHandler("No destination history images found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationHistoryImageResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination history images by destination id : {} , {}", destinationId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination history images from database");
        } finally {
            LOGGER.info("End fetching destination history images by destination id : {} from repository", destinationId);
        }
    }

    @Override
    public CommonResponse<DestinationsWithParamsResponse> getDestinationWithParams(DestinationDataRequest destinationDataRequest) {
        LOGGER.info("Start fetching all destinations with params from repository");
        try {
            DestinationsWithParamsResponse destinationsWithParamsResponse = destinationRepository.getDestinationWithParams(destinationDataRequest);
            Long userId = commonService.getUserIdBySecurityContextWithOutException();

            Set<Long> destinationIdSet = new HashSet<>();
            if (userId != null) {
                LOGGER.info("USER ID : {}, FETCHING WISHLIST DESTINATION IDS", userId);
                List<Long> destinationIds = wishListRepository.getAllDestinationWishListByUserId(userId);
                LOGGER.info("USER ID : {} , WISHLIST DESTINATION IDS : {}", userId, destinationIds);
                if (destinationIds != null) {
                    destinationIdSet.addAll(destinationIds);
                }
            }
            if (destinationsWithParamsResponse != null) {
                List<DestinationResponseDto> destinationResponseDtos = destinationsWithParamsResponse.getDestinationResponseDtos();
                if (destinationResponseDtos != null) {
                    for (DestinationResponseDto destinationResponseDto : destinationResponseDtos) {
                        destinationResponseDto.setWish(destinationIdSet.contains(destinationResponseDto.getDestinationId()));
                    }
                }
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationsWithParamsResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations with params from database");
        } finally {
            LOGGER.info("End fetching all destinations with params from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertDestination(DestinationInsertRequest destinationInsertRequest) {
        LOGGER.info("Start execute insert destination request.");
        try {
            destinationValidationService.validateDestinationInsertRequest(destinationInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            String email = commonService.getUserEmailBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);

            List<String> supervisorEmails = extractSupervisorEmails(supervisorDetails);
            Long destinationId = destinationRepository.insertDestination(destinationInsertRequest, userId);
            List<String> destinationCategories = destinationRepository.getDestinationCategoriesNamesByIds(destinationInsertRequest.getDestinationCategoriesIdList());

            List<Long> supervisorUserIds = extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);
            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.DESTINATION_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Destination Created")
                    .message("A new destination '" + destinationInsertRequest.getName() + "' has been created.")
                    .actionUrl("/destinations/" + destinationId)
                    .actionText("View Destination")
                    .icon("MapPin")
                    .color("#10B981")
                    .metadata(Map.of(
                            "destinationId", destinationId,
                            "destinationName", destinationInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.DESTINATION_CREATE.name())
                    .sourceModule("DESTINATION")
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            LOGGER.info("notification id " + notificationId.toString());

            commonService.createNotificationRecipients(notificationId,supervisorUserIds);

            if (destinationId != null) {
                supervisorEmails.remove(email);
                supervisorEmails.add("felicitatrips@gmail.com");
                String body = emailHelperService.buildDestinationCreateSuccessfullBody(destinationInsertRequest, destinationCategories, loggedUser);
                String subject = emailHelperService.buildDestinationCreateSuccessfullSubject(destinationInsertRequest, loggedUser);
//                emailService.sendFromDev(email, supervisorEmails, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert destination request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            LOGGER.error(vfe.toString());
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert destination request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            LOGGER.error(ife.toString());
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            LOGGER.error(uae.toString());
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.toString());
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateDestination(DestinationTerminateRequest destinationTerminateRequest) {
        LOGGER.info("Start execute terminate destination request.");
        try {
            destinationValidationService.validateTerminateDestinationRequest(destinationTerminateRequest);
            DestinationResponseDto destinationDetailsById = getDestinationDetailsById(destinationTerminateRequest.getDestinationId()).getData();
            Long userId = commonService.getUserIdBySecurityContext();
            User loggeduser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);
            List<String> superviosrList = extractSupervisorEmails(supervisorDetails);
            superviosrList.add("felicitatrips@gmail.com");
            destinationRepository.terminateDestination(destinationTerminateRequest, userId);

            String subject = emailHelperService.buildDestinationTerminateSuccessfullSubject(loggeduser, destinationDetailsById);
            String body = emailHelperService.buildDestinationTerminateSuccessfullBody(loggeduser, destinationDetailsById);

            emailService.sendFromDev(loggeduser.getEmail(), superviosrList, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminate destination request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate destination request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<List<DestinationForTerminateResponse>> getDestinationsForTerminate() {
        LOGGER.info("Start fetching destinations names and ids for terminate from repository");
        try {
            List<DestinationForTerminateResponse> destinationForTerminateResponses =
                    destinationRepository.getDestinationsForTerminate();

            if (destinationForTerminateResponses.isEmpty()) {
                LOGGER.warn("No destinations names and ids for terminate found in database");
                throw new DataNotFoundErrorExceptionHandler("No destinations names and ids for terminate found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationForTerminateResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching active destinations: {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations names and ids for terminate : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations names and ids for terminate from database");
        } finally {
            LOGGER.info("End fetching destinations names and ids for terminate from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateDestination(DestinationUpdateRequest destinationUpdateRequest) {
        LOGGER.info("Start execute update destination request.");
        try {
            destinationValidationService.validateDestinationUpdateRequest(destinationUpdateRequest);
            DestinationResponseDto destinationDetailsById = getDestinationDetailsById(destinationUpdateRequest.getDestinationId()).getData();
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);
            List<String> supervisorsEmails = extractSupervisorEmails(supervisorDetails);
            supervisorsEmails.add("felicitatrips@gmail.com");
            destinationRepository.updateBasicDestinationDetails(destinationUpdateRequest, userId);
            destinationRepository.removeDestinationImages(destinationUpdateRequest.getRemoveImages(), userId);
            destinationRepository.addNewImagesToDestination(destinationUpdateRequest.getNewImages(), destinationUpdateRequest.getDestinationId(), userId);
            destinationRepository.removeDestinationActivities(destinationUpdateRequest.getRemoveActivities(), userId);
            destinationRepository.addNewActivitiesToDestination(destinationUpdateRequest.getNewActivities(), destinationUpdateRequest.getDestinationId(), userId);

            DestinationUpdateComparisonResult comparisonResult = destinationComparator.compareUpdates(
                    destinationUpdateRequest,
                    destinationDetailsById
            );

            LOGGER.info("Update comparison result: {}", comparisonResult);

            String subject = emailHelperService.buildDestinationUpdateSuccessfullSubject(loggedUser);
            String body = emailHelperService.buildDestinationUpdateSuccessfullBody(loggedUser, destinationUpdateRequest.getDestinationId(), comparisonResult);
            emailService.sendFromDev(loggedUser.getEmail(), supervisorsEmails, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update destination request", destinationUpdateRequest.getDestinationId()),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert destination request", vfe.getValidationFailedResponses());
        } catch (UpdateFailedErrorExceptionHandler ufe) {
            throw new UpdateFailedErrorExceptionHandler(ufe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<DestinationStatisticsResponse> getDestinationsStatistics() {
        LOGGER.info("Start fetching destination statistics from repository");
        try {
            DestinationStatisticsResponse destinationStatisticsResponse = new DestinationStatisticsResponse();
            DestinationStatisticsResponse.DestinationDetails destinationDetails = destinationRepository.getDestinationDetailsStatistics();
            DestinationStatisticsResponse.WishDetails wishDetails = destinationRepository.getDestinationWishStatistics();
            List<DestinationStatisticsResponse.CategoryDetails> categoryDetails = destinationRepository.getDestinationCategoryStatistics();

            destinationStatisticsResponse.setDestinationDetails(destinationDetails);
            destinationStatisticsResponse.setWishDetails(wishDetails);
            destinationStatisticsResponse.setCategoryDetails(categoryDetails);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination statistics from database");
        } finally {
            LOGGER.info("End fetching destination statistics from repository");
        }
    }

    @Override
    public CommonResponse<DestinationCategoriesStatisticsResponse> getDestinationCategoriesStatistics() {
        LOGGER.info("Start fetching destination categories statistics from repository");
        try {
            DestinationCategoriesStatisticsResponse destinationCategoriesStatisticsResponse = new DestinationCategoriesStatisticsResponse();

            DestinationCategoriesStatisticsResponse.DestinationCategoriesDetails destinationCategoriesDetails =
                    destinationRepository.getDestinationCategoriesDetails();
            List<DestinationCategoriesStatisticsResponse.CategoryUsedDetails> categoryUsedDetails =
                    destinationRepository.getCategoryUsedDetails();
            List<DestinationCategoriesStatisticsResponse.CategoriesImagesCount> categoriesImagesCounts =
                    destinationRepository.getCategoriesImagesCount();

            destinationCategoriesStatisticsResponse.setDestinationCategoriesDetails(destinationCategoriesDetails);
            destinationCategoriesStatisticsResponse.setCategoryUsedDetails(categoryUsedDetails);
            destinationCategoriesStatisticsResponse.setCategoriesImagesCounts(categoriesImagesCounts);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationCategoriesStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination categories statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination categories statistics from database");
        } finally {
            LOGGER.info("End fetching destination categories statistics from repository");
        }
    }

    @Override
    public CommonResponse<DestinationCategoryDetailsResponseDto> getDestinationsCategoryDetailsById(DestinationCategoryDetailsRequest destinationCategoryDetailsRequest) {
        LOGGER.info("Start fetching destinations category from repository");
        try {
            DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto = destinationRepository.getDestinationsCategoryDetailsById(destinationCategoryDetailsRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationCategoryDetailsResponseDto,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destinations category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destinations category from database");
        } finally {
            LOGGER.info("End fetching destinations category from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertDestinationCategory(DestinationCategoryInsertRequest destinationCategoryInsertRequest) {
        LOGGER.info("Start execute insert destination category request.");
        try {
            destinationValidationService.validateDestinationCategoryInsertRequest(destinationCategoryInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            String email = commonService.getUserEmailBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);
            List<String> supervisorsEmails = extractSupervisorEmails(supervisorDetails);
            Long destinationCategoryId = destinationRepository.insertDestinationCategory(destinationCategoryInsertRequest, userId);
            destinationRepository.insertDestinationCategoryImages(destinationCategoryInsertRequest.getImages(), destinationCategoryId, userId);

            if (destinationCategoryId != null) {
                supervisorsEmails.remove(email);
                supervisorsEmails.add("felicitatrips@gmail.com");
                String body = destinationCategoryEmailHelperService.buildDestinationCategoryCreateSuccessfullBody(destinationCategoryInsertRequest, loggedUser);
                String subject = destinationCategoryEmailHelperService.buildDestinationCategoryCreateSuccessfullSubject(destinationCategoryInsertRequest, loggedUser);
                emailService.sendFromDev(email, supervisorsEmails, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert destination category request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert destination category request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateDestinationCategory(DestinationCategoryUpdateRequest destinationCategoryUpdateRequest) {
        LOGGER.info("Start execute update destination request.");
        try {
            destinationValidationService.validateDestinationCategoryUpdateRequest(destinationCategoryUpdateRequest);
            DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto
                    = getDestinationsCategoryDetailsById(new DestinationCategoryDetailsRequest(
                    destinationCategoryUpdateRequest.getCategoryId()
            )).getData();
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);
            List<String> supervisorsEmails = extractSupervisorEmails(supervisorDetails);
            supervisorsEmails.add("felicitatrips@gmail.com");
            destinationRepository.updateDestinationCategoryDetails(destinationCategoryUpdateRequest, userId);
            destinationRepository.removeDestinationCategoryImagesDetails(destinationCategoryUpdateRequest.getRemoveImageIds(), userId);
            destinationRepository.insertDestinationCategoryImages(destinationCategoryUpdateRequest.getNewImages(), destinationCategoryUpdateRequest.getCategoryId(), userId);
            destinationRepository.updateDestinationCategoryImagesDetails(destinationCategoryUpdateRequest.getUpdateImages(), userId);

            DestinationCategoryUpdateComparisonResult comparisonResult = destinationComparator.compareDestinationCategoryUpdates(
                    destinationCategoryDetailsResponseDto,
                    destinationCategoryUpdateRequest
            );

            LOGGER.info("Update destination category comparison result: {}", comparisonResult);

            String subject = destinationCategoryEmailHelperService.buildDestinationCategoryUpdateSuccessfullSubject(destinationCategoryUpdateRequest, loggedUser);
            String body = destinationCategoryEmailHelperService.buildDestinationCategoryUpdateSuccessfullBody(loggedUser, comparisonResult);
            emailService.sendFromDev(loggedUser.getEmail(), supervisorsEmails, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update destination category request", destinationCategoryUpdateRequest.getCategoryId()),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert destination category request", vfe.getValidationFailedResponses());
        } catch (UpdateFailedErrorExceptionHandler ufe) {
            throw new UpdateFailedErrorExceptionHandler(ufe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateDestinationCategory(DestinationCategoryTerminateRequest destinationCategoryTerminateRequest) {
        LOGGER.info("Start execute terminate destination category request.");
        try {
            destinationValidationService.validateDestinationCategoryTerminateRequest(destinationCategoryTerminateRequest);
            DestinationCategoryDetailsResponseDto destinationCategoryDetailsResponseDto
                    = getDestinationsCategoryDetailsById(new DestinationCategoryDetailsRequest(
                    destinationCategoryTerminateRequest.getDestinationCategoryId()
            )).getData();
            Long userId = commonService.getUserIdBySecurityContext();
            User loggeduser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails =
                    commonService.getSupervisorBasicDetailsByUserId(userId);
            List<String> supervisorsEmails = extractSupervisorEmails(supervisorDetails);
            supervisorsEmails.add("felicitatrips@gmail.com");
            destinationRepository.terminateDestinationCategory(destinationCategoryTerminateRequest, userId);

            String subject = destinationCategoryEmailHelperService.buildDestinationCategoryTerminateSuccessfullSubject(loggeduser, destinationCategoryDetailsResponseDto);
            String body = destinationCategoryEmailHelperService.buildDestinationCategoryTerminateSuccessfullBody(loggeduser, destinationCategoryDetailsResponseDto);

            emailService.sendFromDev(loggeduser.getEmail(), supervisorsEmails, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminate destination category request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate destination category request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    private List<String> extractSupervisorEmails(List<SupervisorBasicDetailsDto> supervisorDetails) {

        if (supervisorDetails == null || supervisorDetails.isEmpty()) {
            return List.of("felicitatrips@gmail.com");
        }

        List<String> emails = supervisorDetails.stream()
                .map(SupervisorBasicDetailsDto::getEmail)
                .filter(email -> email != null && !email.isBlank())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

        emails.add("felicitatrips@gmail.com");

        return emails;
    }

    private List<Long> extractSupervisorUserIds(List<SupervisorBasicDetailsDto> supervisorDetails) {

        if (supervisorDetails == null || supervisorDetails.isEmpty()) {
            return List.of();
        }

        return supervisorDetails.stream()
                .map(SupervisorBasicDetailsDto::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
