package com.felicita.service.impl;

import com.felicita.email.RoleEmailHelperService;
import com.felicita.exception.DataAccessErrorExceptionHandler;
import com.felicita.exception.DataNotFoundErrorExceptionHandler;
import com.felicita.exception.InternalServerErrorExceptionHandler;
import com.felicita.exception.ValidationFailedErrorExceptionHandler;
import com.felicita.model.dto.NotificationInsertRequestDto;
import com.felicita.model.dto.SupervisorBasicDetailsDto;
import com.felicita.model.enums.*;
import com.felicita.model.other.RoleUpdateComparisonResult;
import com.felicita.model.request.*;
import com.felicita.model.response.*;
import com.felicita.repository.RoleRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.RoleService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.RoleValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_ROLE_DETAILS;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final RoleValidationService roleValidationService;
    private final CommonService commonService;
    private final RoleEmailHelperService roleEmailHelperService;
    private final EmailService emailService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleValidationService roleValidationService,
                           CommonService commonService, RoleEmailHelperService roleEmailHelperService,
                           EmailService emailService) {
        this.roleRepository = roleRepository;
        this.roleValidationService = roleValidationService;
        this.commonService = commonService;
        this.roleEmailHelperService = roleEmailHelperService;
        this.emailService = emailService;
    }

    @Override
    public CommonResponse<RoleParamResponse> getAllRoles(RoleDataParamRequest roleDataParamRequest) {
        LOGGER.info("Start fetching all roles from repository");
        try {
            RoleParamResponse roleParamResponse = roleRepository.getAllRoles(roleDataParamRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    roleParamResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all roles: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all roles from database");
        } finally {
            LOGGER.info("End fetching all roles from repository");
        }
    }

    @Override
    public CommonResponse<List<RoleNameAndIdResponse>> getAllRoleNamesAndIds() {
        LOGGER.info("Start fetching role names and ids from repository");
        try {
            List<RoleNameAndIdResponse> roleNameAndIdResponseList = roleRepository.getAllRoleNamesAndIds();
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    roleNameAndIdResponseList,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching role names and ids: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch role names and ids from database");
        } finally {
            LOGGER.info("End fetching role names and ids from repository");
        }
    }

    @Override
    public CommonResponse<RoleDetailsResponse> getRoleDetailsById(RoleDetailsRequest roleDetailsRequest) {
        LOGGER.info("Start fetching role details by id from repository");
        try {
            RoleDetailsResponse roleDetailsResponse = roleRepository.getRoleDetailsById(roleDetailsRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    roleDetailsResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching role details by id: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch role details from database");
        } finally {
            LOGGER.info("End fetching role details by id from repository");
        }
    }

    @Override
    public CommonResponse<RoleResponse> getRoleBasicDetailsById(RoleDetailsRequest roleDetailsRequest) {
        LOGGER.info("Start fetching role basic details by id from repository");
        try {
            RoleResponse roleResponse = roleRepository.getRoleBasicDetailsById(roleDetailsRequest);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    roleResponse,
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching role basic details by id: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch role basic details from database");
        } finally {
            LOGGER.info("End fetching role basic details by id from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createRole(RoleInsertRequest roleInsertRequest) {
        LOGGER.info("Start creating role");
        try {
            roleValidationService.validateRoleInsertRequest(roleInsertRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            User loggedUser = commonService.getLoggedUser();

            Long roleId = roleRepository.createRole(roleInsertRequest, userId);
            roleRepository.addPrivilegesToRole(roleId, roleInsertRequest.getPrivilegesIds(), userId);

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ROLE_CREATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("New Role Created")
                    .message("A new role '" + roleInsertRequest.getName() + "' has been created.")
                    .actionUrl(VIEW_ROLE_DETAILS + "/" + roleId)
                    .actionText("View Role")
                    .icon("Users")
                    .color("#8B5CF6")
                    .metadata(Map.of(
                            "roleId", roleId,
                            "roleName", roleInsertRequest.getName(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ROLE_CREATE.name())
                    .sourceModule(SourceModule.ROLE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (roleId != null) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ROLE_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
                String body = roleEmailHelperService.buildRoleCreateSuccessfullBody(roleInsertRequest, roleId, loggedUser);
                String subject = roleEmailHelperService.buildRoleCreateSuccessfullSubject(roleInsertRequest, roleId, loggedUser);
                emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);
            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully create role : " + roleId),
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler |
                 ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating role: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to create role");
        } finally {
            LOGGER.info("End creating role");
        }
    }

    @Override
    public CommonResponse<UpdateResponse> updateRole(RoleUpdateRequest roleUpdateRequest) {
        LOGGER.info("Start updating role");
        try {
            roleValidationService.validateRoleUpdateRequest(roleUpdateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            roleRepository.updateRole(roleUpdateRequest, userId);
            roleRepository.addPrivilegesToRole(roleUpdateRequest.getId(), roleUpdateRequest.getAddPrivilegesIds(), userId);
            roleRepository.terminatePrivilegesToRole(roleUpdateRequest.getId(), roleUpdateRequest.getRemovePrivilegesIds(), userId);
            roleRepository.updateRolePrivileges(roleUpdateRequest.getUpdatePrivileges(), userId);

            RoleResponse roleResponse = getRoleBasicDetailsById(new RoleDetailsRequest(roleUpdateRequest.getId())).getData();

            User loggedUser = commonService.getLoggedUser();
            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ROLE_UPDATED.name())
                    .priority(Priority.MEDIUM.name())
                    .title("Role Updated")
                    .message("The role '" + roleUpdateRequest.getName() + "' has been updated.")
                    .actionUrl(VIEW_ROLE_DETAILS + "/" + roleUpdateRequest.getId())
                    .actionText("View Role")
                    .icon("Users")
                    .color("#F59E0B")
                    .metadata(Map.of(
                            "roleId", roleUpdateRequest.getId(),
                            "roleName", roleUpdateRequest.getName(),
                            "status", roleUpdateRequest.getStatus(),
                            "description", roleUpdateRequest.getDescription(),
                            "updatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ROLE_UPDATE.name())
                    .sourceModule(SourceModule.ROLE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            RoleUpdateComparisonResult comparisonResult = compareRoleUpdates(
                    roleUpdateRequest,
                    roleResponse
            );

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ROLE_UPDATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);
            String subject = roleEmailHelperService.buildRoleUpdateSuccessfullSubject(loggedUser);
            String body = roleEmailHelperService.buildRoleUpdateSuccessfullBody(loggedUser, comparisonResult);
            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_UPDATE_MESSAGE,
                    new UpdateResponse("Successfully update role", roleUpdateRequest.getId()),
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while updating role: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to update role");
        } finally {
            LOGGER.info("End updating role");
        }
    }



    @Override
    public CommonResponse<TerminateResponse> terminateRole(RoleTerminateRequest roleTerminateRequest) {
        LOGGER.info("Start terminating role");
        try {
            roleValidationService.validateRoleTerminateRequest(roleTerminateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            RoleResponse roleResponse = getRoleBasicDetailsById(new RoleDetailsRequest(roleTerminateRequest.getRoleId())).getData();
            roleRepository.terminateRole(roleTerminateRequest, userId);

            User loggedUser = commonService.getLoggedUser();

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);

            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.ROLE_TERMINATED.name())
                    .priority(Priority.HIGH.name())
                    .title("Role Terminated")
                    .message("The role '" + roleResponse.getRoleName() + "' has been terminated.")
                    .actionUrl(VIEW_ROLE_DETAILS + "/" + roleResponse.getRoleId())
                    .actionText("View Role")
                    .icon("UserX")
                    .color("#DC2626")
                    .metadata(Map.of(
                            "roleId", roleResponse.getRoleId(),
                            "roleName", roleResponse.getRoleName(),
                            "roleDescription", roleResponse.getRoleDescription(),
                            "status", roleResponse.getRoleStatus(),
                            "terminatedBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.ROLE_TERMINATE.name())
                    .sourceModule(SourceModule.ROLE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.ROLE_TERMINATED.name(), supervisorUserIds);
            emailNotificationEnableSupervisors.remove(loggedUser.getEmail());
            emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

            String subject = roleEmailHelperService.buildRoleTerminateSuccessfullSubject(loggedUser, roleResponse);
            String body = roleEmailHelperService.buildRoleTerminateSuccessfullBody(loggedUser, roleResponse);

            emailService.sendFromDev(loggedUser.getEmail(), emailNotificationEnableSupervisors, subject, body);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_TERMINATE_MESSAGE,
                    new TerminateResponse("Successfully terminated role"),
                    Instant.now());
        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler | ValidationFailedErrorExceptionHandler e) {
            LOGGER.error(e.toString());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while terminating role: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to terminate role");
        } finally {
            LOGGER.info("End terminating role");
        }
    }

    @Override
    public CommonResponse<RoleStatisticsResponse> getRoleStatistics() {
        LOGGER.info("Start fetching role statistics from repository");
        try {
            RoleStatisticsResponse roleStatisticsResponse = new RoleStatisticsResponse();
            RoleStatisticsResponse.RoleDetails roleDetails = roleRepository.getRoleDetailsStatistics();
            List<RoleStatisticsResponse.Recent> recentlyUpdatesList = roleRepository.getRoleRecentlyUpdateStatistics();
            List<RoleStatisticsResponse.Recent> recentlyCreateList = roleRepository.getRoleRecentlyCreateStatistics();
            List<RoleStatisticsResponse.Recent> recentlyTerminateList = roleRepository.getRoleRecentlyTerminateStatistics();
            List<RoleStatisticsResponse.RoleUsage> roleUsages = roleRepository.getRoleUsageTerminateStatistics();

            roleStatisticsResponse.setRoleDetails(roleDetails);
            roleStatisticsResponse.setRecentlyUpdates(recentlyUpdatesList);
            roleStatisticsResponse.setRecentlyCreate(recentlyCreateList);
            roleStatisticsResponse.setRecentlyTerminate(recentlyTerminateList);
            roleStatisticsResponse.setRoleUsages(roleUsages);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    roleStatisticsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching role statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch role statistics from database");
        } finally {
            LOGGER.info("End fetching role statistics from repository");
        }
    }

    private RoleUpdateComparisonResult compareRoleUpdates(
            RoleUpdateRequest request,
            RoleResponse response
    ) {

        boolean nameChanged =
                !java.util.Objects.equals(request.getName(), response.getRoleName());

        boolean descriptionChanged =
                !java.util.Objects.equals(request.getDescription(), response.getRoleDescription());

        boolean statusChanged =
                !java.util.Objects.equals(request.getStatus(), response.getRoleStatus());

        boolean privilegesAdded =
                request.getAddPrivilegesIds() != null &&
                        !request.getAddPrivilegesIds().isEmpty();

        boolean privilegesRemoved =
                request.getRemovePrivilegesIds() != null &&
                        !request.getRemovePrivilegesIds().isEmpty();

        boolean hasChanges =
                nameChanged ||
                        descriptionChanged ||
                        statusChanged ||
                        privilegesAdded ||
                        privilegesRemoved;

        return RoleUpdateComparisonResult.builder()
                .roleId(response.getRoleId())

                .nameChanged(nameChanged)
                .oldName(response.getRoleName())
                .newName(request.getName())

                .descriptionChanged(descriptionChanged)
                .oldDescription(response.getRoleDescription())
                .newDescription(request.getDescription())

                .statusChanged(statusChanged)
                .oldStatus(response.getRoleStatus())
                .newStatus(request.getStatus())

                .privilegesAdded(privilegesAdded)
                .addedPrivilegeIds(request.getAddPrivilegesIds())

                .privilegesRemoved(privilegesRemoved)
                .removedPrivilegeIds(request.getRemovePrivilegesIds())

                .hasChanges(hasChanges)
                .build();
    }

}