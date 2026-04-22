package com.felicita.service.impl;

import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.exception.UnAuthenticateErrorExceptionHandler;
import com.felicita.model.dto.ActivityResponseDto;
import com.felicita.model.response.AllCategoriesResponse;
import com.felicita.model.response.CommonResponse;
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


}
