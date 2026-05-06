package com.felicita.model.request.employee;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeIncentiveRequest {

    private LocalDate incentiveDate;

    private String incentiveType;

    private BigDecimal amount;

    private String calculationBasis;

    private String referenceId;

    private String paymentStatus;

    private LocalDate paidDate;
}