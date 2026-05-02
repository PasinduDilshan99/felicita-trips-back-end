package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WelcomeEmployeeDto {
    private String employeeType;
    private String employeeCode;
    private String departmentName;
    private String designation;
    private String employmentType;
    private String supervisorId;
    private String supervisorName;
    private String ReportingManagerID;
    private String ReportingManagerName;
    private LocalDate hiringDate;
    private Integer probationMonths;
    private LocalDate probationEndDate;
}
