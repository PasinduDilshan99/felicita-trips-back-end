package com.felicita.service.impl;

import com.felicita.email.PackageEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.*;
import com.felicita.model.other.PackageComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.PackageScheduleStatisticsResponse;
import com.felicita.model.response.statistics.PackageStatisticsResponse;
import com.felicita.model.response.statistics.PackageTypeStatisticsResponse;
import com.felicita.repository.PackageRepository;
import com.felicita.repository.WishListRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.PackageService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.PackageValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_PACKAGE_DETAILS;

@Service
public class PackageServiceImpl implements PackageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageServiceImpl.class);

    private final PackageRepository packageRepository;
    private final PackageValidationService packageValidationService;
    private final CommonService commonService;
    private final WishListRepository wishListRepository;
    private final PackageEmailHelperService packageEmailHelperService;
    private final EmailService emailService;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, PackageValidationService packageValidationService, CommonService commonService, WishListRepository wishListRepository, PackageEmailHelperService packageEmailHelperService, EmailService emailService) {
        this.packageRepository = packageRepository;
        this.packageValidationService = packageValidationService;
        this.commonService = commonService;
        this.wishListRepository = wishListRepository;
        this.packageEmailHelperService = packageEmailHelperService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<List<PackageResponseDto>> getAllPackages() {
        LOGGER.info("Start fetching all packages from repository");
        try {
            List<PackageResponseDto> packageResponseDtos = packageRepository.getAllPackages();

            if (packageResponseDtos.isEmpty()) {
                LOGGER.warn("No packages found in database");
                throw new DataNotFoundErrorExceptionHandler("No packages found");
            }

            LOGGER.info("Fetched {} packages successfully", packageResponseDtos.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageResponseDtos,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages from database");
        } finally {
            LOGGER.info("End fetching all packages from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageResponseDto>> getActivePackages() {
        LOGGER.info("Start fetching active packages from repository");
        try {
            List<PackageResponseDto> packageResponseDtos = getAllPackages().getData();

            List<PackageResponseDto> packageResponseDtoList = packageResponseDtos.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getPackageStatus()))
                    .toList();

            LOGGER.info("Fetched {} active packages successfully", packageResponseDtoList.size());
            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageResponseDtoList,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active packages: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active packages from database");
        } finally {
            LOGGER.info("End fetching active packages from repository");
        }
    }

    @Override
    public CommonResponse<PackageResponseDto> getPackageDetailsById(Long packageId) {
        LOGGER.info("Start fetching package details by id : {} from repository", packageId);
        try {
            PackageResponseDto packageResponseDto = packageRepository.getPackageDetailsById(packageId);

            if (packageResponseDto == null) {
                LOGGER.warn("No package found in by id : {} database", packageId);
                throw new DataNotFoundErrorExceptionHandler("No package found in by id : " + packageId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageResponseDto,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package by id : {} , {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching package by id : " + packageId);
        } finally {
            LOGGER.info("End fetching package by package id : {} from repository", packageId);
        }
    }

    @Override
    public CommonResponse<List<PackageReviewResponse>> getAllPackageReviewDetails() {
        LOGGER.info("Start fetching all packages review details from repository");
        try {
            List<PackageReviewResponse> packageReviewResponses = packageRepository.getAllPackageReviewDetails();

            if (packageReviewResponses.isEmpty()) {
                LOGGER.warn("No packages review details found in database");
                throw new DataNotFoundErrorExceptionHandler("No packages review details found");
            }

            List<PackageReviewResponse> packageResponseDtoList = packageReviewResponses.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getStatus()))
                    .toList();

            LOGGER.info("Fetched {} packages review details successfully", packageResponseDtoList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageResponseDtoList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages review details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages review details from database");
        } finally {
            LOGGER.info("End fetching all packages review details from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageReviewResponse>> getPackageReviewDetailsById(Long packageId) {
        LOGGER.info("Start fetching packages review details by package id : {} from repository", packageId);
        try {
            List<PackageReviewResponse> packageReviewResponse = packageRepository.getPackageReviewDetailsById(packageId);

            if (packageReviewResponse.isEmpty()) {
                LOGGER.warn("No packages review details by package id : {} found in database", packageId);
                throw new DataNotFoundErrorExceptionHandler("No packages review details by package id : " + packageId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageReviewResponse,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages review details by package id: {} , {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages review details by package id : "+ packageId);
        } finally {
            LOGGER.info("End fetching packages review details by package id : {} from repository", packageId);
        }
    }

    @Override
    public CommonResponse<List<PackageHistoryDetailsResponse>> getAllPackageHistoryDetails() {
        LOGGER.info("Start fetching all packages history details from repository");
        try {
            List<PackageHistoryDetailsResponse> packageHistoryDetailsResponses = packageRepository.getAllPackageHistoryDetails();

            if (packageHistoryDetailsResponses.isEmpty()) {
                LOGGER.warn("No packages history details found in database");
                throw new DataNotFoundErrorExceptionHandler("No packages history details found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageHistoryDetailsResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages history details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages history details from database");
        } finally {
            LOGGER.info("End fetching packages history details from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageHistoryDetailsResponse>> getPackageHistoryDetailsById(Long packageId) {
        LOGGER.info("Start fetching package history details by package id : {} from repository", packageId);
        try {
            List<PackageHistoryDetailsResponse> packageHistoryDetailsResponses = packageRepository.getPackageHistoryDetailsById(packageId);

            if (packageHistoryDetailsResponses.isEmpty()) {
                LOGGER.warn("No package history details by package id : {} found in database", packageId);
                throw new DataNotFoundErrorExceptionHandler("No package history details by package id : " + packageId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageHistoryDetailsResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package history details by package id : {} , {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package history details by package id : " + packageId);
        } finally {
            LOGGER.info("End fetching package history details by package id : {} from repository", packageId);
        }
    }

    @Override
    public CommonResponse<List<PackageHistoryImageResponse>> getAllPackageHistoryImages() {
        LOGGER.info("Start fetching all package history images from repository");
        try {
            List<PackageHistoryImageResponse> packageHistoryImageResponse = packageRepository.getAllPackageHistoryImages();

            if (packageHistoryImageResponse.isEmpty()) {
                LOGGER.warn("No package history images found in database");
                throw new DataNotFoundErrorExceptionHandler("No package history images found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageHistoryImageResponse,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package history images: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package history images from database");
        } finally {
            LOGGER.info("End fetching package history images from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageHistoryImageResponse>> getPackageHistoryImagesById(Long packageId) {
        LOGGER.info("Start fetching package history images by package id : {} from repository", packageId);
        try {
            List<PackageHistoryImageResponse> packageHistoryImageResponse = packageRepository.getPackageHistoryImagesById(packageId);

            if (packageHistoryImageResponse.isEmpty()) {
                LOGGER.warn("No package history images by package id : {} found in database", packageId);
                throw new DataNotFoundErrorExceptionHandler("No package history images by package id : " + packageId);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageHistoryImageResponse,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package history images by package id : {} , {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package history images by package id : " + packageId);
        } finally {
            LOGGER.info("End fetching package history images by package id : from repository", packageId);
        }
    }

    @Override
    public CommonResponse<PackageWithParamsResponse> getPackagesWithParams(PackageDataRequest packageDataRequest) {
        LOGGER.info("Start fetching packages with params from repository");
        try {
            PackageWithParamsResponse packageWithParamsResponse = packageRepository.getPackagesWithParams(packageDataRequest);

            Long userId = commonService.getUserIdBySecurityContextWithOutException();

            Set<Long> packageIdSet = new HashSet<>();
            if (userId != null) {
                List<Long> packageIds = wishListRepository.getAllPackageWishListByUserId(userId);
                if (packageIds != null) {
                    packageIdSet.addAll(packageIds);
                }
            }

            if (packageWithParamsResponse != null) {
                List<PackageResponseDto> packageResponseDtos = packageWithParamsResponse.getPackageResponseDtos();
                if (packageResponseDtos != null) {
                    for (PackageResponseDto packageResponseDto : packageResponseDtos) {
                        packageResponseDto.setWish(packageIdSet.contains(packageResponseDto.getPackageId()));
                    }
                }
            }

            if (packageWithParamsResponse == null) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                        null,
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageWithParamsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages with params from database");
        } finally {
            LOGGER.info("End fetching packages with params from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageDayAccommodationResponse>> getDayToPackageDetailsByTourId(Long tourId) {
        LOGGER.info("Start fetching day to day package details by tourId id : {} from repository", tourId);
        try {
            List<PackageDetailsDto> packageDetailsDtos = packageRepository.getDayToPackageDetailsById(tourId);
            LOGGER.info(packageDetailsDtos.toString());
            List<Long> packgeIds = packageDetailsDtos.stream()
                    .map(PackageDetailsDto::getPackageId)
                    .toList();

            List<PackageDayByDayDto> packageDayByDayDtos = packageRepository.getPackagesAccoamdationsByIds(packgeIds);

            List<PackageDayAccommodationResponse> responses = new ArrayList<>();

            for (PackageDetailsDto packageDetailsDto : packageDetailsDtos) {
                PackageDayAccommodationResponse packageDayAccommodationResponse = new PackageDayAccommodationResponse();
                packageDayAccommodationResponse.setPackageId(packageDetailsDto.getPackageId());
                packageDayAccommodationResponse.setPackageName(packageDetailsDto.getPackageName());
                packageDayAccommodationResponse.setPackageDescription(packageDetailsDto.getPackageDescription());
                packageDayAccommodationResponse.setTotalPrice(packageDetailsDto.getTotalPrice());
                packageDayAccommodationResponse.setPricePerPerson(packageDetailsDto.getPricePerPerson());
                packageDayAccommodationResponse.setDiscount(packageDetailsDto.getDiscount());
                packageDayAccommodationResponse.setColor(packageDetailsDto.getColor());
                packageDayAccommodationResponse.setHoverColor(packageDetailsDto.getHoverColor());

                List<PackageDayByDayDto> packageDayByDayDtoList = new ArrayList<>();
                for (PackageDayByDayDto packageDayByDayDto : packageDayByDayDtos) {
                    if (packageDayByDayDto.getPackageId().equals(packageDetailsDto.getPackageId())) {
                        packageDayByDayDtoList.add(packageDayByDayDto);
                    }
                }
                packageDayAccommodationResponse.setPackageDayByDayDtoList(packageDayByDayDtoList);
                responses.add(packageDayAccommodationResponse);
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            responses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching day to day package details by tourId id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch day to day package details by tourId id : " + tourId);
        } finally {
            LOGGER.info("End fetching day to day package details by tourId id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<PackageExtrasResponse>> getPackageExtraDetailsDayByDay(Long tourId) {
        LOGGER.info("Start fetching package extra details by tour id : {} from repository", tourId);
        try {
            List<Long> packageIds = packageRepository.getPackageIdsByTourId(tourId);

            List<PackageExtrasResponse> responses = new ArrayList<>();
            for (Long packageId : packageIds) {
                List<PackageExtrasResponse.PackageInclusion> inclusions = packageRepository.getPackageInclusions(packageId);
                List<PackageExtrasResponse.PackageExclusion> exclusions = packageRepository.getPackageExclusions(packageId);
                List<PackageExtrasResponse.PackageCondition> conditions = packageRepository.getPackageConditions(packageId);
                List<PackageExtrasResponse.PackageTravelTip> travelTips = packageRepository.getPackageTravelTips(packageId);

                PackageExtrasResponse packageExtrasResponse = new PackageExtrasResponse(
                        packageId,
                        inclusions,
                        exclusions,
                        conditions,
                        travelTips
                );

                responses.add(packageExtrasResponse);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    responses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package extra details by tour id: {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package extra details by tour id : "+ tourId);
        } finally {
            LOGGER.info("End fetching package extra details by tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<PackageScheduleResponse>> getPackageSchedulesByTourId(Long tourId) {
        LOGGER.info("Start fetching package schedules by tour id : {} from repository", tourId);
        try {
            List<Long> packageIds = packageRepository.getPackageIdsByTourId(tourId);

            List<PackageScheduleResponse> responses = new ArrayList<>();
            for (Long packageId : packageIds) {
                List<PackageScheduleResponse.PackageScheduleDetails> scheduleDetails =
                        packageRepository.getPackageSchedulesById(packageId);

                PackageScheduleResponse packageScheduleResponse = new PackageScheduleResponse(
                        packageId,
                        scheduleDetails
                );

                responses.add(packageScheduleResponse);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    responses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package schedules by tour id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package schedules by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching package schedules by tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<PackageScheduleDetailsResponse> getPackageSchedulesForId(Long packageId) {
        LOGGER.info("Start fetching package schedules by package id : {} from the repository", packageId);
        try {
            List<PackageScheduleDetailsResponse.PackageScheduleDetails> scheduleDetails =
                    packageRepository.getPackageSchedulesForId(packageId);
            PackageScheduleDetailsResponse.PackageBasicDetails packageBasicDetails =
                    packageRepository.getPackageBasicDetails(packageId);


            PackageScheduleDetailsResponse response = new PackageScheduleDetailsResponse(
                    packageBasicDetails,
                    scheduleDetails
            );

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    response,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package schedules by package id: {}, {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package schedules by package id : " + packageId);
        } finally {
            LOGGER.info("End fetching all package schedules by package id : {} from repository", packageId);
        }
    }

    @Override
    public CommonResponse<List<PackageComapreResponse>> getDayToPackageDetailsForComapreByTourId(Long tourId) {
        LOGGER.info("Start fetching package for compare by tour id : {} from the repository", tourId);
        try {
            List<PackageComapreResponse> comapreResponses = new ArrayList<>();
            List<PackageExtrasResponse> extrasResponses = getPackageExtraDetailsDayByDay(tourId).getData();
            List<PackageDayAccommodationResponse> accommodationResponses = getDayToPackageDetailsByTourId(tourId).getData();
            List<PackageComapreResponse.PackageImages> images = packageRepository.getAllPackagesImages(tourId);

            for (PackageDayAccommodationResponse accommodationResponse : accommodationResponses) {
                PackageComapreResponse packageComapreResponse = new PackageComapreResponse();
                packageComapreResponse.setPackageId(accommodationResponse.getPackageId());
                packageComapreResponse.setPackageName(accommodationResponse.getPackageName());
                packageComapreResponse.setPackageDescription(accommodationResponse.getPackageDescription());
                packageComapreResponse.setTotalPrice(accommodationResponse.getTotalPrice());
                packageComapreResponse.setPricePerPerson(accommodationResponse.getPricePerPerson());
                packageComapreResponse.setDiscount(accommodationResponse.getDiscount());
                packageComapreResponse.setColor(accommodationResponse.getColor());
                packageComapreResponse.setHoverColor(accommodationResponse.getHoverColor());
                packageComapreResponse.setPackageDayByDayDtoList(accommodationResponse.getPackageDayByDayDtoList());

                for (PackageExtrasResponse extrasResponse : extrasResponses) {
                    if (extrasResponse.getPackageId().equals(accommodationResponse.getPackageId())) {
                        packageComapreResponse.setExtraDetails(extrasResponse);
                    }
                }

                List<PackageComapreResponse.PackageImages> packageImages = new ArrayList<>();
                for (PackageComapreResponse.PackageImages image : images) {
                    if (image.getPackageId().equals(accommodationResponse.getPackageId())) {
                        packageImages.add(image);
                    }
                }
                packageComapreResponse.setImages(packageImages);
                comapreResponses.add(packageComapreResponse);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    comapreResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package for compare by tour id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package for compare by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching package for compare by tour id : {} from repository", tourId);
        }
    }

    @Override
    public PackageBasicDetailsDto getPackageBasicDetailsByScheduleId(Long packageScheduleId) {
        LOGGER.info("Start fetching package basic details by schedule id : {} from repository", packageScheduleId);
        try {
            PackageBasicDetailsDto response = packageRepository.getPackageBasicDetailsByScheduleId(packageScheduleId);
            LOGGER.info("Successfully fetched package basic details by schedule id : {} from repository.", packageScheduleId);
            return response;

        } catch (DataNotFoundErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages basic details by schedule id : {} , {}", packageScheduleId,e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching packages basic details by schedule id : " + packageScheduleId);
        } finally {
            LOGGER.info("End fetching packages basic details by schedule id : {}  from repository", packageScheduleId);
        }
    }

    @Override
    public List<PackageActivityPriceDto> getPackageActivityPriceByScheduleId(Long packageScheduleId) {
        LOGGER.info("Start fetching package activity price by schedule id : {} from repository", packageScheduleId);
        try {
            List<PackageActivityPriceDto> response = packageRepository.getPackageActivityPriceByScheduleId(packageScheduleId);
            LOGGER.info("Successfully fetched package activity price by schedule id : {}  from repository.", packageScheduleId);
            return response;
        } catch (DataNotFoundErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages activity price by schedule id : {} , {}", packageScheduleId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching packages activity price by schedule id : "+ packageScheduleId);
        } finally {
            LOGGER.info("End fetching package activity price by schedule id : {}  from repository", packageScheduleId);
        }
    }

    @Override
    public List<PackageDestinationExtraPriceDto> getPackageDestinationExtraPriceByScheduleId(Long packageScheduleId) {
        LOGGER.info("Start fetching package destination extra price by schedule id : {} from repository", packageScheduleId);
        try {
            List<PackageDestinationExtraPriceDto> response = packageRepository.getPackageDestinationExtraPriceByScheduleId(packageScheduleId);
            LOGGER.info("Successfully fetched package destination extra price by schedule id : {} from repository.", packageScheduleId);
            return response;
        } catch (DataNotFoundErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching destination extra price by schedule id : {} , {}", packageScheduleId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination extra price by schedule id : {} from database"+ packageScheduleId);
        } finally {
            LOGGER.info("End fetching packages destination extra price by schedule id : {} from repository", packageScheduleId);
        }
    }

    @Override
    public List<PackageDayAccommodationPriceDto> getPackageDayAccommodationPriceByScheduleId(Long packageScheduleId) {
        LOGGER.info("Start fetching package day accommodation price by schedule id : {} from repository", packageScheduleId);
        try {
            List<PackageDayAccommodationPriceDto> response = packageRepository.getPackageDayAccommodationPriceByScheduleId(packageScheduleId);
            LOGGER.info("Successfully fetched package day accommodation price by schedule id : {} from repository.", packageScheduleId);
            return response;
        } catch (DataNotFoundErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package day accommodation price by schedule id : {} , {}", packageScheduleId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package day accommodation price by schedule id : {} from database" + packageScheduleId);
        } finally {
            LOGGER.info("End fetching package day accommodation price by schedule id : {} from repository", packageScheduleId);
        }
    }

    @Override
    public CommonResponse<List<PackageForTerminateResponse>> getPackagesForTerminate() {
        LOGGER.info("Start fetching packages for terminate from repository");
        try {
            List<PackageForTerminateResponse> packageForTerminateResponses =
                    packageRepository.getPackagesForTerminate();

            if (packageForTerminateResponses.isEmpty()) {
                LOGGER.warn("No packages for terminate found in database");
                throw new DataNotFoundErrorExceptionHandler("No packages for terminate found");
            }

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageForTerminateResponses,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching packages for terminate: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch packages for terminate from database");
        } finally {
            LOGGER.info("End fetching packages for terminate from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminatePackage(PackageTerminateRequest packageTerminateRequest) {
        LOGGER.info("Start execute terminate package request.");
        try {
            packageValidationService.validateTerminatePackageRequest(packageTerminateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            PackageResponseDto packageResponseDto = getPackageDetailsById(packageTerminateRequest.getPackageId()).getData();
            packageRepository.terminatePackage(packageTerminateRequest, userId);
            packageRepository.removeAllPackageImages(packageTerminateRequest.getPackageId(), userId);
            packageRepository.removeAllPackageFeatures(packageTerminateRequest.getPackageId(), userId);
            packageRepository.removeAllDayByDayAccommodations(packageTerminateRequest.getPackageId(), userId);
            packageRepository.removeAllPcakageInclusions(packageTerminateRequest.getPackageId(),  userId);
            packageRepository.removeAllPackageExclusions(packageTerminateRequest.getPackageId(), userId);
            packageRepository.removeAllPcakageConditions(packageTerminateRequest.getPackageId(), userId);
            packageRepository.removeAllPcakageTravelTips(packageTerminateRequest.getPackageId(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PACKAGE_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Package Terminated")
                    .message("The package '" + packageResponseDto.getPackageName() + "' has been terminated.")
                    .actionUrl(VIEW_PACKAGE_DETAILS + "/" + packageResponseDto.getPackageId())
                    .actionText("View Package")
                    .icon("PackageX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "packageId", packageResponseDto.getPackageId(),
                            "packageName", packageResponseDto.getPackageName(),
                            "status", packageResponseDto.getPackageStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PACKAGE_TERMINATE.name())
                    .sourceModule(SourceModule.PACKAGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PACKAGE_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = packageEmailHelperService.buildPackageTerminateSuccessfullSubject(loggedUser, packageResponseDto);
            String body = packageEmailHelperService.buildPackageTerminateSuccessfullBody(loggedUser, packageResponseDto);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                            new TerminateResponse("Successfully terminate package request"),
                            Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate package request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertPackage(PackageInsertRequest packageInsertRequest) {
        LOGGER.info("Start execute insert package request.");
        try {
            packageValidationService.validatePackageInsertRequest(packageInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long packageId = packageRepository.insertPackageDeails(packageInsertRequest, userId);

            packageRepository.insertPackageImages(packageId, packageInsertRequest.getImages(), userId);
            packageRepository.insertPackageFeatures(packageId, packageInsertRequest.getAddFeatures(), userId);
            packageRepository.insertPackageInclusions(packageId, packageInsertRequest.getInclusions(), userId);
            packageRepository.insertPackageExclusions(packageId, packageInsertRequest.getExclusions(), userId);
            packageRepository.insertPackageConditions(packageId, packageInsertRequest.getConditions(), userId);
            packageRepository.insertPackageTravelTips(packageId, packageInsertRequest.getTravelTips(), userId);
            packageRepository.insertDayByDayAccommodations(packageId, packageInsertRequest.getDayAccommodations(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PACKAGE_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Package Created")
                    .message("A new package '" + packageInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_PACKAGE_DETAILS + "/" + packageId)
                    .actionText("View Package")
                    .icon("Package")
                    .color("#10B981")
                    .metadata(Map.of(
                            "packageId", packageId,
                            "packageName", packageInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PACKAGE_CREATE.name())
                    .sourceModule(SourceModule.PACKAGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (packageId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PACKAGE_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = packageEmailHelperService.buildPackageCreateSuccessfullBody(packageInsertRequest, packageId, loggedUser);
                String subject = packageEmailHelperService.buildPackageCreateSuccessfullSubject(packageInsertRequest,packageId, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert package request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert package request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updatePackage(PackageUpdateRequest packageUpdateRequest) {
        LOGGER.info("Start execute update package request.");
        try {
            packageValidationService.validatePackageUpdateRequest(packageUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            PackageAllDetailsResponse previousPackage = getPackageAllDetailsById(packageUpdateRequest.getPackageId()).getData();

            packageRepository.updatePackageBasicDetails(packageUpdateRequest.getPackageId(), packageUpdateRequest.getPackageBasicDetails(), userId);

            packageRepository.insertPackageImages(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddImages(), userId);
            packageRepository.removePackageImages(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemovedImageIds(), userId);
            packageRepository.updatePackageImages(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedImages(), userId);

            packageRepository.insertPackageFeatures(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddFeatures(), userId);
            packageRepository.removePackageFeatures(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveFeatureIds(), userId);
            packageRepository.updatePackageFeatures(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedFeatures(), userId);

            packageRepository.insertDayByDayAccommodations(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddDayAccommodations(), userId);
            packageRepository.removeDayByDayAccommodations(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveDayAccommodationIds(), userId);
            packageRepository.updateDayByDayAccommodations(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedDayAccommodations(), userId);

            packageRepository.insertPackageInclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddInclusions(), userId);
            packageRepository.removePcakageInclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveInclusionIds(), userId);
            packageRepository.updatePackageInclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedInclusions(), userId);

            packageRepository.insertPackageExclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddExclusions(), userId);
            packageRepository.removePackageExclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveExclusionIds(), userId);
            packageRepository.updatePackageExclusions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedExclusions(), userId);

            packageRepository.insertPackageConditions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddConditions(), userId);
            packageRepository.removePcakageConditions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveConditionIds(), userId);
            packageRepository.updatePackageConditions(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedConditions(), userId);

            packageRepository.insertPackageTravelTips(packageUpdateRequest.getPackageId(), packageUpdateRequest.getAddTravelTips(), userId);
            packageRepository.removePcakageTravelTips(packageUpdateRequest.getPackageId(), packageUpdateRequest.getRemoveTravelTipIds(), userId);
            packageRepository.updatePackageTravelTips(packageUpdateRequest.getPackageId(), packageUpdateRequest.getUpdatedTravelTips(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PACKAGE_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Package Updated")
                    .message("The package '" + packageUpdateRequest.getPackageBasicDetails().getName() + "' has been updated.")
                    .actionUrl(VIEW_PACKAGE_DETAILS + "/" + packageUpdateRequest.getPackageId())
                    .actionText("View Package")
                    .icon("Package")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "packageId", packageUpdateRequest.getPackageId(),
                            "packageName", packageUpdateRequest.getPackageBasicDetails().getName(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PACKAGE_UPDATE.name())
                    .sourceModule(SourceModule.PACKAGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            PackageComparisonResult comparisonResult = comparePackageUpdates(
                    packageUpdateRequest,
                    previousPackage
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PACKAGE_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = packageEmailHelperService.buildPackageUpdateSuccessfullSubject(loggedUser,packageUpdateRequest.getPackageBasicDetails().getName());
            String body = packageEmailHelperService.buildPackageUpdateSuccessfullBody(loggedUser, comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update package request", packageUpdateRequest.getPackageId()),
                    Instant.now());
        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the update package request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler("before login to update the package details");
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<PackageAllDetailsResponse> getPackageAllDetailsById(Long packageId) {
        LOGGER.info("Start fetching all package details by package id : {} from repository", packageId);
        try {
            Long userId = commonService.getUserIdBySecurityContextWithOutException();
            Boolean isWished =false;
            if (userId != null){
                 isWished = wishListRepository.isWishThisPackageByUserId(packageId,userId);
            }
            PackageAllDetailsResponse packageAllDetailsResponse = new PackageAllDetailsResponse();
            PackageResponseDto packageDetailsById = getPackageDetailsById(packageId).getData();

            packageAllDetailsResponse.setPackageId(packageId);
            packageAllDetailsResponse.setPackageName(packageDetailsById.getPackageName());
            packageAllDetailsResponse.setPackageDescription(packageDetailsById.getPackageDescription());
            packageAllDetailsResponse.setTotalPrice(packageDetailsById.getTotalPrice());
            packageAllDetailsResponse.setDiscountPercentage(packageDetailsById.getDiscountPercentage());
            packageAllDetailsResponse.setStartDate(packageDetailsById.getStartDate());
            packageAllDetailsResponse.setEndDate(packageDetailsById.getEndDate());
            packageAllDetailsResponse.setColor(packageDetailsById.getColor());
            packageAllDetailsResponse.setHoverColor(packageDetailsById.getHoverColor());
            packageAllDetailsResponse.setMinPersonCount(packageDetailsById.getMinPersonCount());
            packageAllDetailsResponse.setMaxPersonCount(packageDetailsById.getMaxPersonCount());
            packageAllDetailsResponse.setPricePerPerson(packageDetailsById.getPricePerPerson());
            packageAllDetailsResponse.setPackageStatus(packageDetailsById.getPackageStatus());
            packageAllDetailsResponse.setPackageTypeName(packageDetailsById.getPackageTypeName());
            packageAllDetailsResponse.setTourId(packageDetailsById.getTourId());
            packageAllDetailsResponse.setTourName(packageDetailsById.getTourName());
            packageAllDetailsResponse.setPackageFeatures(packageDetailsById.getFeatures());
            packageAllDetailsResponse.setPackageImages(packageDetailsById.getImages());
            packageAllDetailsResponse.setIsWished(isWished);


            List<PackageExtrasResponse.PackageInclusion> packageInclusions = packageRepository.getPackageInclusions(packageId);
            List<PackageExtrasResponse.PackageExclusion> packageExclusions = packageRepository.getPackageExclusions(packageId);
            List<PackageExtrasResponse.PackageCondition> packageConditions = packageRepository.getPackageConditions(packageId);
            List<PackageExtrasResponse.PackageTravelTip> packageTravelTips = packageRepository.getPackageTravelTips(packageId);

            packageAllDetailsResponse.setInclusions(packageInclusions);
            packageAllDetailsResponse.setExclusions(packageExclusions);
            packageAllDetailsResponse.setConditions(packageConditions);
            packageAllDetailsResponse.setTravelTips(packageTravelTips);

            PackageDayAccommodationResponse dayToPackageDetailsByTourId =
                    getDayToPackageDetailsByTourId(packageAllDetailsResponse.getTourId()).getData().stream().filter(data -> data.getPackageId().equals(packageId)).findFirst().get();

            packageAllDetailsResponse.setDayAccommodationResponses(dayToPackageDetailsByTourId);

            return new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            packageAllDetailsResponse,
                            Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all package details by package id : {} , {}", packageId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all package details by package id : " + packageId);
        } finally {
            LOGGER.info("End fetching all package details by package id : {} from repository", packageId);
        }
    }

    @Override
    public CommonResponse<List<PackageIdAndPackageNameResponse>> getPackageIdsAndPackageNames() {
        CommonResponse<List<PackageForTerminateResponse>> packagesForTerminate = getPackagesForTerminate();
        List<PackageIdAndPackageNameResponse> packageIdAndPackageNameResponses = new ArrayList<>();
        for (PackageForTerminateResponse packageForTerminateResponse : packagesForTerminate.getData()) {
            packageIdAndPackageNameResponses.add(
                    new PackageIdAndPackageNameResponse(
                            packageForTerminateResponse.getPackageId(),
                            packageForTerminateResponse.getPackageName())
            );
        }

        return new CommonResponse<>(
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                packageIdAndPackageNameResponses,
                Instant.now());
    }

    @Override
    public CommonResponse<PackageStatisticsResponse> getPackageStatistics() {
        LOGGER.info("Start fetching package statistics from repository");
        try {
            PackageStatisticsResponse packageStatisticsResponse = new PackageStatisticsResponse();

            PackageStatisticsResponse.Summary summary = packageRepository.getPackageSummaryStatistics();
            List<PackageStatisticsResponse.PackagePopularity> packagePopularities = packageRepository.getPackagePopularityStatistics();
            List<PackageStatisticsResponse.PackageRatingOverview> packageRatingOverviews = packageRepository.getPackageRatingOverviewStatistics();
            List<PackageStatisticsResponse.PackagePriceDistribution> packagePriceDistributions = packageRepository.getPackagePriceDistributionStatistics();
            List<PackageStatisticsResponse.PackageCapacityUtilization> packageCapacityUtilizations = packageRepository.getPackageCapacityUtilizationStatistics();
            List<PackageStatisticsResponse.PackageTypeDistribution> packageTypeDistributions = packageRepository.getPackageTypeDistributionStatistics();

            // Set all the data to the response object
            packageStatisticsResponse.setSummary(summary);
            packageStatisticsResponse.setPackagePopularities(packagePopularities);
            packageStatisticsResponse.setPackageRatingOverviews(packageRatingOverviews);
            packageStatisticsResponse.setPackagePriceDistributions(packagePriceDistributions);
            packageStatisticsResponse.setPackageCapacityUtilizations(packageCapacityUtilizations);
            packageStatisticsResponse.setPackageTypeDistributions(packageTypeDistributions);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package statistics from database");
        } finally {
            LOGGER.info("End fetching package statistics from repository");
        }
    }

    @Override
    public CommonResponse<PackageScheduleStatisticsResponse> getPackageScheduleStatistics() {
        LOGGER.info("Start fetching package schedule statistics from repository");
        try {
            PackageScheduleStatisticsResponse packageScheduleStatisticsResponse = new PackageScheduleStatisticsResponse();

            // Fetch all the required statistics from packageRepository
            PackageScheduleStatisticsResponse.Summary summary = packageRepository.getPackageScheduleSummaryStatistics();
            List<PackageScheduleStatisticsResponse.ScheduleTimeline> scheduleTimelines = packageRepository.getPackageScheduleTimelineStatistics();
            List<PackageScheduleStatisticsResponse.ScheduleStatusDistribution> scheduleStatusDistributions = packageRepository.getPackageScheduleStatusDistributionStatistics();
            List<PackageScheduleStatisticsResponse.DurationDistribution> durationDistributions = packageRepository.getPackageScheduleDurationDistributionStatistics();
            List<PackageScheduleStatisticsResponse.ScheduleParticipationPerformance> scheduleParticipationPerformances = packageRepository.getPackageScheduleParticipationPerformanceStatistics();
            List<PackageScheduleStatisticsResponse.ScheduleRatingOverview> scheduleRatingOverviews = packageRepository.getPackageScheduleRatingOverviewStatistics();

            // Set all the data to the response object
            packageScheduleStatisticsResponse.setSummary(summary);
            packageScheduleStatisticsResponse.setScheduleTimelines(scheduleTimelines);
            packageScheduleStatisticsResponse.setScheduleStatusDistributions(scheduleStatusDistributions);
            packageScheduleStatisticsResponse.setDurationDistributions(durationDistributions);
            packageScheduleStatisticsResponse.setScheduleParticipationPerformances(scheduleParticipationPerformances);
            packageScheduleStatisticsResponse.setScheduleRatingOverviews(scheduleRatingOverviews);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageScheduleStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package schedule statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package schedule statistics from database");
        } finally {
            LOGGER.info("End fetching package schedule statistics from repository");
        }
    }

    @Override
    public CommonResponse<PackageTypeStatisticsResponse> getPackageTypeStatistics() {
        LOGGER.info("Start fetching package type statistics from repository");
        try {
            PackageTypeStatisticsResponse packageTypeStatisticsResponse = new PackageTypeStatisticsResponse();

            // Fetch all the required statistics from packageRepository
            PackageTypeStatisticsResponse.Summary summary = packageRepository.getPackageTypeSummaryStatistics();
            List<PackageTypeStatisticsResponse.TypeDistribution> typeDistributions = packageRepository.getPackageTypesDistributionStatistics();
            List<PackageTypeStatisticsResponse.TypeRevenuePerformance> typeRevenuePerformances = packageRepository.getPackageTypeRevenuePerformanceStatistics();
            List<PackageTypeStatisticsResponse.TypeParticipationImpact> typeParticipationImpacts = packageRepository.getPackageTypeParticipationImpactStatistics();
            List<PackageTypeStatisticsResponse.TypePrimarySecondaryUsage> typePrimarySecondaryUsages = packageRepository.getPackageTypePrimarySecondaryUsageStatistics();
            List<PackageTypeStatisticsResponse.TypeBookingPerformance> typeBookingPerformances = packageRepository.getPackageTypeBookingPerformanceStatistics();
            List<PackageTypeStatisticsResponse.TypeRatingOverview> typeRatingOverviews = packageRepository.getPackageTypeRatingOverviewStatistics();

            // Set all the data to the response object
            packageTypeStatisticsResponse.setSummary(summary);
            packageTypeStatisticsResponse.setTypeDistributions(typeDistributions);
            packageTypeStatisticsResponse.setTypeRevenuePerformances(typeRevenuePerformances);
            packageTypeStatisticsResponse.setTypeParticipationImpacts(typeParticipationImpacts);
            packageTypeStatisticsResponse.setTypePrimarySecondaryUsages(typePrimarySecondaryUsages);
            packageTypeStatisticsResponse.setTypeBookingPerformances(typeBookingPerformances);
            packageTypeStatisticsResponse.setTypeRatingOverviews(typeRatingOverviews);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageTypeStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching package type statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package type statistics from database");
        } finally {
            LOGGER.info("End fetching package type statistics from repository");
        }
    }

    @Override
    public CommonResponse<AddPackageParamResponse> getInsertPackageParams(AddPackageParamRequest addPackageParamRequest) {
        LOGGER.info("Start fetching get params for insert package from repository");
        try {
            AddPackageParamResponse addPackageParamResponse = new AddPackageParamResponse();
            List<HotelsNamesAndIdsDto> hotelsNamesAndIdsDtos = packageRepository.getHotelNamesAndIds(addPackageParamRequest);
            List<VehicleNumberIdTypeDto> vehicleNumberIdTypeDtos = packageRepository.getVehicleNumberIdType(addPackageParamRequest);
            List<String> inclusions = packageRepository.getTourInclusionsNames(addPackageParamRequest);
            List<String> exclusions = packageRepository.getTourExclusionsNames(addPackageParamRequest);
            List<String> conditions = packageRepository.getTourConditions(addPackageParamRequest);
            List<AddPackageParamResponse.TravelTips> travelTips = packageRepository.getTourTravelTips(addPackageParamRequest);

            addPackageParamResponse.setHotelsNamesAndIdsDtos(hotelsNamesAndIdsDtos);
            addPackageParamResponse.setVehicleNumberIdTypeDtos(vehicleNumberIdTypeDtos);
            addPackageParamResponse.setInclusions(inclusions);
            addPackageParamResponse.setExclusions(exclusions);
            addPackageParamResponse.setConditions(conditions);
            addPackageParamResponse.setTravelTips(travelTips);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    addPackageParamResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching get params for insert package: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch get params for insert package from database");
        } finally {
            LOGGER.info("End fetching get params for insert package from repository");
        }
    }

    private PackageComparisonResult comparePackageUpdates(
            PackageUpdateRequest packageUpdateRequest,
            PackageAllDetailsResponse previousPackage) {

        PackageComparisonResult.PackageComparisonResultBuilder resultBuilder =
                PackageComparisonResult.builder();

        List<PackageComparisonResult.FieldChange> fieldChanges = new ArrayList<>();

        // Compare basic details if present
        if (packageUpdateRequest.getPackageBasicDetails() != null) {
            PackageUpdateRequest.PackageBasicDetails basicDetails = packageUpdateRequest.getPackageBasicDetails();

            if (basicDetails.getPackageType() != null &&
                    !basicDetails.getPackageType().equals(previousPackage.getPackageTypeName())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "packageType", previousPackage.getPackageTypeName(), basicDetails.getPackageType()));
            }

            if (basicDetails.getTourId() != null &&
                    !basicDetails.getTourId().equals(previousPackage.getTourId())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "tourId", previousPackage.getTourId(), basicDetails.getTourId()));
            }

            if (basicDetails.getName() != null &&
                    !basicDetails.getName().equals(previousPackage.getPackageName())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "packageName", previousPackage.getPackageName(), basicDetails.getName()));
            }

            if (basicDetails.getDescription() != null &&
                    !basicDetails.getDescription().equals(previousPackage.getPackageDescription())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "packageDescription", previousPackage.getPackageDescription(), basicDetails.getDescription()));
            }

            if (basicDetails.getTotalPrice() != null &&
                    !basicDetails.getTotalPrice().equals(previousPackage.getTotalPrice())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "totalPrice", previousPackage.getTotalPrice(), basicDetails.getTotalPrice()));
            }

            if (basicDetails.getDiscountPercentage() != null &&
                    !basicDetails.getDiscountPercentage().equals(previousPackage.getDiscountPercentage())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "discountPercentage", previousPackage.getDiscountPercentage(), basicDetails.getDiscountPercentage()));
            }

            if (basicDetails.getStartDate() != null &&
                    !basicDetails.getStartDate().equals(previousPackage.getStartDate())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "startDate", previousPackage.getStartDate(), basicDetails.getStartDate()));
            }

            if (basicDetails.getEndDate() != null &&
                    !basicDetails.getEndDate().equals(previousPackage.getEndDate())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "endDate", previousPackage.getEndDate(), basicDetails.getEndDate()));
            }

            if (basicDetails.getColor() != null &&
                    !basicDetails.getColor().equals(previousPackage.getColor())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "color", previousPackage.getColor(), basicDetails.getColor()));
            }

            if (basicDetails.getStatus() != null &&
                    !basicDetails.getStatus().equals(previousPackage.getPackageStatus())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "packageStatus", previousPackage.getPackageStatus(), basicDetails.getStatus()));
            }

            if (basicDetails.getHoverColor() != null &&
                    !basicDetails.getHoverColor().equals(previousPackage.getHoverColor())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "hoverColor", previousPackage.getHoverColor(), basicDetails.getHoverColor()));
            }

            if (basicDetails.getMinPersonCount() != null &&
                    !basicDetails.getMinPersonCount().equals(previousPackage.getMinPersonCount())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "minPersonCount", previousPackage.getMinPersonCount(), basicDetails.getMinPersonCount()));
            }

            if (basicDetails.getMaxPersonCount() != null &&
                    !basicDetails.getMaxPersonCount().equals(previousPackage.getMaxPersonCount())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "maxPersonCount", previousPackage.getMaxPersonCount(), basicDetails.getMaxPersonCount()));
            }

            if (basicDetails.getPricePerPerson() != null &&
                    !basicDetails.getPricePerPerson().equals(previousPackage.getPricePerPerson())) {
                fieldChanges.add(new PackageComparisonResult.FieldChange(
                        "pricePerPerson", previousPackage.getPricePerPerson(), basicDetails.getPricePerPerson()));
            }
        }

        resultBuilder.basicDetailsChanges(fieldChanges);

        // Images changes
        resultBuilder.imageIdsToRemove(packageUpdateRequest.getRemovedImageIds() != null ?
                packageUpdateRequest.getRemovedImageIds() : Collections.emptyList());

        if (packageUpdateRequest.getAddImages() != null) {
            List<PackageComparisonResult.PackageImageChange> imagesToAdd =
                    packageUpdateRequest.getAddImages().stream()
                            .map(img -> PackageComparisonResult.PackageImageChange.builder()
                                    .name(img.getName())
                                    .description(img.getDescription())
                                    .status(img.getStatus())
                                    .imageUrl(img.getImageUrl())
                                    .color(img.getColor())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToAdd(imagesToAdd);
        }

        if (packageUpdateRequest.getUpdatedImages() != null) {
            List<PackageComparisonResult.PackageImageChange> imagesToUpdate =
                    packageUpdateRequest.getUpdatedImages().stream()
                            .map(img -> PackageComparisonResult.PackageImageChange.builder()
                                    .imageId(img.getImageId())
                                    .name(img.getImageName())
                                    .description(img.getImageDescription())
                                    .status(img.getStatus())
                                    .imageUrl(img.getImageUrl())
                                    .color(img.getColor())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToUpdate(imagesToUpdate);
        }

        // Features changes
        if (packageUpdateRequest.getAddFeatures() != null) {
            List<PackageComparisonResult.PackageFeatureChange> featuresToAdd =
                    packageUpdateRequest.getAddFeatures().stream()
                            .map(feature -> PackageComparisonResult.PackageFeatureChange.builder()
                                    .featureName(feature.getFeatureName())
                                    .featureValue(feature.getFeatureValue())
                                    .featureDescription(feature.getFeatureDescription())
                                    .status(feature.getStatus())
                                    .color(feature.getColor())
                                    .hoverColor(feature.getHoverColor())
                                    .specialNote(feature.getSpecialNote())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.featuresToAdd(featuresToAdd);
        }

        resultBuilder.featureIdsToRemove(packageUpdateRequest.getRemoveFeatureIds() != null ?
                packageUpdateRequest.getRemoveFeatureIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedFeatures() != null) {
            List<PackageComparisonResult.PackageFeatureChange> featuresToUpdate =
                    packageUpdateRequest.getUpdatedFeatures().stream()
                            .map(feature -> PackageComparisonResult.PackageFeatureChange.builder()
                                    .featureId(feature.getFeatureId())
                                    .featureName(feature.getFeatureName())
                                    .featureValue(feature.getFeatureValue())
                                    .featureDescription(feature.getFeatureDescription())
                                    .status(feature.getStatus())
                                    .color(feature.getColor())
                                    .hoverColor(feature.getHoverColor())
                                    .specialNote(feature.getSpecialNote())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.featuresToUpdate(featuresToUpdate);
        }

        // Day Accommodations changes
        if (packageUpdateRequest.getAddDayAccommodations() != null) {
            List<PackageComparisonResult.PackageDayAccommodationChange> dayAccommodationsToAdd =
                    packageUpdateRequest.getAddDayAccommodations().stream()
                            .map(accomm -> PackageComparisonResult.PackageDayAccommodationChange.builder()
                                    .dayNumber(accomm.getDayNumber())
                                    .breakfast(accomm.getBreakfast())
                                    .breakfastDescription(accomm.getBreakfastDescription())
                                    .lunch(accomm.getLunch())
                                    .lunchDescription(accomm.getLunchDescription())
                                    .dinner(accomm.getDinner())
                                    .dinnerDescription(accomm.getDinnerDescription())
                                    .morningTea(accomm.getMorningTea())
                                    .morningTeaDescription(accomm.getMorningTeaDescription())
                                    .eveningTea(accomm.getEveningTea())
                                    .eveningTeaDescription(accomm.getEveningTeaDescription())
                                    .snacks(accomm.getSnacks())
                                    .snackNote(accomm.getSnackNote())
                                    .hotelId(accomm.getHotelId())
                                    .transportId(accomm.getTransportId())
                                    .otherNotes(accomm.getOtherNotes())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.dayAccommodationsToAdd(dayAccommodationsToAdd);
        }

        resultBuilder.dayAccommodationIdsToRemove(packageUpdateRequest.getRemoveDayAccommodationIds() != null ?
                packageUpdateRequest.getRemoveDayAccommodationIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedDayAccommodations() != null) {
            List<PackageComparisonResult.PackageDayAccommodationChange> dayAccommodationsToUpdate =
                    packageUpdateRequest.getUpdatedDayAccommodations().stream()
                            .map(accomm -> PackageComparisonResult.PackageDayAccommodationChange.builder()
                                    .packageDayAccommodationId(accomm.getPackageDayAccommodationId())
                                    .dayNumber(accomm.getDayNumber())
                                    .breakfast(accomm.getBreakfast())
                                    .breakfastDescription(accomm.getBreakfastDescription())
                                    .lunch(accomm.getLunch())
                                    .lunchDescription(accomm.getLunchDescription())
                                    .dinner(accomm.getDinner())
                                    .dinnerDescription(accomm.getDinnerDescription())
                                    .morningTea(accomm.getMorningTea())
                                    .morningTeaDescription(accomm.getMorningTeaDescription())
                                    .eveningTea(accomm.getEveningTea())
                                    .eveningTeaDescription(accomm.getEveningTeaDescription())
                                    .snacks(accomm.getSnacks())
                                    .snackNote(accomm.getSnackNote())
                                    .hotelId(accomm.getHotelId())
                                    .transportId(accomm.getTransportId())
                                    .otherNotes(accomm.getOtherNotes())
                                    .status(accomm.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.dayAccommodationsToUpdate(dayAccommodationsToUpdate);
        }

        // Inclusions changes
        if (packageUpdateRequest.getAddInclusions() != null) {
            List<PackageComparisonResult.PackageInclusionChange> inclusionsToAdd =
                    packageUpdateRequest.getAddInclusions().stream()
                            .map(inc -> PackageComparisonResult.PackageInclusionChange.builder()
                                    .inclusionText(inc.getInclusionText())
                                    .displayOrder(inc.getDisplayOrder())
                                    .status(inc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.inclusionsToAdd(inclusionsToAdd);
        }

        resultBuilder.inclusionIdsToRemove(packageUpdateRequest.getRemoveInclusionIds() != null ?
                packageUpdateRequest.getRemoveInclusionIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedInclusions() != null) {
            List<PackageComparisonResult.PackageInclusionChange> inclusionsToUpdate =
                    packageUpdateRequest.getUpdatedInclusions().stream()
                            .map(inc -> PackageComparisonResult.PackageInclusionChange.builder()
                                    .inclusionId(inc.getPackageInclusionId())
                                    .inclusionText(inc.getInclusionText())
                                    .displayOrder(inc.getDisplayOrder())
                                    .status(inc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.inclusionsToUpdate(inclusionsToUpdate);
        }

        // Exclusions changes
        if (packageUpdateRequest.getAddExclusions() != null) {
            List<PackageComparisonResult.PackageExclusionChange> exclusionsToAdd =
                    packageUpdateRequest.getAddExclusions().stream()
                            .map(exc -> PackageComparisonResult.PackageExclusionChange.builder()
                                    .exclusionText(exc.getExclusionText())
                                    .displayOrder(exc.getDisplayOrder())
                                    .status(exc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.exclusionsToAdd(exclusionsToAdd);
        }

        resultBuilder.exclusionIdsToRemove(packageUpdateRequest.getRemoveExclusionIds() != null ?
                packageUpdateRequest.getRemoveExclusionIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedExclusions() != null) {
            List<PackageComparisonResult.PackageExclusionChange> exclusionsToUpdate =
                    packageUpdateRequest.getUpdatedExclusions().stream()
                            .map(exc -> PackageComparisonResult.PackageExclusionChange.builder()
                                    .exclusionId(exc.getPackageExclusionId())
                                    .exclusionText(exc.getExclusionText())
                                    .displayOrder(exc.getDisplayOrder())
                                    .status(exc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.exclusionsToUpdate(exclusionsToUpdate);
        }

        // Conditions changes
        if (packageUpdateRequest.getAddConditions() != null) {
            List<PackageComparisonResult.PackageConditionChange> conditionsToAdd =
                    packageUpdateRequest.getAddConditions().stream()
                            .map(cond -> PackageComparisonResult.PackageConditionChange.builder()
                                    .conditionText(cond.getConditionText())
                                    .displayOrder(cond.getDisplayOrder())
                                    .status(cond.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.conditionsToAdd(conditionsToAdd);
        }

        resultBuilder.conditionIdsToRemove(packageUpdateRequest.getRemoveConditionIds() != null ?
                packageUpdateRequest.getRemoveConditionIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedConditions() != null) {
            List<PackageComparisonResult.PackageConditionChange> conditionsToUpdate =
                    packageUpdateRequest.getUpdatedConditions().stream()
                            .map(cond -> PackageComparisonResult.PackageConditionChange.builder()
                                    .conditionId(cond.getPackageConditionId())
                                    .conditionText(cond.getConditionText())
                                    .displayOrder(cond.getDisplayOrder())
                                    .status(cond.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.conditionsToUpdate(conditionsToUpdate);
        }

        // Travel Tips changes
        if (packageUpdateRequest.getAddTravelTips() != null) {
            List<PackageComparisonResult.PackageTravelTipChange> travelTipsToAdd =
                    packageUpdateRequest.getAddTravelTips().stream()
                            .map(tip -> PackageComparisonResult.PackageTravelTipChange.builder()
                                    .tipTitle(tip.getTipTitle())
                                    .tipDescription(tip.getTipDescription())
                                    .displayOrder(tip.getDisplayOrder())
                                    .status(tip.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.travelTipsToAdd(travelTipsToAdd);
        }

        resultBuilder.travelTipIdsToRemove(packageUpdateRequest.getRemoveTravelTipIds() != null ?
                packageUpdateRequest.getRemoveTravelTipIds() : Collections.emptyList());

        if (packageUpdateRequest.getUpdatedTravelTips() != null) {
            List<PackageComparisonResult.PackageTravelTipChange> travelTipsToUpdate =
                    packageUpdateRequest.getUpdatedTravelTips().stream()
                            .map(tip -> PackageComparisonResult.PackageTravelTipChange.builder()
                                    .travelTipId(tip.getPackageTipId())
                                    .tipTitle(tip.getTipTitle())
                                    .tipDescription(tip.getTipDescription())
                                    .displayOrder(tip.getDisplayOrder())
                                    .status(tip.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.travelTipsToUpdate(travelTipsToUpdate);
        }

        // Determine if there are any changes
        boolean hasChanges = !fieldChanges.isEmpty() ||
                (packageUpdateRequest.getRemovedImageIds() != null && !packageUpdateRequest.getRemovedImageIds().isEmpty()) ||
                (packageUpdateRequest.getAddImages() != null && !packageUpdateRequest.getAddImages().isEmpty()) ||
                (packageUpdateRequest.getUpdatedImages() != null && !packageUpdateRequest.getUpdatedImages().isEmpty()) ||
                (packageUpdateRequest.getAddFeatures() != null && !packageUpdateRequest.getAddFeatures().isEmpty()) ||
                (packageUpdateRequest.getRemoveFeatureIds() != null && !packageUpdateRequest.getRemoveFeatureIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedFeatures() != null && !packageUpdateRequest.getUpdatedFeatures().isEmpty()) ||
                (packageUpdateRequest.getAddDayAccommodations() != null && !packageUpdateRequest.getAddDayAccommodations().isEmpty()) ||
                (packageUpdateRequest.getRemoveDayAccommodationIds() != null && !packageUpdateRequest.getRemoveDayAccommodationIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedDayAccommodations() != null && !packageUpdateRequest.getUpdatedDayAccommodations().isEmpty()) ||
                (packageUpdateRequest.getAddInclusions() != null && !packageUpdateRequest.getAddInclusions().isEmpty()) ||
                (packageUpdateRequest.getRemoveInclusionIds() != null && !packageUpdateRequest.getRemoveInclusionIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedInclusions() != null && !packageUpdateRequest.getUpdatedInclusions().isEmpty()) ||
                (packageUpdateRequest.getAddExclusions() != null && !packageUpdateRequest.getAddExclusions().isEmpty()) ||
                (packageUpdateRequest.getRemoveExclusionIds() != null && !packageUpdateRequest.getRemoveExclusionIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedExclusions() != null && !packageUpdateRequest.getUpdatedExclusions().isEmpty()) ||
                (packageUpdateRequest.getAddConditions() != null && !packageUpdateRequest.getAddConditions().isEmpty()) ||
                (packageUpdateRequest.getRemoveConditionIds() != null && !packageUpdateRequest.getRemoveConditionIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedConditions() != null && !packageUpdateRequest.getUpdatedConditions().isEmpty()) ||
                (packageUpdateRequest.getAddTravelTips() != null && !packageUpdateRequest.getAddTravelTips().isEmpty()) ||
                (packageUpdateRequest.getRemoveTravelTipIds() != null && !packageUpdateRequest.getRemoveTravelTipIds().isEmpty()) ||
                (packageUpdateRequest.getUpdatedTravelTips() != null && !packageUpdateRequest.getUpdatedTravelTips().isEmpty());

        resultBuilder.hasChanges(hasChanges);

        // Create summary
        String summary = createPackageSummary(resultBuilder.build());
        resultBuilder.summary(summary);

        return resultBuilder.build();
    }

    private String createPackageSummary(PackageComparisonResult result) {
        StringBuilder summary = new StringBuilder();

        if (!result.isHasChanges()) {
            return "No changes detected";
        }

        if (!result.getBasicDetailsChanges().isEmpty()) {
            summary.append("Basic details updated: ")
                    .append(result.getBasicDetailsChanges().stream()
                            .map(PackageComparisonResult.FieldChange::getFieldName)
                            .collect(Collectors.joining(", ")))
                    .append(". ");
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

        if (!result.getFeaturesToAdd().isEmpty()) {
            summary.append("Add ").append(result.getFeaturesToAdd().size()).append(" features. ");
        }

        if (!result.getFeatureIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getFeatureIdsToRemove().size()).append(" features. ");
        }

        if (!result.getFeaturesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getFeaturesToUpdate().size()).append(" features. ");
        }

        if (!result.getDayAccommodationsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getDayAccommodationsToAdd().size()).append(" day accommodations. ");
        }

        if (!result.getDayAccommodationIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getDayAccommodationIdsToRemove().size()).append(" day accommodations. ");
        }

        if (!result.getDayAccommodationsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getDayAccommodationsToUpdate().size()).append(" day accommodations. ");
        }

        if (!result.getInclusionsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getInclusionsToAdd().size()).append(" inclusions. ");
        }

        if (!result.getInclusionIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getInclusionIdsToRemove().size()).append(" inclusions. ");
        }

        if (!result.getInclusionsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getInclusionsToUpdate().size()).append(" inclusions. ");
        }

        if (!result.getExclusionsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getExclusionsToAdd().size()).append(" exclusions. ");
        }

        if (!result.getExclusionIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getExclusionIdsToRemove().size()).append(" exclusions. ");
        }

        if (!result.getExclusionsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getExclusionsToUpdate().size()).append(" exclusions. ");
        }

        if (!result.getConditionsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getConditionsToAdd().size()).append(" conditions. ");
        }

        if (!result.getConditionIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getConditionIdsToRemove().size()).append(" conditions. ");
        }

        if (!result.getConditionsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getConditionsToUpdate().size()).append(" conditions. ");
        }

        if (!result.getTravelTipsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getTravelTipsToAdd().size()).append(" travel tips. ");
        }

        if (!result.getTravelTipIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getTravelTipIdsToRemove().size()).append(" travel tips. ");
        }

        if (!result.getTravelTipsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getTravelTipsToUpdate().size()).append(" travel tips.");
        }

        return summary.toString().trim();
    }

}