package com.felicita.model.request.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeBasicDetailsRequest {
    private Long userId;
    private String employeeCode;
    private Integer employeeTypeId;
    private Integer departmentId;
    private Integer designationId;
    private LocalDate hireDate;
    private String employmentType;
    private Long supervisorId;
    private Long reportingManagerId;
    private BigDecimal salary;
    private String bankAccountNumber;
    private String bankName;
    private String bankBranch;
    private String ifscCode;
    private String uanNumber;
    private String pfNumber;
    private String esiNumber;
    private Integer probationPeriodMonths;
    private LocalDate probationEndDate;
    private LocalDate confirmationDate;
    private LocalDate exitDate;
    private String workLocation;
    private String costCenter;
    private String employeeGrade;
    private String status;
}