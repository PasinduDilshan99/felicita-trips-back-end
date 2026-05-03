package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeShiftAssignmentRequest {

    private Integer shiftId;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Long assignedBy;

    private String status;
}