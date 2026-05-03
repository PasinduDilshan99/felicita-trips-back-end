package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeSkillRequest {

    private String skillName;

    private String skillCategory;

    private String proficiencyLevel;

    private String certification;

    private LocalDate certifiedDate;
    private LocalDate expiryDate;

    private Boolean verified;

    private Long verifiedBy;

    private LocalDate verifiedDate;
}