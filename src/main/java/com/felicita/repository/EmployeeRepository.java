package com.felicita.repository;

import com.felicita.model.dto.FilterItem;
import com.felicita.model.dto.WelcomeEmployeeDto;
import com.felicita.model.request.EmployeeBasicDetailsParamRequest;
import com.felicita.model.request.EmployeeFullDetailsRequest;
import com.felicita.model.request.employee.*;
import com.felicita.model.response.*;

import java.util.List;

public interface EmployeeRepository {
    List<EmployeeWithSocialMediaResponse> getEmployeeWithSocailMedia();

    List<EmployeeWithSocialMediaResponse> getALlEmployeeWithSocailMedia();

    List<EmployeeGuideResponse> getEmployeeGuideDetails();

    TourAssignedEmployeeResponse getEmployeeAssignToTourId(Long tourId);

    List<TourAssignedEmployeeResponse.RelatedOtherTours> getOtherRelatedToursByTourId(Long tourId);

    List<Long> getEmployeeIdsForAssignTour();

    List<EmployeesForAssignTourResponse> getEmployeeDetailsForAssignTour();

    CeoDetailsReponse getCeoDetails();

    List<EmployeeBasicDetailsResponse> getAllEmpmoyeesBasicDetails(EmployeeBasicDetailsParamRequest employeeBasicDetailsParamRequest);

    EmployeeFullDetailsResponse getEmployeeFullDetails(EmployeeFullDetailsRequest employeeFullDetailsRequest);

    EmployeeStatisticsResponse.KpiSummary getKpiSummeryStatistics();

    List<EmployeeStatisticsResponse.DepartmentWiseEmployees> getDepartmentWiseEmployeesListStatistics();

    List<EmployeeStatisticsResponse.EmployeeTypeDistribution> getEmployeeTypeDistributionStatistics();

    List<EmployeeStatisticsResponse.WorkLocationDistribution> getWorkLocationDistributionStatistics();

    List<EmployeeStatisticsResponse.EmployeeGradeDistribution> getEmployeeGradeDistributionStatistics();

    List<EmployeeStatisticsResponse.MonthlyHiringTrend> getMonthlyHiringTrendStatistics();

    List<EmployeeStatisticsResponse.SalaryByDepartment> getSalaryByDepartmentStatistics();

    List<EmployeeStatisticsResponse.PerformanceRatingDistribution> getPerformanceRatingDistributionStatistics();

    List<EmployeeStatisticsResponse.SkillDistribution> getSkillDistributionStatistics();

    List<EmployeeStatisticsResponse.AssetDistribution> getAssetDistributionStatistics();

    List<EmployeeStatisticsResponse.ShiftDistribution> getShiftDistributionStatistics();

    EmployeeBasicDetailsParamsResponse getAllEmpmoyeesBasicDetailsParams();

    WelcomeEmployeeDto getNewlyAddedEmployeeBasicDetails(Long userId);

    Long insertEmployeebasicDetails(EmployeeBasicDetailsRequest basicDetails, Long userId);

    void insertEmployeeEmergencyContacts(Long employeeId, List<EmployeeEmergencyContactRequest> emergencyContacts, Long userId);

    void insertEmployeeAssets(Long employeeId, List<EmployeeAssetRequest> assets, Long userId);

    void insertEmployeeDocuments(Long employeeId, List<EmployeeDocumentRequest> documents, Long userId);

    void insertEmployeeDriverDetails(Long employeeId, EmployeeDriverDetailsRequest driverDetails, Long userId);

    void insertEmployeeGuideSpecializations(Long employeeId, List<EmployeeGuideSpecializationRequest> guideSpecializations, Long userId);

    void insertEmployeeIncentives(Long employeeId, List<EmployeeIncentiveRequest> incentives, Long userId);

    void insertEmployeeSalaryStructures(Long employeeId, List<EmployeeSalaryStructureRequest> salaryStructures, Long userId);

    void insertEmployeeSkills(Long employeeId, List<EmployeeSkillRequest> skills, Long userId);

    void insertEmployeeWorkHistories(Long employeeId, List<EmployeeWorkHistoryRequest> workHistories, Long userId);

    void insertEmployeeShiftAssignments(Long employeeId, List<EmployeeShiftAssignmentRequest> shiftAssignments, Long userId);

    void insertEmployeeSocialMediaAccounts(Long employeeId, List<EmployeeSocialMediaRequest> socialMediaAccounts, Long userId);

    List<FilterItem> getDistnictDesignations();

    List<FilterItem> getActiveEmployeesIdsAndNames();

    List<FilterItem> getSalaryComponents();

    List<FilterItem> getSocialMediaPlatforms();
}
