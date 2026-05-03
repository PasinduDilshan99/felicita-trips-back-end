package com.felicita.model.request.employee;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeSocialMediaRequest {

    private Integer platformId;

    private String username;

    private String profileUrl;

    private Integer followerCount;

    private Boolean isPrimary;
    private Boolean isPublic;

    private Boolean verified;

    private Long verifiedBy;

    private LocalDate verifiedDate;

    private LocalDate lastUpdated;

    private String status;
}