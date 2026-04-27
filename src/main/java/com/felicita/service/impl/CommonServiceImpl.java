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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    notificationResponses,
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


}
