package com.felicita.service.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.exception.UnAuthenticateErrorExceptionHandler;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.request.ReadNotificationInsertRequest;
import com.felicita.model.response.*;
import com.felicita.repository.CommonRepository;
import com.felicita.security.model.CustomUserDetails;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.util.CommonResponseMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);

    private final CommonRepository commonRepository;

    @Autowired
    public CommonServiceImpl(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    @Value("${otp.generate.length}")
    private int otpGeneratedLength;

    @Override
    public Long getUserIdBySecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnAuthenticateErrorExceptionHandler("No authenticated user");
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getDomainUser();
        return user.getId();
    }

    @Override
    public String getUserEmailBySecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnAuthenticateErrorExceptionHandler("No authenticated user");
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getDomainUser();
        return user.getEmail();
    }

    @Override
    public Long getUserIdBySecurityContextWithOutException() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = principal.getDomainUser();
        return user.getId();
    }

    @Override
    public List<Long> extractSupervisorUserIds(List<SupervisorBasicDetailsDto> supervisorDetails) {

        if (supervisorDetails == null || supervisorDetails.isEmpty()) {
            return List.of();
        }

        return supervisorDetails.stream()
                .map(SupervisorBasicDetailsDto::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public String createEmployeeUniqueEmployeeCode() {

        String employeeCode;
        boolean exists;
        do {
            employeeCode = "EMP" + System.currentTimeMillis();
            exists = commonRepository.existsByEmployeeCode(employeeCode);

        } while (exists);

        return employeeCode;
    }

    @Override
    public String generateRandomOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otpGeneratedLength; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }

    @Override
    public CommonResponse<AllCategoriesResponse> getAllCategories() {
        LOGGER.info("Start fetching all categories from repository");
        try {
            AllCategoriesResponse allCategoriesResponse = new AllCategoriesResponse();
            List<AllCategoriesResponse.ActivityCategory> activityCategoryList =
                    commonRepository.getAllActivityCategories();
            allCategoriesResponse.setActivityCategoryList(activityCategoryList);
            List<AllCategoriesResponse.DestinationCategory> destinationCategoryList =
                    commonRepository.getAllDestinationCategories();
            allCategoriesResponse.setDestinationCategoryList(destinationCategoryList);
            List<AllCategoriesResponse.TourCategory> tourCategoryList =
                    commonRepository.getAllTourCategories();
            allCategoriesResponse.setTourCategoryList(tourCategoryList);
            List<AllCategoriesResponse.PackageCategory> packageCategoryList =
                    commonRepository.getAllPackageCategories();
            allCategoriesResponse.setPackageCategoryList(packageCategoryList);
            List<AllCategoriesResponse.Seasons> seasonsList =
                    commonRepository.getAllSeasons();
            allCategoriesResponse.setSeasonsList(seasonsList);
            List<AllCategoriesResponse.TourType> tourTypeList =
                    commonRepository.getAllTourTypes();
            allCategoriesResponse.setTourTypeList(tourTypeList);

            LOGGER.info("Fetched all categories successfully.");
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    allCategoriesResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch categories from database");
        } finally {
            LOGGER.info("End fetching all categories from repository");
        }
    }

    @Override
    public List<SupervisorBasicDetailsDto> getSupervisorBasicDetailsByUserId(Long userId) {
        LOGGER.info("Start fetching supervisors emails by user id");
        try {
            List<SupervisorBasicDetailsDto> supervisorsEmails = commonRepository.getSupervisorBasicDetailsByUserId(userId);

            if (supervisorsEmails.isEmpty()) {
                return List.of();
            }

            return supervisorsEmails;

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            return List.of();
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return List.of();
        } finally {
            LOGGER.info("End fetching supervisors emails by user id");
        }
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnAuthenticateErrorExceptionHandler("No authenticated user");
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getDomainUser();
    }

    @Override
    public Long createNotification(NotificationInsertRequestDto notificationInsertRequestDto) {
        LOGGER.info("Start create notification");
        try {
            return commonRepository.createNotification(notificationInsertRequestDto);

        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        } finally {
            LOGGER.info("End create notification");
        }
    }

    @Override
    public void createNotificationRecipients(Long notificationId, List<Long> supervisorUserIds) {
        LOGGER.info("Start create notification recipients");
        try {
            commonRepository.createNotificationRecipients(notificationId,supervisorUserIds);

        } catch (Exception e) {
            LOGGER.error("error when create notification recipients");
        } finally {
            LOGGER.info("End create notification recipients");
        }
    }

    @Override
    public CommonResponse<List<NotificationResponse>> getNotificationForLoggedUser() {
        LOGGER.info("Start fetching notifications from repository");
        try {
            Long userId = getUserIdBySecurityContext();
            List<NotificationResponse> notificationResponses = commonRepository.getNotificationForLoggedUser(userId);

            // Transform messages for each notification
            List<NotificationResponse> transformedNotifications = notificationResponses.stream()
                    .map(this::transformNotificationMessage)
                    .collect(Collectors.toList());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    transformedNotifications,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching notifications: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch notifications from database");
        } finally {
            LOGGER.info("End fetching notifications from repository");
        }
    }

    /**
     * Transform notification message based on who is viewing it
     * If logged user is not the assigned employee, change message to third-person perspective
     */
    private NotificationResponse transformNotificationMessage(NotificationResponse notification) {
        if (notification == null) {
            return notification;
        }

        Long loggedUserId = notification.getLoggedUserId();
        Long assignedTo = notification.getAssignedTo();

        // If current user is the one who performed the action OR no assigned employee, keep original message
        if (assignedTo == null || loggedUserId.equals(assignedTo)) {
            return notification;
        }

        // Transform message to third-person perspective based on notification type
        String transformedMessage = transformMessageByType(
                notification.getMessage(),
                notification.getAssignedUsername(),
                notification.getNotificationType(),
                notification.getMetadata()
        );
        notification.setMessage(transformedMessage);

        // Transform title as well
        if (notification.getTitle() != null) {
            String transformedTitle = transformTitleForViewer(
                    notification.getTitle(),
                    notification.getAssignedUsername(),
                    notification.getNotificationType()
            );
            notification.setTitle(transformedTitle);
        }

        return notification;
    }

    /**
     * Transform message based on notification type
     */
    private String transformMessageByType(String originalMessage, String assignedUsername,
                                          String notificationType, Map<String, Object> metadata) {
        if (originalMessage == null || assignedUsername == null) {
            return originalMessage;
        }

        String transformedMessage = originalMessage;

        switch (notificationType) {
            case "DESTINATION_CREATED":
                if (metadata != null && metadata.containsKey("destinationName")) {
                    transformedMessage = String.format("%s created a new destination: %s",
                            assignedUsername, metadata.get("destinationName"));
                } else {
                    transformedMessage = String.format("%s created a new destination", assignedUsername);
                }
                break;

            case "DESTINATION_UPDATED":
                if (metadata != null && metadata.containsKey("destinationName")) {
                    transformedMessage = String.format("%s updated the destination: %s",
                            assignedUsername, metadata.get("destinationName"));
                } else {
                    transformedMessage = String.format("%s updated a destination", assignedUsername);
                }
                break;

            case "DESTINATION_TERMINATED":
                if (metadata != null && metadata.containsKey("destinationName")) {
                    transformedMessage = String.format("%s terminated/deactivated the destination: %s",
                            assignedUsername, metadata.get("destinationName"));
                } else {
                    transformedMessage = String.format("%s terminated a destination", assignedUsername);
                }
                break;

            case "DESTINATION_CATEGORY_CREATED":
                if (metadata != null && metadata.containsKey("categoryName")) {
                    transformedMessage = String.format("%s created a new destination category: %s",
                            assignedUsername, metadata.get("categoryName"));
                } else {
                    transformedMessage = String.format("%s created a new destination category", assignedUsername);
                }
                break;

            case "DESTINATION_CATEGORY_UPDATED":
                if (metadata != null && metadata.containsKey("categoryName")) {
                    transformedMessage = String.format("%s updated the destination category: %s",
                            assignedUsername, metadata.get("categoryName"));
                } else {
                    transformedMessage = String.format("%s updated a destination category", assignedUsername);
                }
                break;

            case "DESTINATION_CATEGORY_TERMINATED":
                if (metadata != null && metadata.containsKey("categoryName")) {
                    transformedMessage = String.format("%s terminated/deactivated the destination category: %s",
                            assignedUsername, metadata.get("categoryName"));
                } else {
                    transformedMessage = String.format("%s terminated a destination category", assignedUsername);
                }
                break;

            default:
                // Default transformation for any other notification types
                transformedMessage = transformGenericMessage(originalMessage, assignedUsername);
                break;
        }

        // Ensure first letter is capitalized
        if (transformedMessage != null && !transformedMessage.isEmpty()) {
            transformedMessage = Character.toUpperCase(transformedMessage.charAt(0)) +
                    transformedMessage.substring(1);
        }

        return transformedMessage;
    }

    /**
     * Transform title for viewer perspective
     */
    private String transformTitleForViewer(String originalTitle, String assignedUsername, String notificationType) {
        if (originalTitle == null || assignedUsername == null) {
            return originalTitle;
        }

        String transformedTitle = originalTitle;

        // Remove first-person references
        transformedTitle = transformedTitle.replaceAll("(?i)\\bYour\\b", assignedUsername + "'s");
        transformedTitle = transformedTitle.replaceAll("(?i)\\bMy\\b", assignedUsername + "'s");
        transformedTitle = transformedTitle.replaceAll("(?i)\\bI've\\b", assignedUsername + " has");
        transformedTitle = transformedTitle.replaceAll("(?i)\\bI have\\b", assignedUsername + " has");

        // For specific notification types, create more appropriate titles
        switch (notificationType) {
            case "DESTINATION_CREATED":
                transformedTitle = assignedUsername + " Created a Destination";
                break;
            case "DESTINATION_UPDATED":
                transformedTitle = assignedUsername + " Updated a Destination";
                break;
            case "DESTINATION_TERMINATED":
                transformedTitle = assignedUsername + " Terminated a Destination";
                break;
            case "DESTINATION_CATEGORY_CREATED":
                transformedTitle = assignedUsername + " Created a Category";
                break;
            case "DESTINATION_CATEGORY_UPDATED":
                transformedTitle = assignedUsername + " Updated a Category";
                break;
            case "DESTINATION_CATEGORY_TERMINATED":
                transformedTitle = assignedUsername + " Terminated a Category";
                break;
        }

        return transformedTitle;
    }

    /**
     * Generic transformation for any other notification types
     */
    private String transformGenericMessage(String originalMessage, String assignedUsername) {
        String transformedMessage = originalMessage;

        // Replace first-person references with employee's name
        transformedMessage = transformedMessage.replaceAll("(?i)\\bI have\\b", assignedUsername + " has");
        transformedMessage = transformedMessage.replaceAll("(?i)\\bI've\\b", assignedUsername + " has");
        transformedMessage = transformedMessage.replaceAll("(?i)\\bI will\\b", assignedUsername + " will");
        transformedMessage = transformedMessage.replaceAll("(?i)\\bmy\\b", assignedUsername + "'s");
        transformedMessage = transformedMessage.replaceAll("(?i)\\bme\\b", assignedUsername);
        transformedMessage = transformedMessage.replaceAll("(?i)\\bI\\b", assignedUsername);

        // Add employee name at the beginning if no replacement was made
        if (transformedMessage.equals(originalMessage)) {
            transformedMessage = assignedUsername + " " + transformedMessage;
        }

        return transformedMessage;
    }

    @Override
    public CommonResponse<UpdateResponse> readNotification(ReadNotificationInsertRequest notificationInsertRequest) {
        LOGGER.info("Start update notifications read from repository");
        try {
            Long userId = getUserIdBySecurityContext();
            commonRepository.readNotification(notificationInsertRequest,userId);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update", notificationInsertRequest.getNotificationId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching notifications: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch notifications from database");
        } finally {
            LOGGER.info("End update notifications read from repository");
        }
    }

    @Override
    public CommonResponse<UnReadNotificationCountResponse> getAllUnReadNotifications() {
        LOGGER.info("Start get all unread notifications from repository");
        try {
            Long userId = getUserIdBySecurityContext();
            UnReadNotificationCountResponse unReadNotificationCountResponse = commonRepository.getAllUnReadNotifications(userId);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    unReadNotificationCountResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all unread notifications: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all unread notifications from database");
        } finally {
            LOGGER.info("End all unread notifications read from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> readAllUnreadNotifications() {
        LOGGER.info("Start update all unread notifications from repository");
        try {
            Long userId = getUserIdBySecurityContext();
            commonRepository.readAllUnreadNotifications(userId);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update", null),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while update all unread notifications: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update all unread notifications from database");
        } finally {
            LOGGER.info("End update all unread notifications from repository");
        }
    }

    @Override
    public List<String> getSupervisorEmailsWhichEnableNotificationForGiven(String name, List<Long> supervisorUserIds) {
        LOGGER.info("Start fetch supervisor email which allow the notification for given notification type");
        try {
            return commonRepository.getSupervisorEmailsWhichEnableNotificationForGiven(name,supervisorUserIds);

        } catch (Exception e) {
            return List.of();
        } finally {
            LOGGER.info("End fetch supervisor email which allow the notification for given notification type");
        }

    }


}
