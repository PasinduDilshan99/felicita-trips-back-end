package com.felicita.model.request.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateRequest {

    private EmployeeBasicDetailsRequest basicDetails;

    private List<EmployeeEmergencyContactRequest> emergencyContacts;

    private List<EmployeeAssetRequest> assets;

    private List<EmployeeDocumentRequest> documents;

    private EmployeeDriverDetailsRequest driverDetails;

    private List<EmployeeGuideSpecializationRequest> guideSpecializations;

    private List<EmployeeIncentiveRequest> incentives;

    private List<EmployeeSalaryStructureRequest> salaryStructures;

    private List<EmployeeSkillRequest> skills;

    private List<EmployeeWorkHistoryRequest> workHistories;

    private List<EmployeeShiftAssignmentRequest> shiftAssignments;

    private List<EmployeeSocialMediaRequest> socialMediaAccounts;
}