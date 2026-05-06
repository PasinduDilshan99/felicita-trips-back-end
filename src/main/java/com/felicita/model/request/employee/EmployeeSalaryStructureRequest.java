package com.felicita.model.request.employee;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeSalaryStructureRequest {

    private Integer componentId;

    private BigDecimal amount;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private String status;
}