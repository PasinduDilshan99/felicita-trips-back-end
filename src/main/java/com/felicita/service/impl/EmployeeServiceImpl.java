package com.felicita.service.impl;

import com.felicita.email.EmployeeEmailHelperService;
import com.felicita.exception.*;
import com.felicita.model.dto.*;
import com.felicita.model.enums.NotificationType;
import com.felicita.model.enums.Priority;
import com.felicita.model.enums.Privileges;
import com.felicita.model.enums.SourceModule;
import com.felicita.model.request.EmployeeBasicDetailsParamRequest;
import com.felicita.model.request.EmployeeFullDetailsRequest;
import com.felicita.model.request.employee.EmployeeCreateRequest;
import com.felicita.model.response.*;
import com.felicita.repository.EmployeeRepository;
import com.felicita.repository.UserRepository;
import com.felicita.security.model.User;
import com.felicita.service.CommonService;
import com.felicita.service.EmailService;
import com.felicita.service.EmployeeService;
import com.felicita.util.CommonResponseMessages;
import com.felicita.validation.EmployeeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.felicita.util.Constant.COMPANY_EMAIL;
import static com.felicita.util.FrontEndUrls.VIEW_DESTINATION_DETAILS;
import static com.felicita.util.FrontEndUrls.VIEW_EMPLOYEE_DETAILS;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final CommonService commonService;
    private final EmailService emailService;
    private final EmployeeValidationService employeeValidationService;
    private final EmployeeEmailHelperService employeeEmailHelperService;
    private final UserRepository userRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CommonService commonService, EmailService emailService, EmployeeValidationService employeeValidationService, EmployeeEmailHelperService employeeEmailHelperService, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.commonService = commonService;
        this.emailService = emailService;
        this.employeeValidationService = employeeValidationService;
        this.employeeEmailHelperService = employeeEmailHelperService;
        this.userRepository = userRepository;
    }

    @Override
    public CommonResponse<List<EmployeeWithSocialMediaResponse>> getEmployeeWithSocailMedia() {
        LOGGER.info("Start fetching employee with social media details from repository");
        try {
            List<EmployeeWithSocialMediaResponse> employeeWithSocialMediaResponses = employeeRepository.getEmployeeWithSocailMedia();
            LOGGER.info("Fetched employee with social media details successfully");
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            employeeWithSocialMediaResponses,
                            Instant.now()
                    )
            );
        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee with social media details : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee with social media details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee with social media details from database");
        } finally {
            LOGGER.info("End fetching employee with social media details from repository");
        }
    }

    @Override
    public CommonResponse<List<EmployeeWithSocialMediaResponse>> getALlEmployeeWithSocailMedia() {
        LOGGER.info("Start fetching all employee with social media details from repository");
        try {
            List<EmployeeWithSocialMediaResponse> employeeWithSocialMediaResponses = employeeRepository.getALlEmployeeWithSocailMedia();
            LOGGER.info("Fetched all employee with social media details successfully");
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            employeeWithSocialMediaResponses,
                            Instant.now()
                    )
            );
        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching all employee with social media details : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all employee with social media details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all employee with social media details from database");
        } finally {
            LOGGER.info("End fetching all employee with social media details from repository");
        }
    }

    @Override
    public CommonResponse<List<EmployeeGuideResponse>> getEmployeeGiudeDetails() {
        LOGGER.info("Start fetching all employee with social media details from repository");
        try {
            List<EmployeeGuideResponse> employeeGuideResponses = employeeRepository.getEmployeeGuideDetails();
            LOGGER.info(employeeGuideResponses.toString());
            LOGGER.info("Fetched all employee with social media details successfully");
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            employeeGuideResponses,
                            Instant.now()
                    )
            );
        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching all employee with social media details : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching all employee with social media details: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all employee with social media details from database");
        } finally {
            LOGGER.info("End fetching all employee with social media details from repository");
        }
    }

    @Override
    public CommonResponse<TourAssignedEmployeeResponse> getEmployeeAssignToTourId(Long tourId) {
        LOGGER.info("Start fetching employee assign to tour id from repository");
        try {
            TourAssignedEmployeeResponse tourAssignedEmployeeResponses =
                    employeeRepository.getEmployeeAssignToTourId(tourId);
            List<TourAssignedEmployeeResponse.RelatedOtherTours> relatedOtherTours =
                    employeeRepository.getOtherRelatedToursByTourId(tourId);
            tourAssignedEmployeeResponses.setRelatedOtherTours(relatedOtherTours);
            return (
                    new CommonResponse<>(
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                            CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                            tourAssignedEmployeeResponses,
                            Instant.now()
                    )
            );
        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee assign to tour id : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee assign to tour id from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all employee assign to tour id from repository");
        } finally {
            LOGGER.info("End fetching employee assign to tour id from repository");
        }
    }

    @Override
    public CommonResponse<List<EmployeesForAssignTourResponse>> getEmployeeDetailsForAssignTour() {
        LOGGER.info("Start fetching employee assign for tour from repository");
        try {
            List<EmployeesForAssignTourResponse> employeesForAssignTourResponses =
                    employeeRepository.getEmployeeDetailsForAssignTour();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeesForAssignTourResponses,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee assign for tour  : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee assign for tour  from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch all employee assign for tour from repository");
        } finally {
            LOGGER.info("End fetching employee assign for tour from repository");
        }
    }

    @Override
    public CommonResponse<CeoDetailsReponse> getCeoDetails() {
        LOGGER.info("Start fetching ceo details from repository");
        try {
            CeoDetailsReponse ceoDetailsReponse =
                    employeeRepository.getCeoDetails();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    ceoDetailsReponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching ceo details  : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching ceo details from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch ceo details from repository");
        } finally {
            LOGGER.info("End fetching ceo details from repository");
        }
    }

    @Override
    public CommonResponse<List<EmployeeBasicDetailsResponse>> getAllEmpmoyeesBasicDetails(EmployeeBasicDetailsParamRequest employeeBasicDetailsParamRequest) {
        LOGGER.info("Start fetching employee basic details from repository");
        try {
            List<EmployeeBasicDetailsResponse> employeeBasicDetailsResponseList = employeeRepository.getAllEmpmoyeesBasicDetails(employeeBasicDetailsParamRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeeBasicDetailsResponseList,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee basic details  : {}", e.getMessage(), e);
            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    Arrays.asList(),
                    Instant.now());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee basic details from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee basic details from repository");
        } finally {
            LOGGER.info("End fetching employee basic details from repository");
        }
    }

    @Override
    public CommonResponse<EmployeeFullDetailsResponse> getEmployeeFullDetails(EmployeeFullDetailsRequest employeeFullDetailsRequest) {
        LOGGER.info("Start fetching employee full details from repository");
        try {
            EmployeeFullDetailsResponse employeeFullDetailsResponse = employeeRepository.getEmployeeFullDetails(employeeFullDetailsRequest);

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeeFullDetailsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee full details  : {}", e.getMessage(), e);
            throw new DataNotFoundErrorExceptionHandler(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee full details from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee full details from repository");
        } finally {
            LOGGER.info("End fetching employee full details from repository");
        }
    }

    @Override
    public CommonResponse<EmployeeStatisticsResponse> getEmployeeStatistics() {

        LOGGER.info("Start fetching employee statistics from repository");

        try {
            EmployeeStatisticsResponse employeeStatisticsResponse = new EmployeeStatisticsResponse();

            EmployeeStatisticsResponse.KpiSummary kpiSummary =
                    employeeRepository.getKpiSummeryStatistics();
            employeeStatisticsResponse.setKpiSummary(kpiSummary);

            employeeStatisticsResponse.setDepartmentWiseEmployees(
                    employeeRepository.getDepartmentWiseEmployeesListStatistics()
            );

            employeeStatisticsResponse.setEmployeeTypeDistribution(
                    employeeRepository.getEmployeeTypeDistributionStatistics()
            );

            employeeStatisticsResponse.setWorkLocationDistribution(
                    employeeRepository.getWorkLocationDistributionStatistics()
            );

            employeeStatisticsResponse.setEmployeeGradeDistribution(
                    employeeRepository.getEmployeeGradeDistributionStatistics()
            );

            employeeStatisticsResponse.setMonthlyHiringTrend(
                    employeeRepository.getMonthlyHiringTrendStatistics()
            );

            employeeStatisticsResponse.setSalaryByDepartment(
                    employeeRepository.getSalaryByDepartmentStatistics()
            );

            employeeStatisticsResponse.setPerformanceRatingDistribution(
                    employeeRepository.getPerformanceRatingDistributionStatistics()
            );

            employeeStatisticsResponse.setSkillDistribution(
                    employeeRepository.getSkillDistributionStatistics()
            );

            employeeStatisticsResponse.setAssetDistribution(
                    employeeRepository.getAssetDistributionStatistics()
            );

            employeeStatisticsResponse.setShiftDistribution(
                    employeeRepository.getShiftDistributionStatistics()
            );

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeeStatisticsResponse,
                    Instant.now()
            );

        } catch (DataNotFoundErrorExceptionHandler | DataAccessErrorExceptionHandler e) {
            throw e;

        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee statistics: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler(
                    "Failed to fetch employee statistics from database"
            );

        } finally {
            LOGGER.info("End fetching employee statistics from repository");
        }
    }

    @Override
    public CommonResponse<EmployeeBasicDetailsParamsResponse> getAllEmpmoyeesBasicDetailsParams() {
        LOGGER.info("Start fetching employee basic details params from repository");
        try {
            EmployeeBasicDetailsParamsResponse employeeBasicDetailsParamsResponse = employeeRepository.getAllEmpmoyeesBasicDetailsParams();

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeeBasicDetailsParamsResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee basic details params  : {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee basic details params from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee basic details params from repository");
        } finally {
            LOGGER.info("End fetching employee basic details params from repository");
        }
    }

    @Override
    public CommonResponse<InsertResponse> createEmployee(EmployeeCreateRequest employeeCreateRequest) {
        LOGGER.info("Start execute insert employee request.");
        try {
            employeeValidationService.validateEmployeeCreateRequest(employeeCreateRequest);
            Long userId = commonService.getUserIdBySecurityContext();
            String email = commonService.getUserEmailBySecurityContext();
            User loggedUser = commonService.getLoggedUser();
            String employeeUniqueCode = commonService.createEmployeeUniqueEmployeeCode();
            employeeCreateRequest.getBasicDetails().setEmployeeCode(employeeUniqueCode);

            Long employeeId = employeeRepository.insertEmployeebasicDetails(employeeCreateRequest.getBasicDetails(), userId);

            if (Objects.nonNull(employeeId)) {
                employeeRepository.insertEmployeeEmergencyContacts(employeeId, employeeCreateRequest.getEmergencyContacts(), userId);
                employeeRepository.insertEmployeeAssets(employeeId, employeeCreateRequest.getAssets(), userId);
                employeeRepository.insertEmployeeDocuments(employeeId, employeeCreateRequest.getDocuments(), userId);
                employeeRepository.insertEmployeeDriverDetails(employeeId, employeeCreateRequest.getDriverDetails(), userId);
                if (employeeCreateRequest.getBasicDetails().getEmployeeTypeId().equals(1)){
                employeeRepository.insertEmployeeIncentives(employeeId, employeeCreateRequest.getIncentives(), userId);
                }
                employeeRepository.insertEmployeeGuideSpecializations(employeeId, employeeCreateRequest.getGuideSpecializations(), userId);
                employeeRepository.insertEmployeeSalaryStructures(employeeId, employeeCreateRequest.getSalaryStructures(), userId);
                employeeRepository.insertEmployeeSkills(employeeId, employeeCreateRequest.getSkills(), userId);
                employeeRepository.insertEmployeeWorkHistories(employeeId, employeeCreateRequest.getWorkHistories(), userId);
                employeeRepository.insertEmployeeShiftAssignments(employeeId, employeeCreateRequest.getShiftAssignments(), userId);
                employeeRepository.insertEmployeeSocialMediaAccounts(employeeId, employeeCreateRequest.getSocialMediaAccounts(), userId);
            }

            // insert leave plan

            List<SupervisorBasicDetailsDto> supervisorDetails = commonService.getSupervisorBasicDetailsByUserId(userId);

            List<Long> supervisorUserIds = commonService.extractSupervisorUserIds(supervisorDetails);
            supervisorUserIds.add(userId);
            NotificationInsertRequestDto notificationInsertRequestDto = NotificationInsertRequestDto.builder()
                    .notificationType(NotificationType.EMPLOYEE_CREATED.name())
                    .priority(Priority.HIGH.name())
                    .title("New Employee Created")
                    .message("A new employee '" + employeeCreateRequest.getBasicDetails().getEmployeeCode() + "' has been created.")
                    .actionUrl(VIEW_EMPLOYEE_DETAILS + "/" + employeeId)
                    .actionText("View Employee")
                    .icon("UserPlus")
                    .color("#10B981")
                    .metadata(Map.of(
                            "employeeId", employeeId,
                            "employeeCode", employeeCreateRequest.getBasicDetails().getEmployeeCode(),
                            "departmentId", employeeCreateRequest.getBasicDetails().getDepartmentId(),
                            "designationId", employeeCreateRequest.getBasicDetails().getDesignationId(),
                            "createdBy", userId
                    ))
                    .isArchived(false)
                    .isDeleted(false)
                    .assignedTo(null)
                    .targetRole(Privileges.EMPLOYEE_CREATE.name())
                    .sourceModule(SourceModule.EMPLOYEE.name())
                    .expiresAt(null)
                    .createdBy(userId)
                    .build();

            Long notificationId = commonService.createNotification(notificationInsertRequestDto);

            commonService.createNotificationRecipients(notificationId, supervisorUserIds);

            if (Objects.nonNull(employeeId)) {
                List<String> emailNotificationEnableSupervisors = commonService.getSupervisorEmailsWhichEnableNotificationForGiven(NotificationType.EMPLOYEE_CREATED.name(), supervisorUserIds);
                emailNotificationEnableSupervisors.remove(email);
                emailNotificationEnableSupervisors.add(COMPANY_EMAIL);

                WelcomeEmployeeDto welcomeEmployeeDto = employeeRepository.getNewlyAddedEmployeeBasicDetails(userId);

                String body = employeeEmailHelperService.buildEmployeeCreateSuccessfullBody(welcomeEmployeeDto, loggedUser);
                String subject = employeeEmailHelperService.buildEmployeeCreateSuccessfullSubject(welcomeEmployeeDto, loggedUser);
//                emailService.sendFromDev(email, emailNotificationEnableSupervisors, subject, body);

                UserBasicDetailsDto userBasicDetailsDto = userRepository.getUserBasicDetailsForEmployeeCreate(userId);
                String welcomeBody = employeeEmailHelperService.buildEmployeeCreateSuccessfullBodyForEmployee(userBasicDetailsDto,welcomeEmployeeDto, loggedUser);
                String welcomeSubject = employeeEmailHelperService.buildEmployeeCreateSuccessfullSubjectForEmployee(userBasicDetailsDto,welcomeEmployeeDto, loggedUser);
//                emailService.sendFromMain(email, welcomeSubject, welcomeBody);

            }

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_INSERT_CODE,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_INSERT_MESSAGE,
                    new InsertResponse("Successfully insert employee request"),
                    Instant.now());

        } catch (ValidationFailedErrorExceptionHandler vfe) {
            LOGGER.error(vfe.toString());
            throw new ValidationFailedErrorExceptionHandler("validation failed in the insert employee request", vfe.getValidationFailedResponses());
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
    public CommonResponse<EmployeeCreateDataResponse> getCreateEmployeeData() {
        LOGGER.info("Start fetching employee basic details params from repository");
        try {
            EmployeeBasicDetailsParamsResponse employeeBasicDetailsParamsResponse = employeeRepository.getAllEmpmoyeesBasicDetailsParams();

            EmployeeCreateDataResponse employeeCreateDataResponse = new EmployeeCreateDataResponse();
            
            employeeCreateDataResponse.setEmployeeTypes(employeeBasicDetailsParamsResponse.getEmployeeTypes());
            employeeCreateDataResponse.setDepartments(employeeBasicDetailsParamsResponse.getDepartments());
            employeeCreateDataResponse.setDesignationTypes(employeeRepository.getDistnictDesignations());
            employeeCreateDataResponse.setEmploymentTypes(getUniqueEmploymentTypes());
            employeeCreateDataResponse.setBankNames(getUniqueBankNames());
            employeeCreateDataResponse.setWorkLocations(getUniqueWorkLocations());
            employeeCreateDataResponse.setEmployeeGrades(getUniqueEmployeeGrades());
            List<FilterItem> employees = employeeRepository.getActiveEmployeesIdsAndNames();
            employeeCreateDataResponse.setSupervisors(employees);
            employeeCreateDataResponse.setReportingManagers(employees);
            employeeCreateDataResponse.setStatuses(employeeBasicDetailsParamsResponse.getStatuses());
            employeeCreateDataResponse.setSalaryComponents(employeeRepository.getSalaryComponents());
            employeeCreateDataResponse.setShiftTypes(getUniqueAssetTypes());
            employeeCreateDataResponse.setSocialMediaPlatforms(employeeRepository.getSocialMediaPlatforms());
            

            return new CommonResponse<>(
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_CODE,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_STATUS,
                    CommonResponseMessages.SUCCESSFULLY_RETRIEVE_MESSAGE,
                    employeeCreateDataResponse,
                    Instant.now());

        } catch (DataNotFoundErrorExceptionHandler e) {
            LOGGER.error("Error occurred while fetching employee basic details params  : {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error occurred while fetching employee basic details params from repository: {}", e.getMessage(), e);
            throw new InternalServerErrorExceptionHandler("Failed to fetch employee basic details params from repository");
        } finally {
            LOGGER.info("End fetching employee basic details params from repository");
        }
    }

    private List<FilterItem> getUniqueAssetTypes() {
        return List.of(
                FilterItem.builder().id(1L).label("Laptop").build(),
                FilterItem.builder().id(2L).label("Desktop").build(),
                FilterItem.builder().id(3L).label("Mobile Phone").build(),
                FilterItem.builder().id(4L).label("Printer").build(),
                FilterItem.builder().id(5L).label("Vehicle").build()
        );
    }

    private List<FilterItem> getUniqueEmployeeGrades() {
        return List.of(
                FilterItem.builder().id(1L).label("Grade A").build(),
                FilterItem.builder().id(2L).label("Grade B").build(),
                FilterItem.builder().id(3L).label("Grade C").build(),
                FilterItem.builder().id(4L).label("Executive").build(),
                FilterItem.builder().id(5L).label("Manager").build()
        );
    }

    private List<FilterItem> getUniqueWorkLocations() {
        return List.of(
                FilterItem.builder().id(1L).label("Colombo").build(),
                FilterItem.builder().id(2L).label("Negombo").build(),
                FilterItem.builder().id(3L).label("Kandy").build(),
                FilterItem.builder().id(4L).label("Galle").build(),
                FilterItem.builder().id(5L).label("Jaffna").build()
        );
    }

    private List<FilterItem> getUniqueBankNames() {
        return List.of(
                FilterItem.builder().id(1L).label("Bank of Ceylon").build(),
                FilterItem.builder().id(2L).label("People’s Bank").build(),
                FilterItem.builder().id(3L).label("Commercial Bank").build(),
                FilterItem.builder().id(4L).label("Sampath Bank").build(),
                FilterItem.builder().id(5L).label("HNB Bank").build()
        );
    }

    private List<FilterItem> getUniqueEmploymentTypes() {
        return List.of(
                FilterItem.builder().id(1L).label("Permanent").build(),
                FilterItem.builder().id(2L).label("Contract").build(),
                FilterItem.builder().id(3L).label("Temporary").build(),
                FilterItem.builder().id(4L).label("Internship").build(),
                FilterItem.builder().id(5L).label("Freelance").build()
        );
    }
}
