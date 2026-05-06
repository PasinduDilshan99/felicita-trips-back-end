package com.felicita.model.request.employee;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeWorkHistoryRequest {

    private Integer designationId;

    private Integer departmentId;

    private BigDecimal salary;

    private LocalDate startDate;
    private LocalDate endDate;

    private String employmentType;

    private String reason;

    private String notes;

    private String status;
}