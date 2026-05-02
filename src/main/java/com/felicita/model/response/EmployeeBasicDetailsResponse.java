package com.felicita.model.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBasicDetailsResponse {

    private Long employeeId;
    private String employeeCode;

    // User Details
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String nic;

    // Employee Details
    private Long employeeTypeId;
    private String employeeType;

    private Long departmentId;
    private String departmentName;

    private Long designationId;
    private String designationName;

    private LocalDate hireDate;
    private String employmentType;
    private String workLocation;
    private String employeeGrade;
    private BigDecimal salary;

    // Supervisor Details
    private Long supervisorId;
    private String supervisorName;

    // Reporting Manager Details
    private Long reportingManagerId;
    private String reportingManagerName;

    // Status
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}