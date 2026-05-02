package com.felicita.service.impl;

import com.felicita.email.PrivilegeEmailHelperService;
import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.exception.ValidationFailedErrorExceptionHandler;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.enums.NotificationType;
import com.felicita.model.enums.Priority;
import com.felicita.model.enums.Privileges;
import com.felicita.model.enums.SourceModule;
import com.felicita.model.other.DestinationUpdateComparisonResult;
import com.felicita.model.other.PrivilegeUpdateComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.repository.PrivilegeRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.PrivilegeService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.PrivilegeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_DESTINATION_DETAILS;
import static com.felicita.util.FrontEndUrls.VIEW_PRIVILEGE_DETAILS;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeEmailHelperService privilegeEmailHelperService;
    private final CommonService commonService;
    private final PrivilegeValidationService privilegeValidationService;
    private final EmailService emailService;

    @Autowired
    public PrivilegeServiceImpl(PrivilegeRepository privilegeRepository, PrivilegeEmailHelperService privilegeEmailHelperService, CommonService commonService, PrivilegeValidationService privilegeValidationService, EmailService emailService) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeEmailHelperService = privilegeEmailHelperService;
        this.commonService = commonService;
        this.privilegeValidationService = privilegeValidationService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<PrivilageParamResponse> getAllPrivileges(PrivilegeDataParamRequest privilegeDataParamRequest) {
        LOGGER.info("Start fetching all privileges from repository");
        try {
            PrivilageParamResponse privilageParamResponse = privilegeRepository.getAllPrivileges(privilegeDataParamRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    privilageParamResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all privileges: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all privileges from database");
        } finally {
            LOGGER.info("End fetching all privileges from repository");
        }
    }

    @Override
    public CommonResponse<List<PrivilegeNameAndIdResponse>> getAllPrivilegesNamesAndIds() {
        LOGGER.info("Start fetching privilege names and ids from repository");
        try {
            List<PrivilegeNameAndIdResponse> privilegeNameAndIdResponseList = privilegeRepository.getAllPrivilegesNamesAndIds();
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    privilegeNameAndIdResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching privilege names and ids: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch privilege names and ids from database");
        } finally {
            LOGGER.info("End fetching privilege names and ids from repository");
        }
    }

    @Override
    public CommonResponse<PrivilegeDetailsResponse> getPrivilegeDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest) {
        LOGGER.info("Start fetching privilege details by id from repository");
        try {
            PrivilegeDetailsResponse privilegeDetailsResponse = privilegeRepository.getPrivilegeDetailsById(privilegeDetailsRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    privilegeDetailsResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching privilege details by id: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch privilege details from database");
        } finally {
            LOGGER.info("End fetching privilege details by id from repository");
        }
    }

    @Override
    public CommonResponse<PrivilegeResponse> getPrivilegeBasicDetailsById(PrivilegeDetailsRequest privilegeDetailsRequest) {
        LOGGER.info("Start fetching privilege basic details by id from repository");
        try {
            PrivilegeResponse privilegeResponse = privilegeRepository.getPrivilegeBasicDetailsById(privilegeDetailsRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    privilegeResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching privilege basic details by id: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch privilege basic details from database");
        } finally {
            LOGGER.info("End fetching privilege basic details by id from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createPrivilege(PrivilegeInsertRequest privilegeInsertRequest) {
        LOGGER.info("Start creating privilege");
        try {
            privilegeValidationService.validatePrivilegeInsertRequest(privilegeInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long privilegeId = privilegeRepository.createPrivilege(privilegeInsertRequest, userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);
            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PRIVILEGE_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Privilege Created")
                    .message("A new privilege '" + privilegeInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_PRIVILEGE_DETAILS + "/" + privilegeId)
                    .actionText("View Privilege")
                    .icon("Shield")
                    .color("#8B5CF6")
                    .metadata(Map.of(
                            "privilegeId", privilegeId,
                            "privilegeName", privilegeInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PRIVILEGE_CREATE.name())
                    .sourceModule(SourceModule.PRIVILEGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (privilegeId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PRIVILEGE_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = privilegeEmailHelperService.buildPrivilegeCreateSuccessfullBody(privilegeInsertRequest, privilegeId, loggedUser);
                String subject = privilegeEmailHelperService.buildPrivilegeCreateSuccessfullSubject(privilegeInsertRequest,privilegeId, loggedUser);
                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully create privilege : " + privilegeId),
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating privilege: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to create privilege");
        } finally {
            LOGGER.info("End creating privilege");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updatePrivilege(PrivilegeUpdateRequest privilegeUpdateRequest) {
        LOGGER.info("Start updating privilege");
        try {
            privilegeValidationService.validatePrivilegeUpdateRequest(privilegeUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            privilegeRepository.updatePrivilege(privilegeUpdateRequest, userId);

            PrivilegeResponse privilegeResponse = getPrivilegeBasicDetailsById(new PrivilegeDetailsRequest(privilegeUpdateRequest.getId())).getData();


            User loggedUser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);
            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PRIVILEGE_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Privilege Updated")
                    .message("The privilege '" + privilegeUpdateRequest.getName() + "' has been updated.")
                    .actionUrl(VIEW_PRIVILEGE_DETAILS + "/" + privilegeUpdateRequest.getId())
                    .actionText("View Privilege")
                    .icon("Shield")
                    .color("#F59E0B")
                    .metadata(Map.of(
                            "privilegeId", privilegeUpdateRequest.getId(),
                            "privilegeName", privilegeUpdateRequest.getName(),
                            "status", privilegeUpdateRequest.getStatus(),
                            "description", privilegeUpdateRequest.getDescription(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PRIVILEGE_UPDATE.name())
                    .sourceModule(SourceModule.PRIVILEGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            PrivilegeUpdateComparisonResult comparisonResult = comparePrivilegeUpdates(
                    privilegeUpdateRequest,
                    privilegeResponse
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PRIVILEGE_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = privilegeEmailHelperService.buildPrivilegeUpdateSuccessfullSubject(loggedUser);
            String body = privilegeEmailHelperService.buildPrivilegeUpdateSuccessfullBody(loggedUser, comparisonResult);
            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);


            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update privilege", privilegeUpdateRequest.getId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating privilege: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update privilege");
        } finally {
            LOGGER.info("End updating privilege");
        }
    }

    @Override
    public CommonResponse<TerminateResponse> terminatePrivilege(PrivilegeTerminateRequest privilegeTerminateRequest) {
        LOGGER.info("Start terminating privilege");
        try {
            privilegeValidationService.validatePrivilegeTerminateRequest(privilegeTerminateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            PrivilegeResponse privilegeResponse = getPrivilegeBasicDetailsById(new PrivilegeDetailsRequest(privilegeTerminateRequest.getId())).getData();
            privilegeRepository.terminatePrivilege(privilegeTerminateRequest, userId);

            User loggedUser = commonService.getLoggedUser();

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.PRIVILEGE_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Privilege Terminated")
                    .message("The privilege '" + privilegeResponse.getPrivilegeName() + "' has been terminated.")
                    .actionUrl(VIEW_PRIVILEGE_DETAILS + "/" + privilegeResponse.getPrivilegeId())
                    .actionText("View Privilege")
                    .icon("ShieldOff")
                    .color("#DC2626")
                    .metadata(Map.of(
                            "privilegeId", privilegeResponse.getPrivilegeId(),
                            "privilegeName", privilegeResponse.getPrivilegeName(),
                            "privilegeDescription", privilegeResponse.getPrivilegeDescription(),
                            "status", privilegeResponse.getPrivilegeStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.PRIVILEGE_TERMINATE.name())
                    .sourceModule(SourceModule.PRIVILEGE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.PRIVILEGE_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = privilegeEmailHelperService.buildPrivilegeTerminateSuccessfullSubject(loggedUser, privilegeResponse);
            String body = privilegeEmailHelperService.buildPrivilegeTerminateSuccessfullBody(loggedUser, privilegeResponse);

            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminated privilege"),
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating privilege: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate privilege");
        } finally {
            LOGGER.info("End terminating privilege");
        }
    }

    @Override
    public CommonResponse<PrivilegeStatisticsResponse> getPrivilegeStatistics() {
        LOGGER.info("Start fetching privilege statistics from repository");
        try {
            PrivilegeStatisticsResponse privilegeStatisticsResponse = new PrivilegeStatisticsResponse();
            PrivilegeStatisticsResponse.PrivilegeDetails privilegeDetails = privilegeRepository.getPrivilegeDetailsStatistics();
            List<PrivilegeStatisticsResponse.Recent> recentlyUpdatesList = privilegeRepository.getPrivilegeRecentlyUpdateStatistics();
            List<PrivilegeStatisticsResponse.Recent> recentlyCreateList = privilegeRepository.getPrivilegeRecentlyCreateStatistics();
            List<PrivilegeStatisticsResponse.Recent> recentlyTerminateList = privilegeRepository.getPrivilegeRecentlyTerminateStatistics();

            privilegeStatisticsResponse.setPrivilegeDetails(privilegeDetails);
            privilegeStatisticsResponse.setRecentlyUpdates(recentlyUpdatesList);
            privilegeStatisticsResponse.setRecentlyCreate(recentlyCreateList);
            privilegeStatisticsResponse.setRecentlyTerminate(recentlyTerminateList);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    privilegeStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching privilege statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch privilege statistics from database");
        } finally {
            LOGGER.info("End fetching privilege statistics from repository");
        }
    }

    public PrivilegeUpdateComparisonResult comparePrivilegeUpdates(
            PrivilegeUpdateRequest request,
            PrivilegeResponse response
    ) {

        boolean nameChanged = !equals(request.getName(), response.getPrivilegeName());
        boolean descriptionChanged = !equals(request.getDescription(), response.getPrivilegeDescription());
        boolean statusChanged = !equals(request.getStatus(), response.getPrivilegeStatus());

        boolean hasChanges = nameChanged || descriptionChanged || statusChanged;

        return PrivilegeUpdateComparisonResult.builder()
                .privilegeId(response.getPrivilegeId())

                .nameChanged(nameChanged)
                .oldName(response.getPrivilegeName())
                .newName(request.getName())

                .descriptionChanged(descriptionChanged)
                .oldDescription(response.getPrivilegeDescription())
                .newDescription(request.getDescription())

                .statusChanged(statusChanged)
                .oldStatus(response.getPrivilegeStatus())
                .newStatus(request.getStatus())

                .hasChanges(hasChanges)
                .build();
    }

    private boolean equals(String a, String b) {
        return java.util.Objects.equals(a, b);
    }
}
