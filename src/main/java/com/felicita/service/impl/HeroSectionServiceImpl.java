package com.felicita.service.impl;

import com.felicita.email.HeroSectionEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.enums.*;
import com.felicita.model.other.HeroSectionComparisonResult;
import com.felicita.model.request.common.IdWithTypeRequest;
import com.felicita.model.request.heroSection.*;
import com.felicita.model.response.*;
import com.felicita.model.response.heroSection.HeroSectionBasicResponse;
import com.felicita.model.response.heroSection.HeroSectionDataForParamsResponse;
import com.felicita.model.response.heroSection.HeroSectionDetailsResponse;
import com.felicita.model.response.heroSection.HeroSectionParamResponse;
import com.felicita.model.response.statistics.HeroSectionStatisticsResponse;
import com.felicita.repository.HeroSectionRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.HeroSectionService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.HeroSectionValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_HERO_SECTION_DETAILS;

@Service
public class HeroSectionServiceImpl implements HeroSectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroSectionServiceImpl.class);

    private final HeroSectionRepository heroSectionRepository;
    private final CommonService commonService;
    private final EmailService emailService;
    private final HeroSectionValidationService heroSectionValidationService;
    private final HeroSectionEmailHelperService heroSectionEmailHelperService;

    @Autowired
    public HeroSectionServiceImpl(HeroSectionRepository heroSectionRepository, CommonService commonService, EmailService emailService, HeroSectionValidationService heroSectionValidationService, HeroSectionEmailHelperService heroSectionEmailHelperService) {
        this.heroSectionRepository = heroSectionRepository;
        this.commonService = commonService;
        this.emailService = emailService;
        this.heroSectionValidationService = heroSectionValidationService;
        this.heroSectionEmailHelperService = heroSectionEmailHelperService;
    }

    @Override
    public CommonResponse<List<HeroSectionResponse>> getAllHomeHeroSectionData() {
        LOGGER.info("Start fetching home hero section all data from repository");
        try {
            List<HeroSectionResponse> heroSectionResponses = heroSectionRepository.getAllHomeHeroSectionData();

            if (heroSectionResponses.isEmpty()) {
                LOGGER.warn("No home hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No home hero section data found");
            }

            LOGGER.info("Fetched {} home hero section all data successfully", heroSectionResponses.size());
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching home hero section all data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch home hero section all data from database");
        } finally {
            LOGGER.info("End fetching home hero section all data from repository");
        }
    }

    @Override
    public CommonResponse<List<HeroSectionResponse>> getHomeHeroSectionDetails() {
        LOGGER.info("Start fetching home hero section data from repository");

        try {
            List<HeroSectionResponse> heroSectionResponses = getAllHomeHeroSectionData().getData();

            List<HeroSectionResponse> heroSectionResponsesList = heroSectionResponses.stream()
                    .filter(item -> HeroSectionItemStatus.ACTIVE.toString().equalsIgnoreCase(item.getImageStatus()))
                    .toList();

            if (heroSectionResponsesList.isEmpty()) {
                LOGGER.warn("No active home hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active home hero section data found in database");
            }

            LOGGER.info("Fetched {} active home hero section data successfully", heroSectionResponsesList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionResponsesList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active home hero data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch active home hero data from database");
        } finally {
            LOGGER.info("End fetching active home hero data from repository");
        }
    }

    @Override
    public CommonResponse<List<AboutUsHeroSectionResponse>> getAboutUsHeroSectionDetails() {
        LOGGER.info("Start fetching about us hero section data from repository");

        try {
            List<AboutUsHeroSectionResponse> aboutUsHeroSectionResponses = heroSectionRepository.getAboutUsHeroSectionDetails();

            if (aboutUsHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No about us hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No about us hero section data found");
            }

            List<AboutUsHeroSectionResponse> heroSectionResponsesList = aboutUsHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatusName()))
                    .toList();

            if (heroSectionResponsesList.isEmpty()) {
                LOGGER.warn("No active about us hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active about us hero section data found");
            }

            LOGGER.info("Fetched {} active about us hero section data successfully", heroSectionResponsesList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    aboutUsHeroSectionResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active about us hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch about us hero section data from database");
        } finally {
            LOGGER.info("End fetching about us hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<ContactUsHeroSectionResponse>> getContactUsHeroSectionDetails() {
        LOGGER.info("Start fetching contact us hero section data from repository");

        try {
            List<ContactUsHeroSectionResponse> contactUsHeroSectionResponses = heroSectionRepository.getContactUsHeroSectionDetails();

            if (contactUsHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No contact us hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No contact us hero section data found");
            }

            List<ContactUsHeroSectionResponse> contactUsHeroSectionResponseList = contactUsHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatusName()))
                    .toList();

            if (contactUsHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active contact us hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active contact us hero section data found");
            }

            LOGGER.info("Fetched {} active contact us hero section data successfully", contactUsHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    contactUsHeroSectionResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active contact us hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch contact us hero section data from database");
        } finally {
            LOGGER.info("End fetching contact us hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<BlogHeroSectionResponse>> getBlogHeroSectionDetails() {
        LOGGER.info("Start fetching blog hero section data from repository");

        try {
            List<BlogHeroSectionResponse> blogHeroSectionResponses = heroSectionRepository.getBlogHeroSectionDetails();

            if (blogHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No blog hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No blog hero section data found");
            }

            List<BlogHeroSectionResponse> blogHeroSectionResponseList = blogHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatusName()))
                    .toList();

            if (blogHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active blog hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active blog hero section data found");
            }

            LOGGER.info("Fetched {} active blog hero section data successfully", blogHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    blogHeroSectionResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active blog hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch blog hero section data from database");
        } finally {
            LOGGER.info("End fetching blog hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<FaqHeroSectionResponse>> getFAQHeroSectionDetails() {
        LOGGER.info("Start fetching faq hero section data from repository");

        try {
            List<FaqHeroSectionResponse> faqHeroSectionResponses = heroSectionRepository.getFAQHeroSectionDetails();

            if (faqHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No faq hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No faq hero section data found");
            }

            List<FaqHeroSectionResponse> faqHeroSectionResponseList = faqHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (faqHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active faq hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active faq hero section data found");
            }

            LOGGER.info("Fetched {} active faq hero section data successfully", faqHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    faqHeroSectionResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active faq hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch faq hero section data from database");
        } finally {
            LOGGER.info("End fetching faq hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<TourHeroSectionResponse>> getTourHeroSectionDetails() {
        LOGGER.info("Start fetching tour hero section data from repository");

        try {
            List<TourHeroSectionResponse> tourHeroSectionResponses = heroSectionRepository.getTourHeroSectionDetails();

            if (tourHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No tour hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No tour hero section data found");
            }

            List<TourHeroSectionResponse> tourHeroSectionResponseList = tourHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (tourHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active tour hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active tour hero section data found");
            }

            LOGGER.info("Fetched {} active tour hero section data successfully", tourHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    tourHeroSectionResponseList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active tour hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch tour hero section data from database");
        } finally {
            LOGGER.info("End fetching tour hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageHeroSectionResponse>> getPackageHeroSectionDetails() {
        LOGGER.info("Start fetching package hero section data from repository");

        try {
            List<PackageHeroSectionResponse> packageHeroSectionResponses = heroSectionRepository.getPackageHeroSectionDetails();

            if (packageHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No package hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No package hero section data found");
            }

            List<PackageHeroSectionResponse> packageHeroSectionResponseList = packageHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (packageHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active package hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active package hero section data found");
            }

            LOGGER.info("Fetched {} active package hero section data successfully", packageHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageHeroSectionResponseList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active package hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package hero section data from database");
        } finally {
            LOGGER.info("End fetching package hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<DestinationHeroSectionResponse>> getDestinationHeroSectionDetails() {
        LOGGER.info("Start fetching destination hero section data from repository");

        try {
            List<DestinationHeroSectionResponse> destinationHeroSectionResponses = heroSectionRepository.getDestinationHeroSectionDetails();

            if (destinationHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No destination hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No destination hero section data found");
            }

            List<DestinationHeroSectionResponse> destinationHeroSectionResponseList = destinationHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (destinationHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active destination hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active destination hero section data found");
            }

            LOGGER.info("Fetched {} active destination hero section data successfully", destinationHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    destinationHeroSectionResponseList,
                    Instant.now()
            );

        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active destination hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch destination hero section data from database");
        } finally {
            LOGGER.info("End fetching destination hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityHeroSectionResponse>> getActivityHeroSectionDetails() {
        LOGGER.info("Start fetching activity hero section data from repository");

        try {
            List<ActivityHeroSectionResponse> activityHeroSectionResponses = heroSectionRepository.getActivityHeroSectionDetails();

            if (activityHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No activity hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No activity hero section data found");
            }

            List<ActivityHeroSectionResponse> activityHeroSectionResponseList = activityHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (activityHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active activity hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No active activity hero section data found");
            }

            LOGGER.info("Fetched {} active activity hero section data successfully", activityHeroSectionResponseList.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityHeroSectionResponseList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active activity hero section data : {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity hero section data from database");
        } finally {
            LOGGER.info("End fetching activity hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<PackageScheduleHeroSectionResponse>> getPackageScheduleHeroSectionDetails(Long packageScheduleId) {
        LOGGER.info("Start fetching package schedule hero section data from repository");

        try {
            List<PackageScheduleHeroSectionResponse> packageScheduleHeroSectionResponses =
                    heroSectionRepository.getPackageScheduleHeroSectionDetails(packageScheduleId);

            if (packageScheduleHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No package schedule hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No package schedule hero section data found");
            }

            LOGGER.info("Fetched {} active package schedule hero section data successfully", packageScheduleHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    packageScheduleHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active package schedule hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch package schedule hero section data from database");
        } finally {
            LOGGER.info("End fetching package schedule hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<BookedTourHeroSectionResponse>> getBookedTourHeroSectionDetails(Long bookingId) {
        LOGGER.info("Start fetching booked tour hero section data from repository");

        try {
            List<BookedTourHeroSectionResponse> bookedTourHeroSectionResponses =
                    heroSectionRepository.getBookedTourHeroSectionDetails(bookingId);

            if (bookedTourHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No booked tour hero section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No booked tour hero section data found");
            }

            LOGGER.info("Fetched {} active booked tour hero section data successfully", bookedTourHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    bookedTourHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active booked tour hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch booked tour hero section data from database");
        } finally {
            LOGGER.info("End fetching booked tour hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<ActivityDetailsHeroSectionResponse>> getActivityHeroSectionDetailsByActivityId(Long activityId) {
        LOGGER.info("Start fetching activity hero section data by activity id : {} from repository", activityId);

        try {
            List<ActivityDetailsHeroSectionResponse> activityDetailsHeroSectionResponses = heroSectionRepository.getActivityHeroSectionDetailsByActivityId(activityId);

            if (activityDetailsHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No activity hero section data by activity id : {} found in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No activity hero section data by activity id : " + activityId);
            }

            List<ActivityDetailsHeroSectionResponse> activityDetailsHeroSectionResponseList = activityDetailsHeroSectionResponses.stream()
                    .filter(item -> CommonStatus.ACTIVE.toString().equalsIgnoreCase(item.getStatus()))
                    .toList();

            if (activityDetailsHeroSectionResponseList.isEmpty()) {
                LOGGER.warn("No active activity hero section data by activity id : {} found in database", activityId);
                throw new DataNotFoundErrorExceptionHandler("No active activity hero section data by activity id : " + activityId);
            }

            LOGGER.info("Fetched {} active activity hero section data by activity id : {} successfully", activityDetailsHeroSectionResponseList.size(), activityId);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    activityDetailsHeroSectionResponseList,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active activity hero section data by activity id : {} , {}", activityId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch activity hero section data by activity id : " + activityId);
        } finally {
            LOGGER.info("End fetching activity hero section data from by activity id : {} repository", activityId);
        }
    }

    @Override
    public CommonResponse<List<VehicleHeroSectionResponse>> getVehicleHeroSectionDetails() {
        LOGGER.info("Start fetching vehicle hero section data from repository");

        try {
            List<VehicleHeroSectionResponse> vehicleHeroSectionResponses =
                    heroSectionRepository.getVehicleHeroSectionDetails();

            if (vehicleHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No booked vehicle section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No vehicle hero section data found");
            }

            LOGGER.info("Fetched {} active vehicle hero section data successfully", vehicleHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    vehicleHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active vehicle hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch vehicle hero section data from database");
        } finally {
            LOGGER.info("End fetching vehicle hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<VehicleSpecificationHeroSectionResponse>> getVehicleSpecificationHeroSectionDetails() {
        LOGGER.info("Start fetching vehicle specification hero section data from repository");

        try {
            List<VehicleSpecificationHeroSectionResponse> vehicleSpecificationHeroSectionResponses =
                    heroSectionRepository.getVehicleSpecificationHeroSectionDetails();

            if (vehicleSpecificationHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No booked vehicle specification section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No vehicle specification hero section data found");
            }

            LOGGER.info("Fetched {} active vehicle specification hero section data successfully", vehicleSpecificationHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    vehicleSpecificationHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active vehicle specification hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch vehicle specification hero section data from database");
        } finally {
            LOGGER.info("End fetching vehicle specification hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<VehicleTypesHeroSectionResponse>> getVehicleTypesHeroSectionDetails() {
        LOGGER.info("Start fetching vehicle types hero section data from repository");

        try {
            List<VehicleTypesHeroSectionResponse> vehicleTypesHeroSectionResponses =
                    heroSectionRepository.getVehicleTypesHeroSectionDetails();

            if (vehicleTypesHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No booked vehicle types section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No vehicle types hero section data found");
            }

            LOGGER.info("Fetched {} active vehicle types hero section data successfully", vehicleTypesHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    vehicleTypesHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active vehicle types hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch vehicle types hero section data from database");
        } finally {
            LOGGER.info("End fetching vehicle types hero section data from repository");
        }
    }

    @Override
    public CommonResponse<List<SeasonsHeroSectionResponse>> getSeasonHeroSectionDetails() {
        LOGGER.info("Start fetching seasons hero section data from repository");

        try {
            List<SeasonsHeroSectionResponse> seasonsHeroSectionResponses =
                    heroSectionRepository.getSeasonHeroSectionDetails();

            if (seasonsHeroSectionResponses.isEmpty()) {
                LOGGER.warn("No booked seasons section data found in database");
                throw new DataNotFoundErrorExceptionHandler("No seasons hero section data found");
            }

            LOGGER.info("Fetched {} active seasons hero section data successfully", seasonsHeroSectionResponses.size());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonsHeroSectionResponses,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching active seasons hero section data: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch seasons hero section data from database");
        } finally {
            LOGGER.info("End fetching seasons hero section data from repository");
        }
    }

    @Override
    public CommonResponse<HeroSectionParamResponse> getHeroSectionDataWithParams(HeroSectionDataRequest heroSectionDataRequest) {
        LOGGER.info("Start fetching hero section data with params from repository");
        try {
            HeroSectionParamResponse heroSectionData = new HeroSectionParamResponse();

            List<HeroSectionBasicResponse> heroSectionBasicResponses = heroSectionRepository.getHeroSectionBasicResponseForParms(heroSectionDataRequest);
            Integer count = heroSectionRepository.getHeroSectionBasicResponseCountForParms(heroSectionDataRequest);
            heroSectionData.setCount(count);
            heroSectionData.setHeroSectionBasicResponses(heroSectionBasicResponses);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionData,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching hero section data with params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch hero section data with params from database");
        } finally {
            LOGGER.info("End fetching hero section data with params from repository");
        }
    }

    @Override
    public CommonResponse<HeroSectionDataForParamsResponse> getDataForRequestParams(HeroSectionTypeRequest heroSectionTypeRequest) {
        LOGGER.info("Start fetching hero section data for request params from repository");
        try {
            HeroSectionDataForParamsResponse heroSectionData = heroSectionRepository.getDataForRequestParams(heroSectionTypeRequest);

            if (heroSectionData == null) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        "No hero section data found for the given parameters",
                        new HeroSectionDataForParamsResponse(),
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionData,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching hero section data for request params: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch hero section data for request params from database");
        } finally {
            LOGGER.info("End fetching hero section data for request params from repository");
        }
    }

    @Override
    public CommonResponse<HeroSectionDetailsResponse> getHeroSectionDetailsById(HeroSectionDetailsDataRequest heroSectionDetailsDataRequest) {
        LOGGER.info("Start fetching hero section details by id from repository");
        try {
            HeroSectionDetailsResponse heroSectionDetails = heroSectionRepository.getHeroSectionDetailsById(heroSectionDetailsDataRequest);

            if (heroSectionDetails == null) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        "No hero section details found for the given id",
                        new HeroSectionDetailsResponse(),
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching hero section details by id: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch hero section details from database");
        } finally {
            LOGGER.info("End fetching hero section details from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> addHeroSection(HeroSectionInsertRequest heroSectionInsertRequest) {
        LOGGER.info("Start execute add hero section request.");

        try {
            heroSectionValidationService.validateHeroSectionInsertRequest(heroSectionInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long heroSectionId = heroSectionRepository.insertHeroSectionDetails(heroSectionInsertRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.HERO_SECTION_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Hero Section Created")
                    .message("A new hero section '" + heroSectionInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_HERO_SECTION_DETAILS + "/" + heroSectionId)
                    .actionText("View Hero Section")
                    .icon("Image")
                    .color("#8B5CF6")
                    .metadata(Map.of(
                            "heroSectionId", heroSectionId,
                            "heroSectionName", heroSectionInsertRequest.getName(),
                            "heroSectionType", heroSectionInsertRequest.getHeroSectionType(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.HERO_SECTION_CREATE.name())
                    .sourceModule(SourceModule.HERO_SECTION.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (heroSectionId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(
                        NotificationType.HERO_SECTION_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = heroSectionEmailHelperService.buildHeroSectionCreateSuccessfullBody(heroSectionInsertRequest, heroSectionId, loggedUser);
                String subject = heroSectionEmailHelperService.buildHeroSectionCreateSuccessfullSubject(heroSectionInsertRequest, heroSectionId, loggedUser);
                // emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert hero section request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert hero section request", vfe.getValidationFailedResponses());
        } catch (InsertFailedErrorExceptionHandler ife) {
            throw new InsertFailedErrorExceptionHandler(ife.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while adding hero section: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Something went wrong while adding hero section");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateHeroSection(HeroSectionUpdateRequest heroSectionUpdateRequest) {
        LOGGER.info("Start execute update hero section request.");
        try {
            heroSectionValidationService.validateHeroSectionUpdateRequest(heroSectionUpdateRequest);

            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            HeroSectionDetailsResponse previousHeroSection =
                    getHeroSectionDetailsById(
                            new HeroSectionDetailsDataRequest(heroSectionUpdateRequest.getHeroSectionType(), heroSectionUpdateRequest.getHeroSectionId())).getData();

            heroSectionRepository.updateBasicHeroSectionDetails(heroSectionUpdateRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.HERO_SECTION_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Hero Section Updated")
                    .message("The hero section '" + heroSectionUpdateRequest.getName() + "' has been updated.")
                    .actionUrl(VIEW_HERO_SECTION_DETAILS + "/" + heroSectionUpdateRequest.getHeroSectionId())
                    .actionText("View Hero Section")
                    .icon("Image")
                    .color("#8B5CF6")
                    .metadata(Map.of(
                            "heroSectionId", heroSectionUpdateRequest.getHeroSectionId(),
                            "heroSectionName", heroSectionUpdateRequest.getName(),
                            "heroSectionType", heroSectionUpdateRequest.getHeroSectionType(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.HERO_SECTION_UPDATE.name())
                    .sourceModule(SourceModule.HERO_SECTION.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            // Compare changes for email notification
            HeroSectionComparisonResult comparisonResult = compareHeroSectionUpdates(
                    heroSectionUpdateRequest,
                    previousHeroSection
            );

            // Send email notification to supervisors
            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(
                    NotificationType.HERO_SECTION_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = heroSectionEmailHelperService.buildHeroSectionUpdateSuccessfullSubject(loggedUser, heroSectionUpdateRequest.getHeroSectionId());
            String body = heroSectionEmailHelperService.buildHeroSectionUpdateSuccessfullBody(loggedUser, heroSectionUpdateRequest.getHeroSectionId(), comparisonResult);
            // emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update hero section request", heroSectionUpdateRequest.getHeroSectionId()),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the update hero section request", vfe.getValidationFailedResponses());
        } catch (UpdateFailedErrorExceptionHandler ufe) {
            throw new UpdateFailedErrorExceptionHandler(ufe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating hero section: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Something went wrong while updating hero section");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateHeroSection(IdWithTypeRequest idWithTypeRequest) {
        LOGGER.info("Start execute terminate hero section request.");
        try {
            heroSectionValidationService.validateIdWithTypeRequest(idWithTypeRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            HeroSectionDetailsResponse heroSectionData = getHeroSectionDetailsById(new HeroSectionDetailsDataRequest(idWithTypeRequest.getType(), idWithTypeRequest.getId())).getData();

            if (heroSectionData == null) {
                throw new DataNotFoundErrorExceptionHandler("Hero section not found with ID: " + idWithTypeRequest.getId());
            }

            heroSectionRepository.terminateHeroSection(idWithTypeRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            // Create notification for supervisors
            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.HERO_SECTION_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Hero Section Terminated")
                    .message("The hero section '" + heroSectionData.getName() + "' has been terminated.")
                    .actionUrl(VIEW_HERO_SECTION_DETAILS + "/" + heroSectionData.getId())
                    .actionText("View Hero Section")
                    .icon("Image")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "heroSectionId", heroSectionData.getId(),
                            "heroSectionName", heroSectionData.getName(),
                            "heroSectionType", idWithTypeRequest.getType(),
                            "status", heroSectionData.getStatusId(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.HERO_SECTION_TERMINATE.name())
                    .sourceModule(SourceModule.HERO_SECTION.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            // Send email notification to supervisors
            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(
                    NotificationType.HERO_SECTION_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = heroSectionEmailHelperService.buildHeroSectionTerminateSuccessfullSubject(loggedUser, heroSectionData);
            String body = heroSectionEmailHelperService.buildHeroSectionTerminateSuccessfullBody(loggedUser, heroSectionData, idWithTypeRequest.getType());

            // emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminate hero section request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the terminate hero section request", vfe.getValidationFailedResponses());
        } catch (TerminateFailedErrorExceptionHandler tfe) {
            throw new TerminateFailedErrorExceptionHandler(tfe.getMessage());
        } catch (UnAuthenticateErrorExceptionHandler uae) {
            throw new UnAuthenticateErrorExceptionHandler(uae.getMessage());
        } catch (DataNotFoundErrorExceptionHandler dnfe) {
            throw new DataNotFoundErrorExceptionHandler(dnfe.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating hero section: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Something went wrong while terminating hero section");
        }
    }

    @Override
    public CommonResponse<HeroSectionStatisticsResponse> getHeroSectionStatisctisByType(HeroSectionTypeRequest heroSectionTypeRequest) {
        LOGGER.info("Start fetching hero section statistics by type from repository");
        try {
            heroSectionValidationService.validateHeroSectionTypeRequest(heroSectionTypeRequest);

            HeroSectionStatisticsResponse heroSectionStatistics = new HeroSectionStatisticsResponse();

            HeroSectionStatisticsResponse.Summary summary = heroSectionRepository.getHeroSectionSummaryStatistics(heroSectionTypeRequest.getHeroSectionType());
            heroSectionStatistics.setSummary(summary);

            List<HeroSectionStatisticsResponse.StatusStatistics> statusStatistics =
                    heroSectionRepository.getHeroSectionStatusStatistics(heroSectionTypeRequest.getHeroSectionType())
                            .stream()
                            .filter(status -> List.of("ACTIVE", "INACTIVE", "TERMINATED")
                                    .contains(status.getStatus()))
                            .toList();

            heroSectionStatistics.setStatusStatistics(statusStatistics);

            List<HeroSectionStatisticsResponse.MonthlyStatistics> monthlyStatistics = heroSectionRepository.getHeroSectionMonthlyStatistics(heroSectionTypeRequest.getHeroSectionType());
            heroSectionStatistics.setMonthlyStatistics(monthlyStatistics);

            List<HeroSectionStatisticsResponse.ActivityStatistics> activityStatistics = heroSectionRepository.getHeroSectionActivityStatistics(heroSectionTypeRequest.getHeroSectionType());
            heroSectionStatistics.setActivityStatistics(activityStatistics);

            List<HeroSectionStatisticsResponse.TopEditorStatistics> topEditorStatistics = heroSectionRepository.getHeroSectionTopEditorStatistics(heroSectionTypeRequest.getHeroSectionType());
            heroSectionStatistics.setTopEditorStatistics(topEditorStatistics);


            if (heroSectionStatistics == null) {
                return new CommonResponse<>(
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                        CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                        "No statistics found for the given section type",
                        new HeroSectionStatisticsResponse(),
                        Instant.now());
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    heroSectionStatistics,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (ValidationFailedErrorExceptionHandler vfe) {
            throw new ValidationFailedErrorExceptionHandler("validation failed in the get hero section statistics request", vfe.getValidationFailedResponses());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching hero section statistics by type: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch hero section statistics from database");
        } finally {
            LOGGER.info("End fetching hero section statistics by type from repository");
        }
    }

    private HeroSectionComparisonResult compareHeroSectionUpdates(
            HeroSectionUpdateRequest heroSectionUpdateRequest,
            HeroSectionDetailsResponse previousHeroSection) {

        HeroSectionComparisonResult.HeroSectionComparisonResultBuilder resultBuilder =
                HeroSectionComparisonResult.builder();

        List<HeroSectionComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean hasChanges = false;

        // Compare name
        if (heroSectionUpdateRequest.getName() != null &&
                previousHeroSection.getName() != null &&
                !heroSectionUpdateRequest.getName().equals(previousHeroSection.getName())) {
            changes.add(String.format("Name changed from '%s' to '%s'",
                    previousHeroSection.getName(),
                    heroSectionUpdateRequest.getName()));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "name",
                    previousHeroSection.getName(),
                    heroSectionUpdateRequest.getName(),
                    "Name"));
            hasChanges = true;
        }

        // Compare imageUrl
        if (!Objects.equals(heroSectionUpdateRequest.getImageUrl(), previousHeroSection.getImageUrl())) {
            String oldUrl = previousHeroSection.getImageUrl() != null ?
                    previousHeroSection.getImageUrl() : "null";
            String newUrl = heroSectionUpdateRequest.getImageUrl() != null ?
                    heroSectionUpdateRequest.getImageUrl() : "null";
            changes.add(String.format("Image URL changed from '%s' to '%s'", oldUrl, newUrl));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "imageUrl",
                    previousHeroSection.getImageUrl(),
                    heroSectionUpdateRequest.getImageUrl(),
                    "Image URL"));
            hasChanges = true;

            if (heroSectionUpdateRequest.getImageUrl() != null &&
                    !heroSectionUpdateRequest.getImageUrl().startsWith("http")) {
                warnings.add("Warning: Image URL should start with http:// or https://");
            }
        }

        // Compare title
        if (heroSectionUpdateRequest.getTitle() != null &&
                previousHeroSection.getTitle() != null &&
                !heroSectionUpdateRequest.getTitle().equals(previousHeroSection.getTitle())) {
            changes.add(String.format("Title changed from '%s' to '%s'",
                    previousHeroSection.getTitle(),
                    heroSectionUpdateRequest.getTitle()));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "title",
                    previousHeroSection.getTitle(),
                    heroSectionUpdateRequest.getTitle(),
                    "Title"));
            hasChanges = true;
        }

        // Compare subtitle
        if (!Objects.equals(heroSectionUpdateRequest.getSubtitle(), previousHeroSection.getSubtitle())) {
            String oldSub = previousHeroSection.getSubtitle() != null ?
                    previousHeroSection.getSubtitle() : "null";
            String newSub = heroSectionUpdateRequest.getSubtitle() != null ?
                    heroSectionUpdateRequest.getSubtitle() : "null";
            changes.add(String.format("Subtitle changed from '%s' to '%s'", oldSub, newSub));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "subtitle",
                    previousHeroSection.getSubtitle(),
                    heroSectionUpdateRequest.getSubtitle(),
                    "Subtitle"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(heroSectionUpdateRequest.getDescription(), previousHeroSection.getDescription())) {
            String oldDesc = previousHeroSection.getDescription() != null ?
                    previousHeroSection.getDescription() : "null";
            String newDesc = heroSectionUpdateRequest.getDescription() != null ?
                    heroSectionUpdateRequest.getDescription() : "null";
            changes.add(String.format("Description changed from '%s' to '%s'", oldDesc, newDesc));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "description",
                    previousHeroSection.getDescription(),
                    heroSectionUpdateRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare primaryButtonText
        if (!Objects.equals(heroSectionUpdateRequest.getPrimaryButtonText(), previousHeroSection.getPrimaryButtonText())) {
            String oldText = previousHeroSection.getPrimaryButtonText() != null ?
                    previousHeroSection.getPrimaryButtonText() : "null";
            String newText = heroSectionUpdateRequest.getPrimaryButtonText() != null ?
                    heroSectionUpdateRequest.getPrimaryButtonText() : "null";
            changes.add(String.format("Primary Button Text changed from '%s' to '%s'", oldText, newText));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "primaryButtonText",
                    previousHeroSection.getPrimaryButtonText(),
                    heroSectionUpdateRequest.getPrimaryButtonText(),
                    "Primary Button Text"));
            hasChanges = true;
        }

        // Compare primaryButtonLink
        if (!Objects.equals(heroSectionUpdateRequest.getPrimaryButtonLink(), previousHeroSection.getPrimaryButtonLink())) {
            String oldLink = previousHeroSection.getPrimaryButtonLink() != null ?
                    previousHeroSection.getPrimaryButtonLink() : "null";
            String newLink = heroSectionUpdateRequest.getPrimaryButtonLink() != null ?
                    heroSectionUpdateRequest.getPrimaryButtonLink() : "null";
            changes.add(String.format("Primary Button Link changed from '%s' to '%s'", oldLink, newLink));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "primaryButtonLink",
                    previousHeroSection.getPrimaryButtonLink(),
                    heroSectionUpdateRequest.getPrimaryButtonLink(),
                    "Primary Button Link"));
            hasChanges = true;

            if (heroSectionUpdateRequest.getPrimaryButtonLink() != null &&
                    !heroSectionUpdateRequest.getPrimaryButtonLink().startsWith("/") &&
                    !heroSectionUpdateRequest.getPrimaryButtonLink().startsWith("http")) {
                warnings.add("Warning: Primary button link should start with '/' or 'http'");
            }
        }

        // Compare secondaryButtonText
        if (!Objects.equals(heroSectionUpdateRequest.getSecondaryButtonText(), previousHeroSection.getSecondaryButtonText())) {
            String oldText = previousHeroSection.getSecondaryButtonText() != null ?
                    previousHeroSection.getSecondaryButtonText() : "null";
            String newText = heroSectionUpdateRequest.getSecondaryButtonText() != null ?
                    heroSectionUpdateRequest.getSecondaryButtonText() : "null";
            changes.add(String.format("Secondary Button Text changed from '%s' to '%s'", oldText, newText));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "secondaryButtonText",
                    previousHeroSection.getSecondaryButtonText(),
                    heroSectionUpdateRequest.getSecondaryButtonText(),
                    "Secondary Button Text"));
            hasChanges = true;
        }

        // Compare secondaryButtonLink
        if (!Objects.equals(heroSectionUpdateRequest.getSecondaryButtonLink(), previousHeroSection.getSecondaryButtonLink())) {
            String oldLink = previousHeroSection.getSecondaryButtonLink() != null ?
                    previousHeroSection.getSecondaryButtonLink() : "null";
            String newLink = heroSectionUpdateRequest.getSecondaryButtonLink() != null ?
                    heroSectionUpdateRequest.getSecondaryButtonLink() : "null";
            changes.add(String.format("Secondary Button Link changed from '%s' to '%s'", oldLink, newLink));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "secondaryButtonLink",
                    previousHeroSection.getSecondaryButtonLink(),
                    heroSectionUpdateRequest.getSecondaryButtonLink(),
                    "Secondary Button Link"));
            hasChanges = true;

            if (heroSectionUpdateRequest.getSecondaryButtonLink() != null &&
                    !heroSectionUpdateRequest.getSecondaryButtonLink().startsWith("/") &&
                    !heroSectionUpdateRequest.getSecondaryButtonLink().startsWith("http")) {
                warnings.add("Warning: Secondary button link should start with '/' or 'http'");
            }
        }

        // Compare statusId
        Long oldStatusId = previousHeroSection.getStatusId();
        Long newStatusId = heroSectionUpdateRequest.getStatusId();
        String oldStatusName = previousHeroSection.getStatus();
        String newStatusName = getStatusNameById(newStatusId);

        if (oldStatusId != null && newStatusId != null && !oldStatusId.equals(newStatusId)) {
            changes.add(String.format("Status changed from '%s' (ID: %d) to '%s' (ID: %d)",
                    oldStatusName != null ? oldStatusName : "Unknown",
                    oldStatusId,
                    newStatusName != null ? newStatusName : "Unknown",
                    newStatusId));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "statusId",
                    oldStatusId,
                    newStatusId,
                    "Status"));
            hasChanges = true;

            // Status change warnings
            if (newStatusId == 0L || "INACTIVE".equals(newStatusName)) { // Assuming 0 = INACTIVE
                warnings.add("Warning: Hero section is being deactivated. It will not be displayed on the website.");
            } else if (newStatusId == 1L || "ACTIVE".equals(newStatusName)) { // Assuming 1 = ACTIVE
                warnings.add("Info: Hero section is being activated and will be displayed on the website.");
            }
        }

        // Compare order
        Integer oldOrder = previousHeroSection.getOrder();
        Integer newOrder = heroSectionUpdateRequest.getOrder();
        if (oldOrder != null && newOrder != null && !oldOrder.equals(newOrder)) {
            changes.add(String.format("Display Order changed from %d to %d", oldOrder, newOrder));
            fieldChanges.add(new HeroSectionComparisonResult.FieldChange(
                    "order",
                    oldOrder,
                    newOrder,
                    "Display Order"));
            hasChanges = true;

            if (newOrder < 0) {
                warnings.add("Warning: Display order is negative");
            }
        }

        // Additional validations
        if (heroSectionUpdateRequest.getName() != null &&
                heroSectionUpdateRequest.getName().trim().isEmpty()) {
            warnings.add("Warning: Name is empty");
        }

        if (heroSectionUpdateRequest.getTitle() != null &&
                heroSectionUpdateRequest.getTitle().length() > 100) {
            warnings.add("Warning: Title is very long (>100 characters)");
        }

        if (heroSectionUpdateRequest.getDescription() != null &&
                heroSectionUpdateRequest.getDescription().length() > 500) {
            warnings.add("Warning: Description is very long (>500 characters)");
        }

        // Check if both primary and secondary buttons have text but no links or vice versa
        if (heroSectionUpdateRequest.getPrimaryButtonText() != null &&
                !heroSectionUpdateRequest.getPrimaryButtonText().isEmpty() &&
                (heroSectionUpdateRequest.getPrimaryButtonLink() == null ||
                        heroSectionUpdateRequest.getPrimaryButtonLink().isEmpty())) {
            warnings.add("Warning: Primary button has text but no link");
        }

        if (heroSectionUpdateRequest.getSecondaryButtonText() != null &&
                !heroSectionUpdateRequest.getSecondaryButtonText().isEmpty() &&
                (heroSectionUpdateRequest.getSecondaryButtonLink() == null ||
                        heroSectionUpdateRequest.getSecondaryButtonLink().isEmpty())) {
            warnings.add("Warning: Secondary button has text but no link");
        }

        // Check if no changes were made
        if (!hasChanges) {
            changes.add("No changes detected in hero section");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .oldStatusId(oldStatusId)
                .oldStatusName(oldStatusName)
                .newStatusId(newStatusId)
                .newStatusName(newStatusName)
                .oldOrder(oldOrder)
                .newOrder(newOrder)
                .changedBy("System")
                .changeTimestamp(new Date().toString())
                .build();
    }

    // Helper method to get status name by ID
    private String getStatusNameById(Long statusId) {
        if (statusId == null) return null;
        switch (statusId.intValue()) {
            case 0:
                return "INACTIVE";
            case 1:
                return "ACTIVE";
            case 2:
                return "DRAFT";
            default:
                return "UNKNOWN";
        }
    }

}
