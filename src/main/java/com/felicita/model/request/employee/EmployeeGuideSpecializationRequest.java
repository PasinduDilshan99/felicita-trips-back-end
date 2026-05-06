package com.felicita.model.request.employee;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeGuideSpecializationRequest {

    private String specializationType;

    private String regions;
    private String languages;
    private String certifications;

    private Integer experienceYears;

    private BigDecimal rating;

    private Boolean isAvailable;
}