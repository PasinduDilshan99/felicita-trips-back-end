package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDriverDetailsRequest {

    private String licenseType;
    private String licenseNumber;

    private LocalDate licenseIssueDate;
    private LocalDate licenseExpiryDate;

    private String vehicleTypes;

    private Integer experienceYears;
    private Integer accidentFreeYears;

    private String routeExpertise;

    private Boolean isAvailable;
}