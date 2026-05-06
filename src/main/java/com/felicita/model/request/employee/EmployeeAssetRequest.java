package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAssetRequest {

    private String assetType;
    private String assetId;
    private String assetName;
    private String serialNumber;
    private String model;

    private LocalDate assignedDate;
    private LocalDate returnDate;

    private String conditionOnAssignment;
    private String conditionOnReturn;
    private String notes;

    private Long assignedBy;
    private String status;
}