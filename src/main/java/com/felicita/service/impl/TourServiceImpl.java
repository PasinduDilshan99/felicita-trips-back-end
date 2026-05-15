package com.felicita.service.impl;

import com.felicita.email.TourEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.*;
import com.felicita.model.other.ActivitiesCategoryComparisonResult;
import com.felicita.model.other.ActivitiesComparisonResult;
import com.felicita.model.other.TourCategoryComparisonResult;
import com.felicita.model.other.TourComparisonResult;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.TourDataRequest;
import com.felicita.model.request.TourInsertRequest;
import com.felicita.model.request.TourUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryImageInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryImageUpdateRequest;
import com.felicita.model.request.tour.category.TourCategoryInsertRequest;
import com.felicita.model.request.tour.category.TourCategoryUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.statistics.TourCategoryStatisticsResponse;
import com.felicita.model.response.statistics.TourScheduleStatisticsResponse;
import com.felicita.model.response.statistics.TourStatisticsResponse;
import com.felicita.model.response.statistics.TourTypeStatisticsResponse;
import com.felicita.model.response.tour.category.TourCategoryAllDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryBasicDetailsResponse;
import com.felicita.model.response.tour.category.TourCategoryImageResponse;
import com.felicita.repository.TourRepository;
import com.felicita.repository.WishListRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.DestinationService;
import com.felicita.service.EmailService;
import com.felicita.service.TourService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.TourValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_TOUR_CATEGORY_DETAILS;
import static com.felicita.util.FrontEndUrls.VIEW_TOUR_DETAILS;

@Service
public class TourServiceImpl implements TourService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourServiceImpl.class);

    private final TourRepository tourRepository;
    private final DestinationService destinationService;
    private final TourValidationService tourValidationService;
    private final CommonService commonService;
    private final WishListRepository wishListRepository;
    private final TourEmailHelperService tourEmailHelperService;
    private final EmailService emailService;

    @Autowired
    public TourServiceImpl(TourRepository tourRepository, DestinationService destinationService, TourValidationService tourValidationService, CommonService commonService, WishListRepository wishListRepository, TourEmailHelperService tourEmailHelperService, EmailService emailService) {
        this.tourRepository = tourRepository;
        this.destinationService = destinationService;
        this.tourValidationService = tourValidationService;
        this.commonService = commonService;
        this.wishListRepository = wishListRepository;
        this.tourEmailHelperService = tourEmailHelperService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<List<TourResponseDto>> getAllTours() {
        LOGGER.info("Start fetching all tours from repository");
        try {
            List<TourResponseDto> tourResponseDtos = tourRepository.getAllTours();

            Long userId = commonService.getUserIdBySecurityContextWithOutException();

            Set<Long> tourIdSet = new HashSet<>();
            if (userId != null) {
                List<Long> tourIds = wishListRepository.getAllTourWishListByUserId(userId);
                if (tourIds != null) {
                    tourIdSet.addAll(tourIds);
                }
            }
            if (tourResponseDtos != null) {
                for (TourResponseDto tourResponseDto : tourResponseDtos) {
                    tourResponseDto.setWish(tourIdSet.contains(tourResponseDto.getTourId()));
                }

            }

            if (tourResponseDtos.isEmpty()) {
                LOGGER.warn("No tours found in database");
                throw new DataNotFoundErrorExceptionHandler("No tours found");
            }

            LOGGER.info("Fetched {} tours successfully", tourResponseDtos.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourResponseDtos,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tours: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tours from database");
        } finally {
            LOGGER.info("End fetching all tours from repository");
        }
    }

    @Override
    public CommonResponse<List<TourResponseDto>> getActiveTours() {
        LOGGER.info("Start fetching active tours from repository");
        try {
            List<TourResponseDto> tourResponseDtos = getAllTours().getData();

            List<TourResponseDto> tourResponseDtoList = tourResponseDtos.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getStatusName()))
                    .toList();

            LOGGER.info("Fetched {} active tours successfully", tourResponseDtoList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourResponseDtoList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active tours: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active tours from database");
        } finally {
            LOGGER.info("End fetching active tours from repository");
        }
    }

    @Override
    public CommonResponse<List<PopularTourResponseDto>> getPopularTours() {
        LOGGER.info("Start fetching popular tours from repository");
        try {
            List<PopularTourResponseDto> popularTours = tourRepository.getPopularTours();

            if (popularTours.isEmpty()) {
                LOGGER.warn("No popular tours found in database");
                throw new DataNotFoundErrorExceptionHandler("No popular tours found");
            }

            LOGGER.info("Fetched {} popular tours successfully", popularTours.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    popularTours,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching popular tours: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch popular tours from database");
        } finally {
            LOGGER.info("End fetching popular tours from repository");
        }
    }

    @Override
    public CommonResponse<TourResponseDto> getTourDetailsById(Long tourId) {
        LOGGER.info("Start fetching tour details by id from repository");
        try {
            TourResponseDto tourResponseDto = tourRepository.getTourDetailsById(tourId);

            if (tourResponseDto == null) {
                LOGGER.warn("No tours found in database with id : {}", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tours found from tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourResponseDto,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour by tour id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour by tour id from database");
        } finally {
            LOGGER.info("End fetching tour by tour id {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<TourReviewDetailsResponse>> getAllTourReviewDetails() {
        LOGGER.info("Start fetching all tour review details from repository");
        try {
            List<TourReviewDetailsResponse> tourReviewDetailsResponses = tourRepository.getAllTourReviewDetails();

            if (tourReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No review details found in database");
                throw new DataNotFoundErrorExceptionHandler("No review details found in database");
            }

            List<TourReviewDetailsResponse> tourReviewDetailsResponseList = tourReviewDetailsResponses.stream()
                    .filter(data -> CommonStatus.ACTIVE.name().equalsIgnoreCase(data.getReviewStatus()))
                    .toList();

            LOGGER.info("Fetched {} tour review details successfully", tourReviewDetailsResponseList.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourReviewDetailsResponseList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour review details : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour review details from database");
        } finally {
            LOGGER.info("End fetching tour review details from repository");
        }
    }

    @Override
    public CommonResponse<List<TourReviewDetailsResponse>> getTourReviewDetailsById(Long tourId) {
        LOGGER.info("Start fetching tour review details by tour id : {} from repository", tourId);
        try {
            List<TourReviewDetailsResponse> tourReviewDetailsResponses = tourRepository.getTourReviewDetailsById(tourId);

            if (tourReviewDetailsResponses.isEmpty()) {
                LOGGER.warn("No tour review details found by tour id : {} in database", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tour review details found by tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourReviewDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour review details by tour id: {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching tour review details by tour id: {}" + tourId);
        } finally {
            LOGGER.info("End fetching tour review details by tour id: {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<TourDestinationsForMapResponse>> getTourDestinationsForMap(Long tourId) {
        LOGGER.info("Start fetching tour destinations for map by tour id : {}  from repository", tourId);
        try {
            List<TourDestinationsForMapResponse> tourDestinationsForMapResponses = tourRepository.getTourDestinationsForMap(tourId);

            if (tourDestinationsForMapResponses.isEmpty()) {
                LOGGER.warn("No tour destinations for map by tour id : {}  in database", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tour destinations for map by tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourDestinationsForMapResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour destinations for map by tour id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching tour destinations for map by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching tour destinations for map by tour id : {}", tourId);
        }
    }

    @Override
    public CommonResponse<List<TourHistoryResponse>> getAllTourHistoryDetails() {
        LOGGER.info("Start fetching all tour history details from repository");
        try {
            List<TourHistoryResponse> tourHistoryResponses = tourRepository.getAllTourHistoryDetails();

            if (tourHistoryResponses.isEmpty()) {
                LOGGER.warn("No tour history details found in database");
                throw new DataNotFoundErrorExceptionHandler("No tour history details found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourHistoryResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour history details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour history details from database");
        } finally {
            LOGGER.info("End fetching tour history details from repository");
        }
    }

    @Override
    public CommonResponse<List<TourHistoryResponse>> getTourHistoryDetailsById(Long tourId) {
        LOGGER.info("Start fetching tour history details by id : {} from repository", tourId);
        try {
            List<TourHistoryResponse> tourHistoryResponses = tourRepository.getTourHistoryDetailsById(tourId);

            if (tourHistoryResponses.isEmpty()) {
                LOGGER.warn("No tour history details by tour id : {} found in database", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tour history details by tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourHistoryResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour history details by tour id:{} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching tour history details by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching tour history details by tour id:{} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<TourHistoryImageResponse>> getAllTourHistoryImages() {
        LOGGER.info("Start fetching all tour history images from repository");
        try {
            List<TourHistoryImageResponse> tourHistoryImageResponses = tourRepository.getAllTourHistoryImages();

            if (tourHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No tour history images found in database");
                throw new DataNotFoundErrorExceptionHandler("No tour history images found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourHistoryImageResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour history images : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour history images from database");
        } finally {
            LOGGER.info("End fetching tour history images from repository");
        }
    }

    @Override
    public CommonResponse<List<TourHistoryImageResponse>> getTourHistoryImagesById(Long tourId) {
        LOGGER.info("Start fetching tour history images by id : {} from repository", tourId);
        try {
            List<TourHistoryImageResponse> tourHistoryImageResponses = tourRepository.getTourHistoryImagesById(tourId);

            if (tourHistoryImageResponses.isEmpty()) {
                LOGGER.warn("No tour history images by tour id : {} found in database", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tour history images by tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourHistoryImageResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour history images by id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching tour history images by id : " + tourId);
        } finally {
            LOGGER.info("End fetching tour history images by id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<ToursDetailsWithParamResponse> getToursToShowWithParam(TourDataRequest tourDataRequest) {
        LOGGER.info("Start fetching tours for params from repository");
        try {
            ToursDetailsWithParamResponse toursDetailsWithParamResponse = tourRepository.getToursToShowWithParam(tourDataRequest);

            Long userId = commonService.getUserIdBySecurityContextWithOutException();

            Set<Long> tourIdSet = new HashSet<>();
            if (userId != null) {
                LOGGER.info("USER ID : {}, FETCHING WISHLIST ACTIVITY IDS", userId);
                List<Long> tourIds = wishListRepository.getAllTourWishListByUserId(userId);
                if (tourIds != null) {
                    tourIdSet.addAll(tourIds);
                    LOGGER.info("USER ID : {} , WISHLIST ACTIVITY IDS : {}", userId, tourIdSet);
                }
            }
            if (toursDetailsWithParamResponse != null) {
                List<TourResponseDto> tourResponseDtos = toursDetailsWithParamResponse.getTourResponseDtoList();
                if (tourResponseDtos != null) {
                    for (TourResponseDto tourResponseDto : tourResponseDtos) {
                        tourResponseDto.setWish(tourIdSet.contains(tourResponseDto.getTourId()));
                    }
                }
            }

            if (toursDetailsWithParamResponse == null) {
                LOGGER.warn("No tours for param found in database");
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
                    toursDetailsWithParamResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tours for param: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tours for param from database");
        } finally {
            LOGGER.info("End fetching all tours for param from repository");
        }
    }

    @Override
    public CommonResponse<List<TourDetailsWithDayToDayResponse>> getTourDetailsDayByDay(Long tourId) {
        LOGGER.info("Start fetching tour daya to day details by tour id : {} from repository", tourId);
        try {
            List<TourDayDestinationActivityIdsDto> tourDayDestinationActivityIdsDtos = tourRepository.getTourDayDestinationActivityIds(tourId);

            List<TourDetailsIdDayByDayReponse> tourDetailsIdDayByDayResponses = tourDayDestinationActivityIdsDtos.stream()
                    .collect(Collectors.groupingBy(TourDayDestinationActivityIdsDto::getDay))
                    .entrySet().stream()
                    .map(entry -> {
                        int day = entry.getKey();
                        List<TourDetailsIdDayByDayReponse.DestinationDetails> destinationDetails = entry.getValue().stream()
                                .map(dto -> TourDetailsIdDayByDayReponse.DestinationDetails.builder()
                                        .destinationId(Long.valueOf(dto.getDestinationId()))
                                        .activityIds(dto.getActivityIds().stream()
                                                .map(Long::valueOf)
                                                .toList())
                                        .build())
                                .toList();

                        return TourDetailsIdDayByDayReponse.builder()
                                .day(day)
                                .destinationDetails(destinationDetails)
                                .build();
                    })
                    .sorted(Comparator.comparingInt(TourDetailsIdDayByDayReponse::getDay))
                    .toList();


            List<Long> destinationIdList = tourDayDestinationActivityIdsDtos.stream()
                    .map(TourDayDestinationActivityIdsDto::getDestinationId)
                    .filter(Objects::nonNull)
                    .map(Long::valueOf)
                    .distinct()
                    .toList();

            List<Long> activityIdList = tourDayDestinationActivityIdsDtos.stream()
                    .flatMap(dto -> dto.getActivityIds().stream())
                    .filter(Objects::nonNull)
                    .map(Long::valueOf)
                    .distinct()
                    .toList();


            List<TourDetailsWithDayToDayResponse.DestinationDetailsPerDay> destionationsDetailsList =
                    tourRepository.getDestinationsDetailsByIds(destinationIdList);
            List<TourDetailsWithDayToDayResponse.ActivityPerDayResponse> activityDetailsList =
                    tourRepository.getActivityDetailsByIds(activityIdList);

            List<TourDetailsWithDayToDayResponse> response = new ArrayList<>();

            for (TourDetailsIdDayByDayReponse dayDetail : tourDetailsIdDayByDayResponses) {
                TourDetailsWithDayToDayResponse dayResponse = new TourDetailsWithDayToDayResponse();
                dayResponse.setDayNumber(dayDetail.getDay());

                List<TourDetailsWithDayToDayResponse.DestinationPerDayResponse> destinationPerDayResponses = new ArrayList<>();

                for (TourDetailsIdDayByDayReponse.DestinationDetails dest : dayDetail.getDestinationDetails()) {
                    TourDetailsWithDayToDayResponse.DestinationPerDayResponse destResponse = new TourDetailsWithDayToDayResponse.DestinationPerDayResponse();

                    TourDetailsWithDayToDayResponse.DestinationDetailsPerDay destinationDetails = destionationsDetailsList.stream()
                            .filter(d -> d.getDestinationId().equals(dest.getDestinationId()))
                            .findFirst()
                            .orElse(null);

                    destResponse.setDestination(destinationDetails);

                    List<TourDetailsWithDayToDayResponse.ActivityPerDayResponse> activitiesForDestination = activityDetailsList.stream()
                            .filter(a -> dest.getActivityIds().contains(a.getId()))
                            .toList();

                    destResponse.setActivities(activitiesForDestination);

                    destinationPerDayResponses.add(destResponse);
                }

                dayResponse.setDestinations(destinationPerDayResponses);
                response.add(dayResponse);
            }


            if (tourDayDestinationActivityIdsDtos.isEmpty()) {
                LOGGER.warn("No tours found in tour id : {} in database", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tours found in tour id : " + tourId);
            }

            LOGGER.info("Fetched {} tour daya to day details successfully", tourDayDestinationActivityIdsDtos);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    response,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour day to day details with tour id :{} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching tour day to day details with tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching tour day to day details with tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<TourExtrasResponse> getTourExtraDetailsDayByDay(Long tourId) {
        LOGGER.info("Start fetching tour extra details by tour id : {} from repository", tourId);
        try {
            List<TourExtrasResponse.TourInclusion> inclusions = tourRepository.getTourInclusions(tourId);
            List<TourExtrasResponse.TourExclusion> exclusions = tourRepository.getTourExclusions(tourId);
            List<TourExtrasResponse.TourCondition> conditions = tourRepository.getTourConditions(tourId);
            List<TourExtrasResponse.TourTravelTip> travelTips = tourRepository.getTourTravelTips(tourId);

            TourExtrasResponse tourExtrasResponse = new TourExtrasResponse(
                    inclusions,
                    exclusions,
                    conditions,
                    travelTips
            );

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourExtrasResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour extra details by tour id: {} ,{}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour extra details by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching all tour extra details by tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<TourSchedulesResponse> getTourSchedules(Long tourId) {
        LOGGER.info("Start fetching tour schedules by tour id : {} from the repository", tourId);
        try {
            List<TourSchedulesResponse.TourScheduleDetails> scheduleDetails =
                    tourRepository.getTourSchedulesById(tourId);
            TourSchedulesResponse.TourBasicDetails tourBasicDetails =
                    tourRepository.getTourBasicDetails(tourId);


            TourSchedulesResponse tourSchedulesResponse = new TourSchedulesResponse(
                    tourBasicDetails,
                    scheduleDetails
            );

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourSchedulesResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour schedules by tour id: {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour schedules by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching all tour schedules by tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<List<TourBasicDetailsResponse>> getAllToursBasicDetails() {
        LOGGER.info("Start fetching all tours basic details from repository");
        try {
            List<TourBasicDetailsResponse> tourBasicDetailsResponses = tourRepository.getAllToursBasicDetails();

            if (tourBasicDetailsResponses.isEmpty()) {
                LOGGER.warn("No tours found in database");
                throw new DataNotFoundErrorExceptionHandler("No tours found");
            }

            LOGGER.info("Fetched {} tours basic details successfully", tourBasicDetailsResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourBasicDetailsResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tours basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tours basic details from database");
        } finally {
            LOGGER.info("End fetching all tours basic details from repository");
        }
    }

    @Override
    public CommonResponse<List<TourForTerminateResponse>> getToursForTerminate() {
        LOGGER.info("Start fetching tours for terminate from repository");
        try {
            List<TourForTerminateResponse> tourForTerminateResponses =
                    tourRepository.getToursForTerminate();

            if (tourForTerminateResponses.isEmpty()) {
                LOGGER.warn("No tours found in database");
                throw new DataNotFoundErrorExceptionHandler("No tours found");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourForTerminateResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tours: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tours from database");
        } finally {
            LOGGER.info("End fetching tours for terminate from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateTour(TourTerminateRequest tourTerminateRequest) {
        LOGGER.info("Start execute terminate tour request.");
        try {
            tourValidationService.validateTerminateTourRequest(tourTerminateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            TourAllDetailsResponse tourDetails = getTourAllDetailsById(tourTerminateRequest.getTourId()).getData();

            tourRepository.terminateTour(tourTerminateRequest, userId);
            tourRepository.terminateTourDestinations(tourTerminateRequest.getTourId(), userId);
            tourRepository.terminateTourTypesAssignToTour(tourTerminateRequest.getTourId(), userId);
            tourRepository.terminateTourCategoriesAssignToTour(tourTerminateRequest.getTourId(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Tour Terminated")
                    .message("The tour '" + tourDetails.getTourName() + "' has been terminated.")
                    .actionUrl(VIEW_TOUR_DETAILS + "/" + tourDetails.getTourId())
                    .actionText("View Tour")
                    .icon("MapOff")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "tourId", tourDetails.getTourId(),
                            "tourName", tourDetails.getTourName(),
                            "status", tourDetails.getStatusName(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_TERMINATE.name())
                    .sourceModule(SourceModule.TOUR.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = tourEmailHelperService.buildTourTerminateSuccessfullSubject(loggedUser, tourDetails);
            String body = tourEmailHelperService.buildTourTerminateSuccessfullBody(loggedUser, tourDetails);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminate tour request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate tour request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertTour(TourInsertRequest tourInsertRequest) {
        LOGGER.info("Start execute insert tour request.");
        try {
            tourValidationService.validateTourInsertRequest(tourInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long tourId = tourRepository.insertTourDetails(tourInsertRequest, userId);
            tourRepository.insertTourTypesToTour(tourId, tourInsertRequest.getTourTypes(), userId);
            tourRepository.insertTourCategoriesToTour(tourId, tourInsertRequest.getTourCategories(), userId);
            tourRepository.insertTourDestinations(tourId, tourInsertRequest.getItinerary(), userId);
            tourRepository.insertTourImages(tourId, tourInsertRequest.getImages(), userId);
            tourRepository.insertTourInclusions(tourId, tourInsertRequest.getInclusions(), userId);
            tourRepository.insertTourExclusions(tourId, tourInsertRequest.getExclusions(), userId);
            tourRepository.insertTourConditions(tourId, tourInsertRequest.getConditions(), userId);
            tourRepository.insertTourTravelTips(tourId, tourInsertRequest.getTravelTips(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Tour Created")
                    .message("A new tour '" + tourInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_TOUR_DETAILS + "/" + tourId)
                    .actionText("View Tour")
                    .icon("Map")
                    .color("#10B981")
                    .metadata(Map.of(
                            "tourId", tourId,
                            "tourName", tourInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_CREATE.name())
                    .sourceModule(SourceModule.TOUR.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (tourId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.TOUR_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = tourEmailHelperService.buildTourCreateSuccessfullBody(tourInsertRequest, tourId, loggedUser);
                String subject = tourEmailHelperService.buildTourCreateSuccessfullSubject(tourInsertRequest,tourId, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert tour request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert tour request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<List<TourIdAndTourNameResponse>> getTourIdsAndTourNames() {
        LOGGER.info("Strat fetching tours names and id from repository");
        CommonResponse<List<TourForTerminateResponse>> toursForTerminate = getToursForTerminate();
        List<TourIdAndTourNameResponse> tourIdAndTourNameResponses = new ArrayList<>();
        for (TourForTerminateResponse tourForTerminateResponse : toursForTerminate.getData()) {
            tourIdAndTourNameResponses.add(
                    new TourIdAndTourNameResponse(
                            tourForTerminateResponse.getTourId(),
                            tourForTerminateResponse.getTourName()
                    )
            );
        }
        LOGGER.info("End fetching tours names and id from repository");
        return new CommonResponse<>(
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                tourIdAndTourNameResponses,
                Instant.now());
    }

    @Override
    public CommonResponse<TourDetailsForAddPackageResponse> getTourDetailsForAddPackage(Long tourId) {
        LOGGER.info("Start fetching tour details by tour id : {} from the repository for add package", tourId);
        try {
            TourDetailsForAddPackageResponse tourDetailsForAddPackageResponses =
                    tourRepository.getTourDetailsForAddPackage(tourId);
            List<String> tourInclusions = tourRepository.getTourInclusionsNamesOnly(tourId);
            List<String> tourExclusions = tourRepository.getTourExclusionsNamesOnly(tourId);
            List<String> tourConditions = tourRepository.getTourConditionsNamesOnly(tourId);
            List<TourDetailsForAddPackageResponse.TravelTip> tourTravelTips = tourRepository.getTourTravelTipsNamesOnly(tourId);

            tourDetailsForAddPackageResponses.setInclusions(tourInclusions);
            tourDetailsForAddPackageResponses.setExclusions(tourExclusions);
            tourDetailsForAddPackageResponses.setConditions(tourConditions);
            tourDetailsForAddPackageResponses.setTravelTips(tourTravelTips);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourDetailsForAddPackageResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour details by tour id: {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour details by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching tour details by tour id : {} from the repository for add package", tourId);
        }
    }

    @Override
    public CommonResponse<TourAllDetailsResponse> getTourAllDetailsById(Long tourId) {
        LOGGER.info("Start fetching all tour details by tour id : {} from repository", tourId);
        try {
            TourAllDetailsResponse tourAllDetailsResponse = new TourAllDetailsResponse();
            TourResponseDto tourResponseDto = tourRepository.getTourDetailsById(tourId);

            TourAssignUserDto tourAssignUserDto = tourRepository.getTourAssignUserDetailsByTourId(tourId);
            tourAllDetailsResponse.setTourId(tourResponseDto.getTourId());
            tourAllDetailsResponse.setTourName(tourResponseDto.getTourName());
            tourAllDetailsResponse.setTourDescription(tourResponseDto.getTourDescription());
            tourAllDetailsResponse.setDuration(tourResponseDto.getDuration());
            tourAllDetailsResponse.setLongitude(tourResponseDto.getLongitude());
            tourAllDetailsResponse.setLatitude(tourResponseDto.getLatitude());
            tourAllDetailsResponse.setStartLocation(tourResponseDto.getStartLocation());
            tourAllDetailsResponse.setEndLocation(tourResponseDto.getEndLocation());
            tourAllDetailsResponse.setTourTypeDtos(tourResponseDto.getTourTypeDtos());
            tourAllDetailsResponse.setTourCategoryDto(tourResponseDto.getTourCategoryDto());
            tourAllDetailsResponse.setSeasonName(tourResponseDto.getSeasonName());
            tourAllDetailsResponse.setSeasonDescription(tourResponseDto.getSeasonDescription());
            tourAllDetailsResponse.setStatusName(tourResponseDto.getStatusName());
            tourAllDetailsResponse.setSchedules(tourResponseDto.getSchedules());
            tourAllDetailsResponse.setImages(tourResponseDto.getImages());
            tourAllDetailsResponse.setAssignTo(tourAssignUserDto.getAssignTo());
            tourAllDetailsResponse.setAssignToName(tourAssignUserDto.getAssignToName());
            tourAllDetailsResponse.setAssignMessage(tourAssignUserDto.getAssignMessage());

            List<TourExtrasResponse.TourExclusion> tourExclusions = tourRepository.getTourExclusions(tourId);
            List<TourExtrasResponse.TourCondition> tourConditions = tourRepository.getTourConditions(tourId);
            List<TourExtrasResponse.TourInclusion> tourInclusions = tourRepository.getTourInclusions(tourId);
            List<TourExtrasResponse.TourTravelTip> tourTravelTips = tourRepository.getTourTravelTips(tourId);

            tourAllDetailsResponse.setExclusions(tourExclusions);
            tourAllDetailsResponse.setConditions(tourConditions);
            tourAllDetailsResponse.setInclusions(tourInclusions);
            tourAllDetailsResponse.setTravelTips(tourTravelTips);

            List<TourDetailsWithDayToDayResponse> tourDetailsDayByDay = getTourDetailsDayByDay(tourId).getData();
            List<TourAllDetailsResponse.DayToDayResponse> dayToDayResponses = new ArrayList<>();

            for (TourDetailsWithDayToDayResponse daySource : tourDetailsDayByDay) {

                TourAllDetailsResponse.DayToDayResponse dayTarget =
                        new TourAllDetailsResponse.DayToDayResponse();
                dayTarget.setDayNumber(daySource.getDayNumber());

                List<TourAllDetailsResponse.DestinationPerDayResponse> destinationTargets = new ArrayList<>();

                for (TourDetailsWithDayToDayResponse.DestinationPerDayResponse destSource : daySource.getDestinations()) {

                    TourAllDetailsResponse.DestinationPerDayResponse destTarget =
                            new TourAllDetailsResponse.DestinationPerDayResponse();

                    TourAllDetailsResponse.DestinationDetailsPerDay destDetails =
                            new TourAllDetailsResponse.DestinationDetailsPerDay();

                    var srcDest = destSource.getDestination();

                    destDetails.setDestinationId(srcDest.getDestinationId());
                    destDetails.setDestinationName(srcDest.getDestinationName());
                    destDetails.setDestinationDescription(srcDest.getDestinationDescription());
                    destDetails.setCategory(srcDest.getDestinationCategoryDetailsDtos());
                    destDetails.setLocation(srcDest.getLocation());
                    destDetails.setLatitude(srcDest.getLatitude());
                    destDetails.setLongitude(srcDest.getLongitude());

                    List<TourAllDetailsResponse.DestinationImagePerDay> destImages = new ArrayList<>();
                    if (srcDest.getImages() != null) {
                        for (var img : srcDest.getImages()) {
                            destImages.add(
                                    TourAllDetailsResponse.DestinationImagePerDay.builder()
                                            .imageId(img.getImageId())
                                            .imageName(img.getImageName())
                                            .imageDescription(img.getImageDescription())
                                            .imageUrl(img.getImageUrl())
                                            .imageStatus(img.getImageStatus())
                                            .build()
                            );
                        }
                    }
                    destDetails.setImages(destImages);
                    destTarget.setDestination(destDetails);

                    List<TourAllDetailsResponse.ActivityPerDay> activityTargets = new ArrayList<>();

                    for (var actSource : destSource.getActivities()) {

                        TourAllDetailsResponse.ActivityPerDay actTarget =
                                new TourAllDetailsResponse.ActivityPerDay();

                        actTarget.setActivityId(actSource.getId());
                        actTarget.setDestinationId(actSource.getDestinationId());
                        actTarget.setActivityName(actSource.getName());
                        actTarget.setActivityDescription(actSource.getDescription());
                        actTarget.setActivitiesCategory(actSource.getActivityCategoryDtos());
                        actTarget.setDurationHours(actSource.getDurationHours());
                        actTarget.setAvailableFrom(actSource.getAvailableFrom());
                        actTarget.setAvailableTo(actSource.getAvailableTo());
                        actTarget.setMinParticipate(actSource.getMinParticipate());
                        actTarget.setMaxParticipate(actSource.getMaxParticipate());
                        actTarget.setSeason(actSource.getSeason());
                        actTarget.setCategoryName(actSource.getCategoryName());

                        List<TourAllDetailsResponse.ActivityImagePerDay> actImages = new ArrayList<>();
                        if (actSource.getImages() != null) {
                            for (var img : actSource.getImages()) {
                                actImages.add(
                                        TourAllDetailsResponse.ActivityImagePerDay.builder()
                                                .imageId(img.getId())
                                                .imageName(img.getName())
                                                .imageDescription(img.getDescription())
                                                .imageUrl(img.getImageUrl())
                                                .build()
                                );
                            }
                        }
                        actTarget.setImages(actImages);

                        activityTargets.add(actTarget);
                    }

                    destTarget.setActivities(activityTargets);
                    destinationTargets.add(destTarget);
                }

                dayTarget.setDestinations(destinationTargets);
                dayToDayResponses.add(dayTarget);
            }

            tourAllDetailsResponse.setDayToDayResponses(dayToDayResponses);

            if (tourResponseDto == null) {
                LOGGER.warn("No tours found in database by tour id : {} ", tourId);
                throw new DataNotFoundErrorExceptionHandler("No tours found in database by tour id : " + tourId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourAllDetailsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all tour details by tour id : {} , {}", tourId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Error occurred while fetching all tour details by tour id : " + tourId);
        } finally {
            LOGGER.info("End fetching all tour details by tour id : {} from repository", tourId);
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateTour(TourUpdateRequest tourUpdateRequest) {
        LOGGER.info("Start execute update tour request.");
        try {
            tourValidationService.validateTourUpdateRequest(tourUpdateRequest);

            TourAllDetailsResponse previousTourData = getTourAllDetailsById(tourUpdateRequest.getTourId()).getData();

            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            tourRepository.updateTourBasicDetails(tourUpdateRequest.getTourId(), tourUpdateRequest.getTourBasicDetails(), userId);

            tourRepository.insertTourTypesToTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddTourTypes(), userId);
            tourRepository.removeTourTypesFromTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveTourTypes(), userId);
            tourRepository.updateTourTypesInTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateTourTypes(), userId);

            tourRepository.insertTourCategoriesToTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddTourCategories(), userId);
            tourRepository.removeTourCategoriesFromTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveTourCategories(), userId);
            tourRepository.updateTourCategoriesInTour(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateTourCategories(), userId);

            tourRepository.insertTourDestinations(tourUpdateRequest.getTourId(), tourUpdateRequest.getItinerary(), userId);
            tourRepository.removeTourDestinations(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveDestinations(), userId);
            tourRepository.removeActivitiesFromTourDestinations(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveActivities(), userId);
            tourRepository.updateTourDestinations(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateDestinations(), userId);

            tourRepository.insertTourImages(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddImages(), userId);
            tourRepository.removeTourImages(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveImages(), userId);
            tourRepository.updateTourImages(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateImages(), userId);

            tourRepository.insertTourInclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddInclusions(), userId);
            tourRepository.removeTourInclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveInclusions(), userId);
            tourRepository.updateTourInclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateInclusions(), userId);

            tourRepository.insertTourExclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddExclusions(), userId);
            tourRepository.removeTourExclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveExclusions(), userId);
            tourRepository.updateTourExclusions(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateExclusions(), userId);

            tourRepository.insertTourConditions(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddConditions(), userId);
            tourRepository.removeTourConditions(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveConditions(), userId);
            tourRepository.updateTourConditions(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateConditions(), userId);

            tourRepository.insertTourTravelTips(tourUpdateRequest.getTourId(), tourUpdateRequest.getAddTravelTips(), userId);
            tourRepository.removeTourTravelTips(tourUpdateRequest.getTourId(), tourUpdateRequest.getRemoveTravelTips(), userId);
            tourRepository.updateTourTravelTips(tourUpdateRequest.getTourId(), tourUpdateRequest.getUpdateTravelTips(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Tour Updated")
                    .message("The tour '" + tourUpdateRequest.getTourBasicDetails().getTourName() + "' has been updated.")
                    .actionUrl(VIEW_TOUR_DETAILS + "/" + tourUpdateRequest.getTourId())
                    .actionText("View Tour")
                    .icon("MapPinned")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "tourId", tourUpdateRequest.getTourId(),
                            "tourName", tourUpdateRequest.getTourBasicDetails().getTourName(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_UPDATE.name())
                    .sourceModule(SourceModule.TOUR.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            TourComparisonResult comparisonResult = compareTourUpdates(
                    tourUpdateRequest,
                    previousTourData
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ACTIVITY_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = tourEmailHelperService.buildTourUpdateSuccessfullSubject(loggedUser,tourUpdateRequest.getTourId());
            String body = tourEmailHelperService.buildTourUpdateSuccessfullBody(loggedUser,tourUpdateRequest.getTourId(), comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update tour request", tourUpdateRequest.getTourId()),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the update tour request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorExceptionHandler("Something went wrong");
        }
    }

    @Override
    public CommonResponse<TourStatisticsResponse> getTourStatistics() {
        LOGGER.info("Start fetching tour statistics from repository");
        try {
            TourStatisticsResponse tourStatisticsResponse = new TourStatisticsResponse();
            TourStatisticsResponse.Summary summary = tourRepository.getToutSummeryStatistics();
            List<TourStatisticsResponse.TourPopularity> tourPopularities = tourRepository.getTourPopularityStatistics();
            List<TourStatisticsResponse.BookingStatusDistribution> bookingStatusDistributions = tourRepository.getBookingStatusDistributionStatistics();
            List<TourStatisticsResponse.CategoryPerformance> categoryPerformances = tourRepository.getCategoryPerformanceStatistics();
            List<TourStatisticsResponse.TypeDistribution> typeDistributions = tourRepository.getTypeDistributionStatistics();
            List<TourStatisticsResponse.LocationDistribution> locationDistributions = tourRepository.getLocationDistributionStatistics();

            tourStatisticsResponse.setSummary(summary);
            tourStatisticsResponse.setTourPopularity(tourPopularities);
            tourStatisticsResponse.setBookingStatusDistribution(bookingStatusDistributions);
            tourStatisticsResponse.setCategoryPerformance(categoryPerformances);
            tourStatisticsResponse.setTypeDistribution(typeDistributions);
            tourStatisticsResponse.setLocationDistribution(locationDistributions);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour statistics from database");
        } finally {
            LOGGER.info("End fetching tour statistics from repository");
        }
    }

    @Override
    public CommonResponse<TourScheduleStatisticsResponse> getTourScheduleStatistics() {
        LOGGER.info("Start fetching tour schedule statistics from repository");
        try {
            TourScheduleStatisticsResponse tourScheduleStatisticsResponse = new TourScheduleStatisticsResponse();
            TourScheduleStatisticsResponse.Summary summary = tourRepository.getTourScheduleSummeryStatistics();
            List<TourScheduleStatisticsResponse.ScheduleTimeline> scheduleTimelines = tourRepository.getScheduleTimelineStatistics();
            List<TourScheduleStatisticsResponse.DurationDistribution> durationDistributions = tourRepository.getDurationDistributionStatistics();
            List<TourScheduleStatisticsResponse.ScheduleExecutionPerformance> scheduleExecutionPerformances = tourRepository.getScheduleExecutionPerformanceStatistics();
            List<TourScheduleStatisticsResponse.ScheduleRatingOverview> scheduleRatingOverviews = tourRepository.getScheduleRatingOverviewStatistics();
            List<TourScheduleStatisticsResponse.ParticipationTrend> participationTrends = tourRepository.getParticipationTrendStatistics();

            tourScheduleStatisticsResponse.setSummary(summary);
            tourScheduleStatisticsResponse.setScheduleTimeline(scheduleTimelines);
            tourScheduleStatisticsResponse.setDurationDistribution(durationDistributions);
            tourScheduleStatisticsResponse.setExecutionPerformance(scheduleExecutionPerformances);
            tourScheduleStatisticsResponse.setRatingOverview(scheduleRatingOverviews);
            tourScheduleStatisticsResponse.setParticipationTrend(participationTrends);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourScheduleStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour schedule statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour schedule statistics from database");
        } finally {
            LOGGER.info("End fetching tour schedule statistics from repository");
        }
    }

    @Override
    public CommonResponse<TourCategoryStatisticsResponse> getTourCategoryStatistics() {
        LOGGER.info("Start fetching tour category statistics from repository");
        try {
            TourCategoryStatisticsResponse tourCategoryStatisticsResponse = new TourCategoryStatisticsResponse();

            TourCategoryStatisticsResponse.Summary summary = tourRepository.getTourCategorySummaryStatistics();
            List<TourCategoryStatisticsResponse.CategoryDistribution> categoryDistribution = tourRepository.getCategoryDistributionStatistics();
            List<TourCategoryStatisticsResponse.CategoryBookingPerformance> categoryBookingPerformance = tourRepository.getCategoryBookingPerformanceStatistics();
            List<TourCategoryStatisticsResponse.CategoryRatingOverview> categoryRatingOverview = tourRepository.getCategoryRatingOverviewStatistics();
            List<TourCategoryStatisticsResponse.CategoryPrimarySecondaryUsage> categoryPrimarySecondaryUsage = tourRepository.getCategoryPrimarySecondaryUsageStatistics();
            List<TourCategoryStatisticsResponse.CategoryParticipationImpact> categoryParticipationImpact = tourRepository.getCategoryParticipationImpactStatistics();

            // Set all the data to the response object
            tourCategoryStatisticsResponse.setSummary(summary);
            tourCategoryStatisticsResponse.setCategoryDistribution(categoryDistribution);
            tourCategoryStatisticsResponse.setCategoryBookingPerformance(categoryBookingPerformance);
            tourCategoryStatisticsResponse.setCategoryRatingOverview(categoryRatingOverview);
            tourCategoryStatisticsResponse.setCategoryPrimarySecondaryUsage(categoryPrimarySecondaryUsage);
            tourCategoryStatisticsResponse.setCategoryParticipationImpact(categoryParticipationImpact);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourCategoryStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour category statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour category statistics from database");
        } finally {
            LOGGER.info("End fetching tour category statistics from repository");
        }
    }

    @Override
    public CommonResponse<TourTypeStatisticsResponse> getTourTypeStatistics() {
        LOGGER.info("Start fetching tour type statistics from repository");
        try {
            TourTypeStatisticsResponse tourTypeStatisticsResponse = new TourTypeStatisticsResponse();

            // Fetch all the required statistics from repository
            TourTypeStatisticsResponse.Summary summary = tourRepository.getTourTypeSummaryStatistics();
            List<TourTypeStatisticsResponse.TypeDistribution> typeDistribution = tourRepository.getTypesDistributionStatistics();
            List<TourTypeStatisticsResponse.TypeBookingPerformance> typeBookingPerformance = tourRepository.getTypeBookingPerformanceStatistics();
            List<TourTypeStatisticsResponse.TypeRatingOverview> typeRatingOverview = tourRepository.getTypeRatingOverviewStatistics();
            List<TourTypeStatisticsResponse.TypeParticipationImpact> typeParticipationImpact = tourRepository.getTypeParticipationImpactStatistics();
            List<TourTypeStatisticsResponse.TypePrimarySecondaryUsage> typePrimarySecondaryUsage = tourRepository.getTypePrimarySecondaryUsageStatistics();

            // Set all the data to the response object
            tourTypeStatisticsResponse.setSummary(summary);
            tourTypeStatisticsResponse.setTypeDistribution(typeDistribution);
            tourTypeStatisticsResponse.setTypeBookingPerformance(typeBookingPerformance);
            tourTypeStatisticsResponse.setTypeRatingOverview(typeRatingOverview);
            tourTypeStatisticsResponse.setTypeParticipationImpact(typeParticipationImpact);
            tourTypeStatisticsResponse.setTypePrimarySecondaryUsage(typePrimarySecondaryUsage);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourTypeStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour type statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour type statistics from database");
        } finally {
            LOGGER.info("End fetching tour type statistics from repository");
        }
    }

    @Override
    public CommonResponse<List<TourCategoryBasicDetailsResponse>> getTourCategories() {
        LOGGER.info("Start fetching tour categories from repository");
        try {
            List<TourCategoryBasicDetailsResponse> tourCategories = tourRepository.getTourCategories();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourCategories,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour categories: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour categories from database");
        } finally {
            LOGGER.info("End fetching tour categories from repository");
        }
    }

    @Override
    public CommonResponse<TourCategoryAllDetailsResponse> getTourCategoryDetailsById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start fetching tour category details by id from repository");
        try {
            TourCategoryAllDetailsResponse tourCategoryDetails = tourRepository.getTourCategoryDetailsById(commonIdRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourCategoryDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour category details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour category details from database");
        } finally {
            LOGGER.info("End fetching tour category details from repository");
        }
    }

    @Override
    public CommonResponse<TourCategoryBasicDetailsResponse> getTourCategoryBasicDetailsById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start fetching tour category basic details by id from repository");
        try {
            TourCategoryBasicDetailsResponse tourCategoryBasicDetails = tourRepository.getTourCategoryBasicDetailsById(commonIdRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourCategoryBasicDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching tour category basic details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour category basic details from database");
        } finally {
            LOGGER.info("End fetching tour category basic details from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateTourCategory(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start terminating tour category by id from repository");
        try {
            tourValidationService.validateCommonIdRequest(commonIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            TourCategoryBasicDetailsResponse tourCategoryResponse = getTourCategoryBasicDetailsById(commonIdRequest).getData();
            tourRepository.terminateTourCategory(commonIdRequest,userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_CATEGORY_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Tour Category Terminated")
                    .message("The tour category '" + tourCategoryResponse.getCategoryName() + "' has been terminated.")
                    .actionUrl(VIEW_TOUR_CATEGORY_DETAILS + "/" + tourCategoryResponse.getCategoryId())
                    .actionText("View Tour Category")
                    .icon("FolderX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "tourCategoryId", tourCategoryResponse.getCategoryId(),
                            "tourCategoryName", tourCategoryResponse.getCategoryName(),
                            "status", tourCategoryResponse.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_CATEGORY_TERMINATE.name())
                    .sourceModule(SourceModule.TOUR_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.TOUR_CATEGORY_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = tourEmailHelperService.buildTourCategoryTerminateSuccessfullSubject(loggedUser, tourCategoryResponse);
            String body = tourEmailHelperService.buildTourCategoryTerminateSuccessfullBody(loggedUser, tourCategoryResponse);

//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse(""),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating tour category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate tour category in database");
        } finally {
            LOGGER.info("End terminating tour category from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertTourCategory(TourCategoryInsertRequest tourCategoryInsertRequest) {
        LOGGER.info("Start inserting tour category from repository");
        try {
            tourValidationService.validateTourCategoryInsertRequest(tourCategoryInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long tourCategoryId = tourRepository.insertTourCategoryBasicDetails(tourCategoryInsertRequest, userId);
            tourRepository.insertTourCatgeoryImages(tourCategoryId,tourCategoryInsertRequest.getImages(), userId);
            tourRepository.insertToursForTourCategory(tourCategoryId,tourCategoryInsertRequest.getTourIds(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_CATEGORY_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Tour Category Created")
                    .message("A new tour category '" + tourCategoryInsertRequest.getCategoryName() + "' has been created.")
                    .actionUrl(VIEW_TOUR_CATEGORY_DETAILS + "/" + tourCategoryId)
                    .actionText("View Tour Category")
                    .icon("FolderPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "tourCategoryId", tourCategoryId,
                            "tourCategoryName", tourCategoryInsertRequest.getCategoryName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_CATEGORY_CREATE.name())
                    .sourceModule(SourceModule.TOUR_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (tourCategoryId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.TOUR_CATEGORY_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = tourEmailHelperService.buildTourCategoryCreateSuccessfullBody(tourCategoryId, tourCategoryInsertRequest, loggedUser);
                String subject = tourEmailHelperService.buildTourCategoryCreateSuccessfullSubject(tourCategoryId, tourCategoryInsertRequest, loggedUser);
//                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse(""),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while inserting tour category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to insert tour category in database");
        } finally {
            LOGGER.info("End inserting tour category from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateTourCategory(TourCategoryUpdateRequest tourCategoryUpdateRequest) {
        LOGGER.info("Start updating tour category from repository");
        try {
            tourValidationService.vaidateTourCategoryUpdateRequest(tourCategoryUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            TourCategoryBasicDetailsResponse previousTourCategoryResponse = getTourCategoryBasicDetailsById(new CommonIdRequest(tourCategoryUpdateRequest.getCategoryId())).getData();

            tourRepository.updateTourCategoryBasicDetails(tourCategoryUpdateRequest, userId);

            tourRepository.insertToursForTourCategory(tourCategoryUpdateRequest.getCategoryId(), tourCategoryUpdateRequest.getAddTourIds(),userId);
            tourRepository.removeToursForTourCategory(tourCategoryUpdateRequest.getCategoryId(), tourCategoryUpdateRequest.getRemoveTourIds(), userId);

            tourRepository.insertTourCatgeoryImages(tourCategoryUpdateRequest.getCategoryId(), tourCategoryUpdateRequest.getAddImages(), userId);
            tourRepository.removeTourCatgeoryImages(tourCategoryUpdateRequest.getCategoryId(), tourCategoryUpdateRequest.getRemoveImageIds(), userId);
            tourRepository.updateTourCatgeoryImages(tourCategoryUpdateRequest.getCategoryId(), tourCategoryUpdateRequest.getUpdateImages(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.TOUR_CATEGORY_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Tour Category Updated")
                    .message("The tour category '" + tourCategoryUpdateRequest.getCategoryName() + "' has been updated.")
                    .actionUrl(VIEW_TOUR_CATEGORY_DETAILS + "/" + tourCategoryUpdateRequest.getCategoryId())
                    .actionText("View Tour Category")
                    .icon("FolderEdit")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "tourCategoryId", tourCategoryUpdateRequest.getCategoryId(),
                            "tourCategoryName", tourCategoryUpdateRequest.getCategoryName(),
                            "status", tourCategoryUpdateRequest.getStatus(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.TOUR_CATEGORY_UPDATE.name())
                    .sourceModule(SourceModule.TOUR_CATEGORY.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            TourCategoryComparisonResult comparisonResult = compareTourCategoryUpdates(
                    previousTourCategoryResponse,
                    tourCategoryUpdateRequest,
                    loggedUser
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.TOUR_CATEGORY_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = tourEmailHelperService.buildTourCategoryUpdateSuccessfullSubject(loggedUser, tourCategoryUpdateRequest);
            String body = tourEmailHelperService.buildTourCategoryUpdateSuccessfullBody(loggedUser, tourCategoryUpdateRequest, comparisonResult);
//            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse(),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating tour category: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update tour category in database");
        } finally {
            LOGGER.info("End updating tour category from repository");
        }
    }

    private TourCategoryComparisonResult compareTourCategoryUpdates(
            TourCategoryBasicDetailsResponse previousTourCategoryResponse,
            TourCategoryUpdateRequest tourCategoryUpdateRequest,
            User loggedUser) {

        TourCategoryComparisonResult.TourCategoryComparisonResultBuilder resultBuilder =
                TourCategoryComparisonResult.builder();

        List<TourCategoryComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        boolean hasChanges = false;
        List<String> warnings = new ArrayList<>();

        // Compare categoryName
        if (tourCategoryUpdateRequest.getCategoryName() != null &&
                previousTourCategoryResponse.getCategoryName() != null &&
                !tourCategoryUpdateRequest.getCategoryName().equals(previousTourCategoryResponse.getCategoryName())) {
            changes.add(String.format("Category Name changed from '%s' to '%s'",
                    previousTourCategoryResponse.getCategoryName(),
                    tourCategoryUpdateRequest.getCategoryName()));
            fieldChanges.add(new TourCategoryComparisonResult.FieldChange(
                    "categoryName",
                    previousTourCategoryResponse.getCategoryName(),
                    tourCategoryUpdateRequest.getCategoryName(),
                    "Category Name"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(tourCategoryUpdateRequest.getDescription(), previousTourCategoryResponse.getDescription())) {
            String oldDesc = previousTourCategoryResponse.getDescription() != null ?
                    previousTourCategoryResponse.getDescription() : "null";
            String newDesc = tourCategoryUpdateRequest.getDescription() != null ?
                    tourCategoryUpdateRequest.getDescription() : "null";
            changes.add(String.format("Description changed from '%s' to '%s'", oldDesc, newDesc));
            fieldChanges.add(new TourCategoryComparisonResult.FieldChange(
                    "description",
                    previousTourCategoryResponse.getDescription(),
                    tourCategoryUpdateRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare color
        if (tourCategoryUpdateRequest.getColor() != null &&
                previousTourCategoryResponse.getColor() != null &&
                !tourCategoryUpdateRequest.getColor().equals(previousTourCategoryResponse.getColor())) {
            changes.add(String.format("Color changed from '%s' to '%s'",
                    previousTourCategoryResponse.getColor(),
                    tourCategoryUpdateRequest.getColor()));
            fieldChanges.add(new TourCategoryComparisonResult.FieldChange(
                    "color",
                    previousTourCategoryResponse.getColor(),
                    tourCategoryUpdateRequest.getColor(),
                    "Color"));
            hasChanges = true;
        }

        // Compare hoverColor
        if (tourCategoryUpdateRequest.getHoverColor() != null &&
                previousTourCategoryResponse.getHoverColor() != null &&
                !tourCategoryUpdateRequest.getHoverColor().equals(previousTourCategoryResponse.getHoverColor())) {
            changes.add(String.format("Hover Color changed from '%s' to '%s'",
                    previousTourCategoryResponse.getHoverColor(),
                    tourCategoryUpdateRequest.getHoverColor()));
            fieldChanges.add(new TourCategoryComparisonResult.FieldChange(
                    "hoverColor",
                    previousTourCategoryResponse.getHoverColor(),
                    tourCategoryUpdateRequest.getHoverColor(),
                    "Hover Color"));
            hasChanges = true;
        }

        // Compare status
        String oldStatus = previousTourCategoryResponse.getStatus();
        String newStatus = tourCategoryUpdateRequest.getStatus();
        if (oldStatus != null && newStatus != null && !oldStatus.equals(newStatus)) {
            changes.add(String.format("Status changed from '%s' to '%s'", oldStatus, newStatus));
            fieldChanges.add(new TourCategoryComparisonResult.FieldChange(
                    "status",
                    oldStatus,
                    newStatus,
                    "Status"));
            hasChanges = true;
        }

        // Handle tours to add
        List<Long> toursToAdd = new ArrayList<>();
        if (tourCategoryUpdateRequest.getAddTourIds() != null &&
                !tourCategoryUpdateRequest.getAddTourIds().isEmpty()) {
            toursToAdd.addAll(tourCategoryUpdateRequest.getAddTourIds());
            changes.add(String.format("Tours to add: %s", tourCategoryUpdateRequest.getAddTourIds()));
            hasChanges = true;
        }

        // Handle tours to remove
        List<Long> toursToRemove = new ArrayList<>();
        if (tourCategoryUpdateRequest.getRemoveTourIds() != null &&
                !tourCategoryUpdateRequest.getRemoveTourIds().isEmpty()) {
            toursToRemove.addAll(tourCategoryUpdateRequest.getRemoveTourIds());
            changes.add(String.format("Tours to remove: %s", tourCategoryUpdateRequest.getRemoveTourIds()));
            hasChanges = true;
        }

        // Validate tour changes
        if (!toursToRemove.isEmpty() && !toursToAdd.isEmpty()) {
            // Check for conflicts (same tour in both lists)
            List<Long> conflicts = toursToRemove.stream()
                    .filter(toursToAdd::contains)
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                warnings.add(String.format("Warning: Tours %s are both being added and removed!", conflicts));
            }
        }

        // Handle images to add
        List<TourCategoryComparisonResult.ImageChange> imagesToAdd = new ArrayList<>();
        if (tourCategoryUpdateRequest.getAddImages() != null &&
                !tourCategoryUpdateRequest.getAddImages().isEmpty()) {
            for (TourCategoryImageInsertRequest imageRequest : tourCategoryUpdateRequest.getAddImages()) {
                TourCategoryComparisonResult.ImageChange imageChange =
                        TourCategoryComparisonResult.ImageChange.builder()
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
        if (tourCategoryUpdateRequest.getRemoveImageIds() != null &&
                !tourCategoryUpdateRequest.getRemoveImageIds().isEmpty()) {
            imagesToRemove.addAll(tourCategoryUpdateRequest.getRemoveImageIds());
            changes.add(String.format("Images to remove IDs: %s", tourCategoryUpdateRequest.getRemoveImageIds()));
            hasChanges = true;
        }

        // Handle images to update
        List<TourCategoryComparisonResult.ImageUpdateChange> imagesToUpdate = new ArrayList<>();
        if (tourCategoryUpdateRequest.getUpdateImages() != null &&
                !tourCategoryUpdateRequest.getUpdateImages().isEmpty()) {

            // Find existing images in previous response for comparison
            Map<Long, TourCategoryImageResponse> existingImagesMap = new HashMap<>();
            if (previousTourCategoryResponse.getImages() != null) {
                existingImagesMap = previousTourCategoryResponse.getImages().stream()
                        .collect(Collectors.toMap(
                                TourCategoryImageResponse::getImageId,
                                image -> image
                        ));
            }

            for (TourCategoryImageUpdateRequest updateRequest : tourCategoryUpdateRequest.getUpdateImages()) {
                TourCategoryImageResponse existingImage = existingImagesMap.get(updateRequest.getImageId());

                if (existingImage != null) {
                    // Check if there are actual changes
                    boolean hasImageChanges = false;
                    String oldName = existingImage.getName();
                    String newName = updateRequest.getName();
                    String oldDescription = existingImage.getDescription();
                    String newDescription = updateRequest.getDescription();
                    String oldImageUrl = existingImage.getImageUrl();
                    String newImageUrl = updateRequest.getImageUrl();
                    String oldImgStatus = existingImage.getStatus();
                    String newImgStatus = updateRequest.getStatus();

                    if (!Objects.equals(oldName, newName)) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d name changed from '%s' to '%s'",
                                updateRequest.getImageId(), oldName, newName));
                    }
                    if (!Objects.equals(oldDescription, newDescription)) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d description changed from '%s' to '%s'",
                                updateRequest.getImageId(), oldDescription, newDescription));
                    }
                    if (!Objects.equals(oldImageUrl, newImageUrl)) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d URL changed", updateRequest.getImageId()));
                    }
                    if (!Objects.equals(oldImgStatus, newImgStatus)) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d status changed from '%s' to '%s'",
                                updateRequest.getImageId(), oldImgStatus, newImgStatus));
                    }

                    if (hasImageChanges) {
                        TourCategoryComparisonResult.ImageUpdateChange imageUpdateChange =
                                TourCategoryComparisonResult.ImageUpdateChange.builder()
                                        .imageId(updateRequest.getImageId())
                                        .oldName(oldName)
                                        .newName(newName)
                                        .oldDescription(oldDescription)
                                        .newDescription(newDescription)
                                        .oldImageUrl(oldImageUrl)
                                        .newImageUrl(newImageUrl)
                                        .oldStatus(oldImgStatus)
                                        .newStatus(newImgStatus)
                                        .build();
                        imagesToUpdate.add(imageUpdateChange);
                        hasChanges = true;
                    }
                } else {
                    warnings.add(String.format("Warning: Image with ID %d not found for update",
                            updateRequest.getImageId()));
                }
            }
        }

        // Additional warnings for image operations
        if (!imagesToAdd.isEmpty() && imagesToAdd.size() > 10) {
            warnings.add("Warning: Adding more than 10 images at once might impact performance");
        }

        if (!imagesToRemove.isEmpty() && !imagesToAdd.isEmpty()) {
            warnings.add("Note: Adding and removing images in the same operation");
        }

        // Warning for status change implications
        if (oldStatus != null && newStatus != null &&
                !oldStatus.equals(newStatus) && "INACTIVE".equals(newStatus)) {
            warnings.add("Warning: Changing status to INACTIVE will hide this category from users");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .toursToAdd(toursToAdd)
                .toursToRemove(toursToRemove)
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

    private TourComparisonResult compareTourUpdates(
            TourUpdateRequest tourUpdateRequest,
            TourAllDetailsResponse previousTourData) {

        TourComparisonResult.TourComparisonResultBuilder resultBuilder =
                TourComparisonResult.builder();

        List<TourComparisonResult.FieldChange> fieldChanges = new ArrayList<>();

        // Compare basic details if present
        if (tourUpdateRequest.getTourBasicDetails() != null) {
            TourUpdateRequest.TourBasicDetails basicDetails = tourUpdateRequest.getTourBasicDetails();

            if (basicDetails.getTourName() != null &&
                    !basicDetails.getTourName().equals(previousTourData.getTourName())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "tourName", previousTourData.getTourName(), basicDetails.getTourName()));
            }

            if (basicDetails.getTourDescription() != null &&
                    !basicDetails.getTourDescription().equals(previousTourData.getTourDescription())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "tourDescription", previousTourData.getTourDescription(), basicDetails.getTourDescription()));
            }

            if (basicDetails.getDuration() != null &&
                    !basicDetails.getDuration().equals(previousTourData.getDuration())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "duration", previousTourData.getDuration(), basicDetails.getDuration()));
            }

            if (basicDetails.getLatitude() != null) {
                BigDecimal oldLatitude = previousTourData.getLatitude() != null ?
                        BigDecimal.valueOf(previousTourData.getLatitude()) : null;
                if (!basicDetails.getLatitude().equals(oldLatitude)) {
                    fieldChanges.add(new TourComparisonResult.FieldChange(
                            "latitude", oldLatitude, basicDetails.getLatitude()));
                }
            }

            if (basicDetails.getLongitude() != null) {
                BigDecimal oldLongitude = previousTourData.getLongitude() != null ?
                        BigDecimal.valueOf(previousTourData.getLongitude()) : null;
                if (!basicDetails.getLongitude().equals(oldLongitude)) {
                    fieldChanges.add(new TourComparisonResult.FieldChange(
                            "longitude", oldLongitude, basicDetails.getLongitude()));
                }
            }

            if (basicDetails.getStartLocation() != null &&
                    !basicDetails.getStartLocation().equals(previousTourData.getStartLocation())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "startLocation", previousTourData.getStartLocation(), basicDetails.getStartLocation()));
            }

            if (basicDetails.getEndLocation() != null &&
                    !basicDetails.getEndLocation().equals(previousTourData.getEndLocation())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "endLocation", previousTourData.getEndLocation(), basicDetails.getEndLocation()));
            }

            if (basicDetails.getSeason() != null &&
                    !basicDetails.getSeason().equals(previousTourData.getSeasonName())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "season", previousTourData.getSeasonName(), basicDetails.getSeason()));
            }

            if (basicDetails.getStatus() != null &&
                    !basicDetails.getStatus().equals(previousTourData.getStatusName())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "status", previousTourData.getStatusName(), basicDetails.getStatus()));
            }

            if (basicDetails.getAssignTo() != null &&
                    !basicDetails.getAssignTo().equals(previousTourData.getAssignTo())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "assignTo", previousTourData.getAssignTo(), basicDetails.getAssignTo()));
            }

            if (basicDetails.getAssignMessage() != null &&
                    !basicDetails.getAssignMessage().equals(previousTourData.getAssignMessage())) {
                fieldChanges.add(new TourComparisonResult.FieldChange(
                        "assignMessage", previousTourData.getAssignMessage(), basicDetails.getAssignMessage()));
            }
        }

        resultBuilder.basicDetailsChanges(fieldChanges);

        // Tour Types changes
        resultBuilder.tourTypeIdsToAdd(tourUpdateRequest.getAddTourTypes() != null ?
                tourUpdateRequest.getAddTourTypes() : Collections.emptyList());
        resultBuilder.tourTypeIdsToRemove(tourUpdateRequest.getRemoveTourTypes() != null ?
                tourUpdateRequest.getRemoveTourTypes() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateTourTypes() != null) {
            List<TourComparisonResult.TourTypeChange> tourTypesToUpdate =
                    tourUpdateRequest.getUpdateTourTypes().stream()
                            .map(type -> TourComparisonResult.TourTypeChange.builder()
                                    .tourTypeId(type.getTourTypeId())
                                    .isPrimary(type.getIsPrimary())
                                    .status(type.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.tourTypesToUpdate(tourTypesToUpdate);
        }

        // Tour Categories changes
        resultBuilder.tourCategoryIdsToAdd(tourUpdateRequest.getAddTourCategories() != null ?
                tourUpdateRequest.getAddTourCategories() : Collections.emptyList());
        resultBuilder.tourCategoryIdsToRemove(tourUpdateRequest.getRemoveTourCategories() != null ?
                tourUpdateRequest.getRemoveTourCategories() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateTourCategories() != null) {
            List<TourComparisonResult.TourCategoryChange> tourCategoriesToUpdate =
                    tourUpdateRequest.getUpdateTourCategories().stream()
                            .map(cat -> TourComparisonResult.TourCategoryChange.builder()
                                    .tourCategoryId(cat.getTourCategoryId())
                                    .isPrimary(cat.getIsPrimary())
                                    .status(cat.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.tourCategoriesToUpdate(tourCategoriesToUpdate);
        }

        // Destinations changes
        if (tourUpdateRequest.getItinerary() != null) {
            List<TourComparisonResult.TourDestinationChange> destinationsToAdd =
                    tourUpdateRequest.getItinerary().stream()
                            .map(dest -> TourComparisonResult.TourDestinationChange.builder()
                                    .destinationId(dest.getDestinations().getFirst().getDestinationId())
                                    .activityId(dest.getDestinations().getLast().getDestinationId())
                                    .dayNumber(dest.getDayNumber())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.destinationsToAdd(destinationsToAdd);
        }

        resultBuilder.destinationIdsToRemove(tourUpdateRequest.getRemoveDestinations() != null ?
                tourUpdateRequest.getRemoveDestinations() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateDestinations() != null) {
            List<TourComparisonResult.TourDestinationChange> destinationsToUpdate =
                    tourUpdateRequest.getUpdateDestinations().stream()
                            .map(dest -> TourComparisonResult.TourDestinationChange.builder()
                                    .tourDestinationId(dest.getTourDestinationId())
                                    .destinationId(dest.getDestinationId())
                                    .activityId(dest.getActivityId())
                                    .dayNumber(dest.getDayNumber())
                                    .status(dest.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.destinationsToUpdate(destinationsToUpdate);
        }

        // Images changes
        if (tourUpdateRequest.getAddImages() != null) {
            List<TourComparisonResult.TourImageChange> imagesToAdd =
                    tourUpdateRequest.getAddImages().stream()
                            .map(img -> TourComparisonResult.TourImageChange.builder()
                                    .imageName(img.getImageName())
                                    .imageDescription(img.getImageDescription())
                                    .imageUrl(img.getImageUrl())
                                    .status(img.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToAdd(imagesToAdd);
        }

        resultBuilder.imageIdsToRemove(tourUpdateRequest.getRemoveImages() != null ?
                tourUpdateRequest.getRemoveImages() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateImages() != null) {
            List<TourComparisonResult.TourImageChange> imagesToUpdate =
                    tourUpdateRequest.getUpdateImages().stream()
                            .map(img -> TourComparisonResult.TourImageChange.builder()
                                    .imageId(img.getImageId())
                                    .imageName(img.getImageName())
                                    .imageDescription(img.getImageDescription())
                                    .imageUrl(img.getImageUrl())
                                    .status(img.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.imagesToUpdate(imagesToUpdate);
        }

        // Inclusions changes
        if (tourUpdateRequest.getAddInclusions() != null) {
            List<TourComparisonResult.TourInclusionChange> inclusionsToAdd =
                    tourUpdateRequest.getAddInclusions().stream()
                            .map(inc -> TourComparisonResult.TourInclusionChange.builder()
                                    .inclusionText(inc.getInclusionText())
                                    .displayOrder(inc.getDisplayOrder())
                                    .status(inc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.inclusionsToAdd(inclusionsToAdd);
        }

        resultBuilder.inclusionIdsToRemove(tourUpdateRequest.getRemoveInclusions() != null ?
                tourUpdateRequest.getRemoveInclusions() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateInclusions() != null) {
            List<TourComparisonResult.TourInclusionChange> inclusionsToUpdate =
                    tourUpdateRequest.getUpdateInclusions().stream()
                            .map(inc -> TourComparisonResult.TourInclusionChange.builder()
                                    .inclusionId(inc.getInclusionId())
                                    .inclusionText(inc.getInclusionText())
                                    .displayOrder(inc.getDisplayOrder())
                                    .status(inc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.inclusionsToUpdate(inclusionsToUpdate);
        }

        // Exclusions changes
        if (tourUpdateRequest.getAddExclusions() != null) {
            List<TourComparisonResult.TourExclusionChange> exclusionsToAdd =
                    tourUpdateRequest.getAddExclusions().stream()
                            .map(exc -> TourComparisonResult.TourExclusionChange.builder()
                                    .exclusionText(exc.getExclusionText())
                                    .displayOrder(exc.getDisplayOrder())
                                    .status(exc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.exclusionsToAdd(exclusionsToAdd);
        }

        resultBuilder.exclusionIdsToRemove(tourUpdateRequest.getRemoveExclusions() != null ?
                tourUpdateRequest.getRemoveExclusions() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateExclusions() != null) {
            List<TourComparisonResult.TourExclusionChange> exclusionsToUpdate =
                    tourUpdateRequest.getUpdateExclusions().stream()
                            .map(exc -> TourComparisonResult.TourExclusionChange.builder()
                                    .exclusionId(exc.getExclusionId())
                                    .exclusionText(exc.getExclusionText())
                                    .displayOrder(exc.getDisplayOrder())
                                    .status(exc.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.exclusionsToUpdate(exclusionsToUpdate);
        }

        // Conditions changes
        if (tourUpdateRequest.getAddConditions() != null) {
            List<TourComparisonResult.TourConditionChange> conditionsToAdd =
                    tourUpdateRequest.getAddConditions().stream()
                            .map(cond -> TourComparisonResult.TourConditionChange.builder()
                                    .conditionText(cond.getConditionText())
                                    .displayOrder(cond.getDisplayOrder())
                                    .status(cond.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.conditionsToAdd(conditionsToAdd);
        }

        resultBuilder.conditionIdsToRemove(tourUpdateRequest.getRemoveConditions() != null ?
                tourUpdateRequest.getRemoveConditions() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateConditions() != null) {
            List<TourComparisonResult.TourConditionChange> conditionsToUpdate =
                    tourUpdateRequest.getUpdateConditions().stream()
                            .map(cond -> TourComparisonResult.TourConditionChange.builder()
                                    .conditionId(cond.getConditionId())
                                    .conditionText(cond.getConditionText())
                                    .displayOrder(cond.getDisplayOrder())
                                    .status(cond.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.conditionsToUpdate(conditionsToUpdate);
        }

        // Travel Tips changes
        if (tourUpdateRequest.getAddTravelTips() != null) {
            List<TourComparisonResult.TourTravelTipChange> travelTipsToAdd =
                    tourUpdateRequest.getAddTravelTips().stream()
                            .map(tip -> TourComparisonResult.TourTravelTipChange.builder()
                                    .tipTitle(tip.getTipTitle())
                                    .tipDescription(tip.getTipDescription())
                                    .displayOrder(tip.getDisplayOrder())
                                    .status(tip.getStatus())
                                    .build())
                            .collect(Collectors.toList());
            resultBuilder.travelTipsToAdd(travelTipsToAdd);
        }

        resultBuilder.travelTipIdsToRemove(tourUpdateRequest.getRemoveTravelTips() != null ?
                tourUpdateRequest.getRemoveTravelTips() : Collections.emptyList());

        if (tourUpdateRequest.getUpdateTravelTips() != null) {
            List<TourComparisonResult.TourTravelTipChange> travelTipsToUpdate =
                    tourUpdateRequest.getUpdateTravelTips().stream()
                            .map(tip -> TourComparisonResult.TourTravelTipChange.builder()
                                    .travelTipId(tip.getTravelTipId())
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
                (tourUpdateRequest.getAddTourTypes() != null && !tourUpdateRequest.getAddTourTypes().isEmpty()) ||
                (tourUpdateRequest.getRemoveTourTypes() != null && !tourUpdateRequest.getRemoveTourTypes().isEmpty()) ||
                (tourUpdateRequest.getUpdateTourTypes() != null && !tourUpdateRequest.getUpdateTourTypes().isEmpty()) ||
                (tourUpdateRequest.getAddTourCategories() != null && !tourUpdateRequest.getAddTourCategories().isEmpty()) ||
                (tourUpdateRequest.getRemoveTourCategories() != null && !tourUpdateRequest.getRemoveTourCategories().isEmpty()) ||
                (tourUpdateRequest.getUpdateTourCategories() != null && !tourUpdateRequest.getUpdateTourCategories().isEmpty()) ||
                (tourUpdateRequest.getItinerary() != null && !tourUpdateRequest.getItinerary().isEmpty()) ||
                (tourUpdateRequest.getRemoveDestinations() != null && !tourUpdateRequest.getRemoveDestinations().isEmpty()) ||
                (tourUpdateRequest.getUpdateDestinations() != null && !tourUpdateRequest.getUpdateDestinations().isEmpty()) ||
                (tourUpdateRequest.getAddImages() != null && !tourUpdateRequest.getAddImages().isEmpty()) ||
                (tourUpdateRequest.getRemoveImages() != null && !tourUpdateRequest.getRemoveImages().isEmpty()) ||
                (tourUpdateRequest.getUpdateImages() != null && !tourUpdateRequest.getUpdateImages().isEmpty()) ||
                (tourUpdateRequest.getAddInclusions() != null && !tourUpdateRequest.getAddInclusions().isEmpty()) ||
                (tourUpdateRequest.getRemoveInclusions() != null && !tourUpdateRequest.getRemoveInclusions().isEmpty()) ||
                (tourUpdateRequest.getUpdateInclusions() != null && !tourUpdateRequest.getUpdateInclusions().isEmpty()) ||
                (tourUpdateRequest.getAddExclusions() != null && !tourUpdateRequest.getAddExclusions().isEmpty()) ||
                (tourUpdateRequest.getRemoveExclusions() != null && !tourUpdateRequest.getRemoveExclusions().isEmpty()) ||
                (tourUpdateRequest.getUpdateExclusions() != null && !tourUpdateRequest.getUpdateExclusions().isEmpty()) ||
                (tourUpdateRequest.getAddConditions() != null && !tourUpdateRequest.getAddConditions().isEmpty()) ||
                (tourUpdateRequest.getRemoveConditions() != null && !tourUpdateRequest.getRemoveConditions().isEmpty()) ||
                (tourUpdateRequest.getUpdateConditions() != null && !tourUpdateRequest.getUpdateConditions().isEmpty()) ||
                (tourUpdateRequest.getAddTravelTips() != null && !tourUpdateRequest.getAddTravelTips().isEmpty()) ||
                (tourUpdateRequest.getRemoveTravelTips() != null && !tourUpdateRequest.getRemoveTravelTips().isEmpty()) ||
                (tourUpdateRequest.getUpdateTravelTips() != null && !tourUpdateRequest.getUpdateTravelTips().isEmpty());

        resultBuilder.hasChanges(hasChanges);

        // Create summary
        String summary = createTourSummary(resultBuilder.build());
        resultBuilder.summary(summary);

        return resultBuilder.build();
    }

    private String createTourSummary(TourComparisonResult result) {
        StringBuilder summary = new StringBuilder();

        if (!result.isHasChanges()) {
            return "No changes detected";
        }

        if (!result.getBasicDetailsChanges().isEmpty()) {
            summary.append("Basic details updated: ")
                    .append(result.getBasicDetailsChanges().stream()
                            .map(TourComparisonResult.FieldChange::getFieldName)
                            .collect(Collectors.joining(", ")))
                    .append(". ");
        }

        if (!result.getTourTypeIdsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getTourTypeIdsToAdd().size()).append(" tour types. ");
        }

        if (!result.getTourTypeIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getTourTypeIdsToRemove().size()).append(" tour types. ");
        }

        if (!result.getTourTypesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getTourTypesToUpdate().size()).append(" tour types. ");
        }

        if (!result.getTourCategoryIdsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getTourCategoryIdsToAdd().size()).append(" tour categories. ");
        }

        if (!result.getTourCategoryIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getTourCategoryIdsToRemove().size()).append(" tour categories. ");
        }

        if (!result.getTourCategoriesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getTourCategoriesToUpdate().size()).append(" tour categories. ");
        }

        if (!result.getDestinationsToAdd().isEmpty()) {
            summary.append("Add ").append(result.getDestinationsToAdd().size()).append(" destinations. ");
        }

        if (!result.getDestinationIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getDestinationIdsToRemove().size()).append(" destinations. ");
        }

        if (!result.getDestinationsToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getDestinationsToUpdate().size()).append(" destinations. ");
        }

        if (!result.getImagesToAdd().isEmpty()) {
            summary.append("Add ").append(result.getImagesToAdd().size()).append(" images. ");
        }

        if (!result.getImageIdsToRemove().isEmpty()) {
            summary.append("Remove ").append(result.getImageIdsToRemove().size()).append(" images. ");
        }

        if (!result.getImagesToUpdate().isEmpty()) {
            summary.append("Update ").append(result.getImagesToUpdate().size()).append(" images. ");
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
