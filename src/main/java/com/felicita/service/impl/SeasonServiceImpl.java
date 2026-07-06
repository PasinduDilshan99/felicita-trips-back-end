package com.felicita.service.impl;

import com.felicita.email.SeasonEmailHelperService;
import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.enums.NotificationType;
import com.felicita.model.enums.Priority;
import com.felicita.model.enums.Privileges;
import com.felicita.model.enums.SourceModule;
import com.felicita.model.other.SeasonUpdateComparisonResult;
import com.felicita.model.request.CommonIdRequest;
import com.felicita.model.request.seasons.SeasonImageInsertRequest;
import com.felicita.model.request.seasons.SeasonImageUpdateRequest;
import com.felicita.model.request.seasons.SeasonInsertRequest;
import com.felicita.model.request.seasons.SeasonUpdateRequest;
import com.felicita.model.response.*;
import com.felicita.model.response.common.SeasonIdAndNameResponse;
import com.felicita.model.response.seasons.SeasonAllDetailsResponse;
import com.felicita.model.response.seasons.SeasonImageResponse;
import com.felicita.model.response.statistics.SeasonStatisticsResponse;
import com.felicita.repository.SeasonRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.SeasonService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.SeasonValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_SEASON_DETAILS;

@Service
public class SeasonServiceImpl implements SeasonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonServiceImpl.class);

    private final SeasonRepository seasonRepository;
    private final SeasonEmailHelperService seasonEmailHelperService;
    private final CommonService commonService;
    private final SeasonValidationService seasonValidationService;
    private final EmailService emailService;

    @Autowired
    public SeasonServiceImpl(SeasonRepository seasonRepository, SeasonEmailHelperService seasonEmailHelperService, CommonService commonService, SeasonValidationService seasonValidationService, EmailService emailService) {
        this.seasonRepository = seasonRepository;
        this.seasonEmailHelperService = seasonEmailHelperService;
        this.commonService = commonService;
        this.seasonValidationService = seasonValidationService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<List<SeasonDetailsResponse>> getSeasonDetailsBySeasonId(String seasonId) {
        LOGGER.info("Start fetching season details by season id : {} from repository", seasonId);
        try {
            List<SeasonDetailsResponse> seasonDetailsResponses = seasonRepository.getSeasonDetailsBySeasonId(seasonId);

            if (seasonDetailsResponses == null) {
                LOGGER.warn("No season details by season id : {} in database", seasonId);
                throw new DataNotFoundErrorExceptionHandler("No season details by season id : " + seasonId);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonDetailsResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching season details by season id : {} , {}", seasonId, e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch season details by season id : " + seasonId);
        } finally {
            LOGGER.info("End fetching season details by season id : {} from repository", seasonId);
        }
    }

    @Override
    public CommonResponse<List<SeasonBasicResponse>> getActiveSeasonDetails() {
        LOGGER.info("Start fetching season details from repository");
        try {
            List<SeasonBasicResponse> seasonBasicResponses = seasonRepository.getActiveSeasonDetails();

            if (seasonBasicResponses == null) {
                LOGGER.warn("No season details in database");
                throw new DataNotFoundErrorExceptionHandler("No season details");
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonBasicResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching season details {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch season details ");
        } finally {
            LOGGER.info("End fetching season details from repository");
        }
    }

    @Override
    public CommonResponse<SeasonStatisticsResponse> getSeasonsStatistics() {
        LOGGER.info("Start fetching seasons statistics from repository");
        try {
            SeasonStatisticsResponse seasonStatistics = new SeasonStatisticsResponse();
            seasonStatistics.setSeasonActivityCounts(seasonRepository.getSeasonActivityCount());
            seasonStatistics.setSeasonTourCounts(seasonRepository.getSeasonTourCount());
            seasonStatistics.setSeasonPopularities(seasonRepository.getSeasonPopularity());
            seasonStatistics.setPeakSeasonDistributions(seasonRepository.getPeakSeasonDistribution());
            seasonStatistics.setSeasonWeatherOverviews(seasonRepository.getSeasonWeatherOverview());
            seasonStatistics.setSummary(seasonRepository.getSeasonSummary());

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonStatistics,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching seasons statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch seasons statistics from database");
        } finally {
            LOGGER.info("End fetching seasons statistics from repository");
        }
    }

    @Override
    public CommonResponse<SeasonAllDetailsResponse> getSeasonAllDetailsById(CommonIdRequest commonIdRequest) {
        LOGGER.info("Start fetching season all details by id from repository");
        try {
            SeasonAllDetailsResponse seasonAllDetails = seasonRepository.getSeasonAllDetailsById(commonIdRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonAllDetails,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching season all details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch season all details from database");
        } finally {
            LOGGER.info("End fetching season all details from repository");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminateSeason(CommonIdRequest seasonTerminateIdRequest) {
        LOGGER.info("Start terminating season from repository");
        try {
            seasonValidationService.validateCommonIdRequest(seasonTerminateIdRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            SeasonAllDetailsResponse seasonResponse = getSeasonAllDetailsById(seasonTerminateIdRequest).getData();

            seasonRepository.terminateSeason(seasonTerminateIdRequest);
            seasonRepository.terminateSeasonImages(seasonTerminateIdRequest);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.SEASON_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Season Terminated")
                    .message("The season '" + seasonResponse.getName() + "' has been terminated.")
                    .actionUrl(VIEW_SEASON_DETAILS + "/" + seasonResponse.getId())
                    .actionText("View Season")
                    .icon("CalendarX")
                    .color("#EF4444")
                    .metadata(Map.of(
                            "seasonId", seasonResponse.getId(),
                            "seasonName", seasonResponse.getName(),
                            "status", seasonResponse.getStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.SEASON_TERMINATE.name())
                    .sourceModule(SourceModule.SEASON.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.SEASON_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = seasonEmailHelperService.buildSeasonTerminateSuccessfullSubject(loggedUser, seasonResponse);
            String body = seasonEmailHelperService.buildSeasonTerminateSuccessfullBody(loggedUser, seasonResponse);

            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse(""),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating season: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate season in database");
        } finally {
            LOGGER.info("End terminating season from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> insertSeasons(SeasonInsertRequest seasonInsertRequest) {
        LOGGER.info("Start inserting season from repository");
        try {

            SeasonValidationService.validateSeasonInsertRequest(seasonInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long seasonId = seasonRepository.insertSeasonBasicDetails(seasonInsertRequest, userId);
            seasonRepository.insertSeasonImages(seasonId, seasonInsertRequest.getImageInsertRequests(), userId);
            seasonRepository.updateActivitiesSeasonIds(seasonId, seasonInsertRequest.getInsertActivitiesIds(), userId);
            seasonRepository.updateToursSeasonIds(seasonId, seasonInsertRequest.getInsertTourIds(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.SEASON_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Season Created")
                    .message("A new season '" + seasonInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_SEASON_DETAILS + "/" + seasonId)
                    .actionText("View Season")
                    .icon("CalendarPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "seasonId", seasonId,
                            "seasonName", seasonInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.SEASON_CREATE.name())
                    .sourceModule(SourceModule.SEASON.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (seasonId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.SEASON_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = seasonEmailHelperService.buildSeasonCreateSuccessfullBody(seasonInsertRequest, seasonId, loggedUser);
                String subject = seasonEmailHelperService.buildSeasonCreateSuccessfullSubject(seasonInsertRequest, seasonId, loggedUser);
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
            LOGGER.error("Error occurred while inserting season: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to insert season in database");
        } finally {
            LOGGER.info("End inserting season from repository");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateSeasons(SeasonUpdateRequest seasonUpdateRequest) {
        LOGGER.info("Start updating season from repository");
        try {
            seasonValidationService.validateSeasonUpdateRequest(seasonUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            SeasonAllDetailsResponse previousSeasonResponse = getSeasonAllDetailsById(new CommonIdRequest(seasonUpdateRequest.getId())).getData();

            seasonRepository.updateSeasonBasicDetails(seasonUpdateRequest);

            seasonRepository.removeSeasonImages(seasonUpdateRequest.getId(), seasonUpdateRequest.getImageRemoveRequests(), userId);
            seasonRepository.insertSeasonImages(seasonUpdateRequest.getId(), seasonUpdateRequest.getImageInsertRequests(), userId);
            seasonRepository.updateSeasonImages(seasonUpdateRequest.getId(), seasonUpdateRequest.getImageUpdateRequests(), userId);

            seasonRepository.updateActivitiesSeasonIds(seasonUpdateRequest.getId(),seasonUpdateRequest.getInsertActivitiesIds(), userId);
            seasonRepository.removeActivitiesSeasonIds(seasonUpdateRequest.getId(),seasonUpdateRequest.getRemoveActivitiesIds(), userId);

            seasonRepository.updateToursSeasonIds(seasonUpdateRequest.getId(), seasonUpdateRequest.getInsertTourIds(), userId);
            seasonRepository.removeToursSeasonIds(seasonUpdateRequest.getId(), seasonUpdateRequest.getRemoveTourIds(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);
            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.SEASON_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Season Updated")
                    .message("The season '" + previousSeasonResponse.getName() + "' has been updated.")
                    .actionUrl(VIEW_SEASON_DETAILS + "/" + previousSeasonResponse.getId())
                    .actionText("View Season")
                    .icon("CalendarEdit")
                    .color("#3B82F6")
                    .metadata(Map.of(
                            "seasonId", previousSeasonResponse.getId(),
                            "seasonName", previousSeasonResponse.getName(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.SEASON_UPDATE.name())
                    .sourceModule(SourceModule.SEASON.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);
            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            SeasonUpdateComparisonResult comparisonResult = compareSeasonUpdates(
                    seasonUpdateRequest,
                    previousSeasonResponse
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.SEASON_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = seasonEmailHelperService.buildSeasonUpdateSuccessfullSubject(loggedUser, seasonUpdateRequest.getId());
            String body = seasonEmailHelperService.buildSeasonUpdateSuccessfullBody(loggedUser, seasonUpdateRequest.getId(), comparisonResult);
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
            LOGGER.error("Error occurred while updating season: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update season in database");
        } finally {
            LOGGER.info("End updating season from repository");
        }
    }


    @Override
    public CommonResponse<List<SeasonIdAndNameResponse>> getSeasonsIdsAndSeasonsNames() {
        LOGGER.info("Start fetching seasons ids and names from repository");
        try {
            List<SeasonIdAndNameResponse> seasonsIdsAndNames = commonService.getSeasonIdAndNameResponses();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    seasonsIdsAndNames,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching seasons ids and names: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch seasons ids and names from database");
        } finally {
            LOGGER.info("End fetching seasons ids and names from repository");
        }
    }

    private SeasonUpdateComparisonResult compareSeasonUpdates(
            SeasonUpdateRequest seasonUpdateRequest,
            SeasonAllDetailsResponse previousSeasonResponse) {

        SeasonUpdateComparisonResult.SeasonUpdateComparisonResultBuilder resultBuilder =
                SeasonUpdateComparisonResult.builder();

        List<SeasonUpdateComparisonResult.FieldChange> fieldChanges = new ArrayList<>();
        List<String> changes = new ArrayList<>();
        boolean hasChanges = false;
        List<String> warnings = new ArrayList<>();

        boolean isMonthRangeValid = true;
        Integer monthsSpan = null;
        boolean isTemperatureValid = true;

        // Compare name
        if (seasonUpdateRequest.getName() != null &&
                previousSeasonResponse.getName() != null &&
                !seasonUpdateRequest.getName().equals(previousSeasonResponse.getName())) {
            changes.add(String.format("Season Name changed from '%s' to '%s'",
                    previousSeasonResponse.getName(),
                    seasonUpdateRequest.getName()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "name",
                    previousSeasonResponse.getName(),
                    seasonUpdateRequest.getName(),
                    "Season Name"));
            hasChanges = true;
        }

        // Compare standardName
        if (seasonUpdateRequest.getStandardName() != null &&
                previousSeasonResponse.getStandardName() != null &&
                !seasonUpdateRequest.getStandardName().equals(previousSeasonResponse.getStandardName())) {
            changes.add(String.format("Standard Name changed from '%s' to '%s'",
                    previousSeasonResponse.getStandardName(),
                    seasonUpdateRequest.getStandardName()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "standardName",
                    previousSeasonResponse.getStandardName(),
                    seasonUpdateRequest.getStandardName(),
                    "Standard Name"));
            hasChanges = true;
        }

        // Compare localName
        if (!Objects.equals(seasonUpdateRequest.getLocalName(), previousSeasonResponse.getLocalName())) {
            String oldName = previousSeasonResponse.getLocalName() != null ?
                    previousSeasonResponse.getLocalName() : "null";
            String newName = seasonUpdateRequest.getLocalName() != null ?
                    seasonUpdateRequest.getLocalName() : "null";
            changes.add(String.format("Local Name changed from '%s' to '%s'", oldName, newName));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "localName",
                    previousSeasonResponse.getLocalName(),
                    seasonUpdateRequest.getLocalName(),
                    "Local Name"));
            hasChanges = true;
        }

        // Compare startMonth
        if (seasonUpdateRequest.getStartMonth() != null &&
                previousSeasonResponse.getStartMonth() != null &&
                !seasonUpdateRequest.getStartMonth().equals(previousSeasonResponse.getStartMonth())) {
            changes.add(String.format("Start Month changed from %d to %d",
                    previousSeasonResponse.getStartMonth(),
                    seasonUpdateRequest.getStartMonth()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "startMonth",
                    previousSeasonResponse.getStartMonth(),
                    seasonUpdateRequest.getStartMonth(),
                    "Start Month"));
            hasChanges = true;
        }

        // Compare endMonth
        if (seasonUpdateRequest.getEndMonth() != null &&
                previousSeasonResponse.getEndMonth() != null &&
                !seasonUpdateRequest.getEndMonth().equals(previousSeasonResponse.getEndMonth())) {
            changes.add(String.format("End Month changed from %d to %d",
                    previousSeasonResponse.getEndMonth(),
                    seasonUpdateRequest.getEndMonth()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "endMonth",
                    previousSeasonResponse.getEndMonth(),
                    seasonUpdateRequest.getEndMonth(),
                    "End Month"));
            hasChanges = true;
        }

        // Validate month range
        if (seasonUpdateRequest.getStartMonth() != null && seasonUpdateRequest.getEndMonth() != null) {
            Integer startMonth = seasonUpdateRequest.getStartMonth();
            Integer endMonth = seasonUpdateRequest.getEndMonth();

            if (startMonth < 1 || startMonth > 12) {
                isMonthRangeValid = false;
                warnings.add("Error: Start month must be between 1 and 12");
            }

            if (endMonth < 1 || endMonth > 12) {
                isMonthRangeValid = false;
                warnings.add("Error: End month must be between 1 and 12");
            }

            if (startMonth <= endMonth) {
                monthsSpan = endMonth - startMonth + 1;
            } else {
                // Wrap around year (e.g., November to February)
                monthsSpan = (12 - startMonth + 1) + endMonth;
            }

            if (monthsSpan > 6) {
                warnings.add(String.format("Note: Season spans %d months (long season)", monthsSpan));
            } else if (monthsSpan < 1) {
                isMonthRangeValid = false;
                warnings.add("Error: Invalid month range");
            }
        }

        // Compare monsoonType
        if (seasonUpdateRequest.getMonsoonType() != null &&
                previousSeasonResponse.getMonsoonType() != null &&
                !seasonUpdateRequest.getMonsoonType().equals(previousSeasonResponse.getMonsoonType())) {
            changes.add(String.format("Monsoon Type changed from '%s' to '%s'",
                    previousSeasonResponse.getMonsoonType(),
                    seasonUpdateRequest.getMonsoonType()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "monsoonType",
                    previousSeasonResponse.getMonsoonType(),
                    seasonUpdateRequest.getMonsoonType(),
                    "Monsoon Type"));
            hasChanges = true;
        }

        // Compare weatherSummary
        if (!Objects.equals(seasonUpdateRequest.getWeatherSummary(), previousSeasonResponse.getWeatherSummary())) {
            String oldSummary = previousSeasonResponse.getWeatherSummary() != null ?
                    previousSeasonResponse.getWeatherSummary() : "null";
            String newSummary = seasonUpdateRequest.getWeatherSummary() != null ?
                    seasonUpdateRequest.getWeatherSummary() : "null";
            changes.add(String.format("Weather Summary changed from '%s' to '%s'", oldSummary, newSummary));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "weatherSummary",
                    previousSeasonResponse.getWeatherSummary(),
                    seasonUpdateRequest.getWeatherSummary(),
                    "Weather Summary"));
            hasChanges = true;
        }

        // Compare temperatureMin
        if (seasonUpdateRequest.getTemperatureMin() != null &&
                previousSeasonResponse.getTemperatureMin() != null &&
                !seasonUpdateRequest.getTemperatureMin().equals(previousSeasonResponse.getTemperatureMin())) {
            changes.add(String.format("Minimum Temperature changed from %d°C to %d°C",
                    previousSeasonResponse.getTemperatureMin(),
                    seasonUpdateRequest.getTemperatureMin()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "temperatureMin",
                    previousSeasonResponse.getTemperatureMin(),
                    seasonUpdateRequest.getTemperatureMin(),
                    "Minimum Temperature (°C)"));
            hasChanges = true;
        }

        // Compare temperatureMax
        if (seasonUpdateRequest.getTemperatureMax() != null &&
                previousSeasonResponse.getTemperatureMax() != null &&
                !seasonUpdateRequest.getTemperatureMax().equals(previousSeasonResponse.getTemperatureMax())) {
            changes.add(String.format("Maximum Temperature changed from %d°C to %d°C",
                    previousSeasonResponse.getTemperatureMax(),
                    seasonUpdateRequest.getTemperatureMax()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "temperatureMax",
                    previousSeasonResponse.getTemperatureMax(),
                    seasonUpdateRequest.getTemperatureMax(),
                    "Maximum Temperature (°C)"));
            hasChanges = true;
        }

        // Validate temperature range
        if (seasonUpdateRequest.getTemperatureMin() != null && seasonUpdateRequest.getTemperatureMax() != null) {
            Integer tempMin = seasonUpdateRequest.getTemperatureMin();
            Integer tempMax = seasonUpdateRequest.getTemperatureMax();

            if (tempMax < tempMin) {
                isTemperatureValid = false;
                warnings.add("Error: Maximum temperature is less than minimum temperature!");
            } else if (tempMax - tempMin > 20) {
                warnings.add(String.format("Warning: Large temperature range (%d°C difference)", tempMax - tempMin));
            }

            if (tempMin < -20 || tempMax > 50) {
                warnings.add("Warning: Extreme temperature values detected");
            }
        }

        // Compare rainfallPattern
        if (!Objects.equals(seasonUpdateRequest.getRainfallPattern(), previousSeasonResponse.getRainfallPattern())) {
            String oldPattern = previousSeasonResponse.getRainfallPattern() != null ?
                    previousSeasonResponse.getRainfallPattern() : "null";
            String newPattern = seasonUpdateRequest.getRainfallPattern() != null ?
                    seasonUpdateRequest.getRainfallPattern() : "null";
            changes.add(String.format("Rainfall Pattern changed from '%s' to '%s'", oldPattern, newPattern));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "rainfallPattern",
                    previousSeasonResponse.getRainfallPattern(),
                    seasonUpdateRequest.getRainfallPattern(),
                    "Rainfall Pattern"));
            hasChanges = true;
        }

        // Compare isPeak
        if (seasonUpdateRequest.getIsPeak() != null &&
                previousSeasonResponse.getIsPeak() != null &&
                !seasonUpdateRequest.getIsPeak().equals(previousSeasonResponse.getIsPeak())) {
            changes.add(String.format("Peak Season changed from %s to %s",
                    previousSeasonResponse.getIsPeak() ? "Yes" : "No",
                    seasonUpdateRequest.getIsPeak() ? "Yes" : "No"));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "isPeak",
                    previousSeasonResponse.getIsPeak(),
                    seasonUpdateRequest.getIsPeak(),
                    "Peak Season"));
            hasChanges = true;

            if (seasonUpdateRequest.getIsPeak()) {
                warnings.add("Note: Marking as peak season may affect pricing and availability");
            }
        }

        // Compare displayOrder
        if (seasonUpdateRequest.getDisplayOrder() != null &&
                previousSeasonResponse.getDisplayOrder() != null &&
                !seasonUpdateRequest.getDisplayOrder().equals(previousSeasonResponse.getDisplayOrder())) {
            changes.add(String.format("Display Order changed from %d to %d",
                    previousSeasonResponse.getDisplayOrder(),
                    seasonUpdateRequest.getDisplayOrder()));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "displayOrder",
                    previousSeasonResponse.getDisplayOrder(),
                    seasonUpdateRequest.getDisplayOrder(),
                    "Display Order"));
            hasChanges = true;
        }

        // Compare description
        if (!Objects.equals(seasonUpdateRequest.getDescription(), previousSeasonResponse.getDescription())) {
            String oldDesc = previousSeasonResponse.getDescription() != null ?
                    previousSeasonResponse.getDescription() : "null";
            String newDesc = seasonUpdateRequest.getDescription() != null ?
                    seasonUpdateRequest.getDescription() : "null";
            changes.add(String.format("Description changed from '%s' to '%s'", oldDesc, newDesc));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "description",
                    previousSeasonResponse.getDescription(),
                    seasonUpdateRequest.getDescription(),
                    "Description"));
            hasChanges = true;
        }

        // Compare status
        Integer oldStatus = previousSeasonResponse.getStatus();
        Integer newStatus = 1;
        if (oldStatus != null && newStatus != null && !oldStatus.equals(newStatus)) {
            String oldStatusText = getStatusText(oldStatus);
            String newStatusText = getStatusText(newStatus);
            changes.add(String.format("Status changed from '%s' to '%s'", oldStatusText, newStatusText));
            fieldChanges.add(new SeasonUpdateComparisonResult.FieldChange(
                    "status",
                    oldStatus,
                    newStatus,
                    "Status"));
            hasChanges = true;

            // Status change warnings
            if (newStatus == 0) { // Assuming 0 = INACTIVE
                warnings.add("Warning: Season is being deactivated");
            } else if (newStatus == 1 && oldStatus == 0) { // 1 = ACTIVE
                warnings.add("Info: Reactivating this season will make it available for selection");
            } else if (newStatus == 2) { // 2 = DRAFT
                warnings.add("Note: Season is in DRAFT mode");
            }
        }

        // Handle activities to add
        List<Long> activitiesToAdd = new ArrayList<>();
        if (seasonUpdateRequest.getInsertActivitiesIds() != null &&
                !seasonUpdateRequest.getInsertActivitiesIds().isEmpty()) {
            activitiesToAdd.addAll(seasonUpdateRequest.getInsertActivitiesIds());
            changes.add(String.format("Activities to add: %s", seasonUpdateRequest.getInsertActivitiesIds()));
            hasChanges = true;
        }

        // Handle activities to remove
        List<Long> activitiesToRemove = new ArrayList<>();
        if (seasonUpdateRequest.getRemoveActivitiesIds() != null &&
                !seasonUpdateRequest.getRemoveActivitiesIds().isEmpty()) {
            activitiesToRemove.addAll(seasonUpdateRequest.getRemoveActivitiesIds());
            changes.add(String.format("Activities to remove: %s", seasonUpdateRequest.getRemoveActivitiesIds()));
            hasChanges = true;
        }

        // Validate activity changes
        if (!activitiesToRemove.isEmpty() && !activitiesToAdd.isEmpty()) {
            List<Long> conflicts = activitiesToRemove.stream()
                    .filter(activitiesToAdd::contains)
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                warnings.add(String.format("Warning: Activities %s are both being added and removed!", conflicts));
            }
        }

        // Handle tours to add
        List<Long> toursToAdd = new ArrayList<>();
        if (seasonUpdateRequest.getInsertTourIds() != null &&
                !seasonUpdateRequest.getInsertTourIds().isEmpty()) {
            toursToAdd.addAll(seasonUpdateRequest.getInsertTourIds());
            changes.add(String.format("Tours to add: %s", seasonUpdateRequest.getInsertTourIds()));
            hasChanges = true;
        }

        // Handle tours to remove
        List<Long> toursToRemove = new ArrayList<>();
        if (seasonUpdateRequest.getRemoveTourIds() != null &&
                !seasonUpdateRequest.getRemoveTourIds().isEmpty()) {
            toursToRemove.addAll(seasonUpdateRequest.getRemoveTourIds());
            changes.add(String.format("Tours to remove: %s", seasonUpdateRequest.getRemoveTourIds()));
            hasChanges = true;
        }

        // Validate tour changes
        if (!toursToRemove.isEmpty() && !toursToAdd.isEmpty()) {
            List<Long> conflicts = toursToRemove.stream()
                    .filter(toursToAdd::contains)
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                warnings.add(String.format("Warning: Tours %s are both being added and removed!", conflicts));
            }
        }

        // Handle images to add
        List<SeasonUpdateComparisonResult.ImageChange> imagesToAdd = new ArrayList<>();
        if (seasonUpdateRequest.getImageInsertRequests() != null &&
                !seasonUpdateRequest.getImageInsertRequests().isEmpty()) {
            for (SeasonImageInsertRequest imageRequest : seasonUpdateRequest.getImageInsertRequests()) {
                SeasonUpdateComparisonResult.ImageChange imageChange =
                        SeasonUpdateComparisonResult.ImageChange.builder()
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
        if (seasonUpdateRequest.getImageRemoveRequests() != null &&
                !seasonUpdateRequest.getImageRemoveRequests().isEmpty()) {
            imagesToRemove.addAll(seasonUpdateRequest.getImageRemoveRequests());
            changes.add(String.format("Images to remove IDs: %s", seasonUpdateRequest.getImageRemoveRequests()));
            hasChanges = true;
        }

        // Handle images to update
        List<SeasonUpdateComparisonResult.ImageUpdateChange> imagesToUpdate = new ArrayList<>();
        if (seasonUpdateRequest.getImageUpdateRequests() != null &&
                !seasonUpdateRequest.getImageUpdateRequests().isEmpty()) {

            // Find existing images in previous response for comparison
            Map<Long, SeasonImageResponse> existingImagesMap = new HashMap<>();
            if (previousSeasonResponse.getSeasonImages() != null) {
                existingImagesMap = previousSeasonResponse.getSeasonImages().stream()
                        .collect(Collectors.toMap(
                                SeasonImageResponse::getId,
                                image -> image
                        ));
            }

            for (SeasonImageUpdateRequest updateRequest : seasonUpdateRequest.getImageUpdateRequests()) {
                SeasonImageResponse existingImage = existingImagesMap.get(updateRequest.getId().longValue());

                if (existingImage != null) {
                    boolean hasImageChanges = false;
                    String oldName = existingImage.getName();
                    String newName = updateRequest.getName();
                    String oldDescription = existingImage.getDescription();
                    String newDescription = updateRequest.getDescription();
                    String oldImageUrl = existingImage.getImageUrl();
                    String newImageUrl = updateRequest.getImageUrl();
                    String oldImgStatus = existingImage.getStatus();
                    String newImgStatus = updateRequest.getStatus();

                    if (!Objects.equals(oldName, newName) && newName != null) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d name changed from '%s' to '%s'",
                                updateRequest.getId(), oldName, newName));
                    }

                    if (!Objects.equals(oldDescription, newDescription) && newDescription != null) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d description changed", updateRequest.getId()));
                    }

                    if (!Objects.equals(oldImageUrl, newImageUrl) && newImageUrl != null) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d URL changed", updateRequest.getId()));
                    }

                    if (!Objects.equals(oldImgStatus, newImgStatus) && newImgStatus != null) {
                        hasImageChanges = true;
                        changes.add(String.format("Image ID %d status changed from %d to %d",
                                updateRequest.getId(), oldImgStatus, newImgStatus));
                    }

                    if (hasImageChanges) {
                        SeasonUpdateComparisonResult.ImageUpdateChange imageUpdateChange =
                                SeasonUpdateComparisonResult.ImageUpdateChange.builder()
                                        .imageId(updateRequest.getId().longValue())
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
                    warnings.add(String.format("Warning: Image with ID %d not found for update", updateRequest.getId()));
                }
            }
        }

        // Additional validations and warnings
        if (seasonUpdateRequest.getStartMonth() != null && seasonUpdateRequest.getEndMonth() != null) {
            if (seasonUpdateRequest.getStartMonth().equals(seasonUpdateRequest.getEndMonth())) {
                warnings.add("Note: Season starts and ends in the same month");
            }
        }

        if (seasonUpdateRequest.getName() != null && seasonUpdateRequest.getName().length() > 100) {
            warnings.add("Warning: Season name is very long (>100 characters)");
        }

        if (seasonUpdateRequest.getDisplayOrder() != null && seasonUpdateRequest.getDisplayOrder() < 0) {
            warnings.add("Warning: Display order is negative");
        }

        // Check if any changes were made
        boolean hasNoUpdates = (seasonUpdateRequest.getName() == null ||
                seasonUpdateRequest.getName().equals(previousSeasonResponse.getName())) &&
                (seasonUpdateRequest.getStatus() == null ||
                        seasonUpdateRequest.getStatus().equals(previousSeasonResponse.getStatus())) &&
                (seasonUpdateRequest.getImageInsertRequests() == null || seasonUpdateRequest.getImageInsertRequests().isEmpty()) &&
                (seasonUpdateRequest.getImageUpdateRequests() == null || seasonUpdateRequest.getImageUpdateRequests().isEmpty()) &&
                (seasonUpdateRequest.getImageRemoveRequests() == null || seasonUpdateRequest.getImageRemoveRequests().isEmpty()) &&
                (seasonUpdateRequest.getInsertActivitiesIds() == null || seasonUpdateRequest.getInsertActivitiesIds().isEmpty()) &&
                (seasonUpdateRequest.getRemoveActivitiesIds() == null || seasonUpdateRequest.getRemoveActivitiesIds().isEmpty()) &&
                (seasonUpdateRequest.getInsertTourIds() == null || seasonUpdateRequest.getInsertTourIds().isEmpty()) &&
                (seasonUpdateRequest.getRemoveTourIds() == null || seasonUpdateRequest.getRemoveTourIds().isEmpty());

        if (hasNoUpdates && !hasChanges) {
            changes.add("No changes detected in season");
        }

        // Build the result
        return resultBuilder
                .fieldChanges(fieldChanges)
                .changes(changes)
                .hasChanges(hasChanges)
                .warnings(warnings)
                .isMonthRangeValid(isMonthRangeValid)
                .monthsSpan(monthsSpan)
                .isTemperatureValid(isTemperatureValid)
                .activitiesToAdd(activitiesToAdd)
                .activitiesToRemove(activitiesToRemove)
                .toursToAdd(toursToAdd)
                .toursToRemove(toursToRemove)
                .imagesToAdd(imagesToAdd)
                .imagesToRemove(imagesToRemove)
                .imagesToUpdate(imagesToUpdate)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy("System") // Since loggedUser is not passed, using default
                .changedByUserId(null)
                .changeTimestamp(new Date().toString())
                .build();
    }

    // Helper method to get status text
    private String getStatusText(Integer status) {
        if (status == null) return "Unknown";
        switch (status) {
            case 0: return "INACTIVE";
            case 1: return "ACTIVE";
            case 2: return "DRAFT";
            default: return "UNKNOWN";
        }
    }
}
