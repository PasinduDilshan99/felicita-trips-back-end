package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFullDetailsResponse {

    // =========================================
    // Employee Basic Details
    // =========================================

    private Long employeeId;
    private String employeeCode;

    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String nic;

    private Long employeeTypeId;
    private String employeeType;

    private Long departmentId;
    private String departmentName;

    private Long designationId;
    private String designationName;

    private LocalDate hireDate;
    private String employmentType;
    private String workLocation;
    private String employeeGrade;
    private BigDecimal salary;

    private Long supervisorId;
    private String supervisorName;

    private Long reportingManagerId;
    private String reportingManagerName;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =========================================
    // Child Collections
    // =========================================

    private List<ShiftDetails> shifts;
    private List<SkillDetails> skills;
    private List<SocialMediaDetails> socialMedia;
    private List<PerformanceMetricDetails> performanceMetrics;
    private List<PerformanceReviewDetails> performanceReviews;
    private List<EmergencyContactDetails> emergencyContacts;
    private List<AssetDetails> assets;

    // =========================================
    // Shift Details
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShiftDetails {

        private String shiftName;
        private LocalTime startTime;
        private LocalTime endTime;

        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
    }

    // =========================================
    // Skill Details
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillDetails {

        private String skillName;
        private String skillCategory;
        private String proficiencyLevel;

        private String certification;

        private LocalDate certifiedDate;
        private LocalDate expiryDate;

        private Boolean verified;
    }

    // =========================================
    // Social Media Details
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialMediaDetails {

        private String platformName;
        private String username;
        private String profileUrl;

        private Integer followerCount;

        private Boolean primary;
        private Boolean verified;
    }

    // =========================================
    // Performance Metrics
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceMetricDetails {

        private LocalDate metricDate;

        private String metricType;

        private BigDecimal metricValue;
        private BigDecimal targetValue;
        private BigDecimal achievementPercentage;

        private String notes;
    }

    // =========================================
    // Performance Reviews
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceReviewDetails {

        private LocalDate reviewPeriodStart;
        private LocalDate reviewPeriodEnd;

        private LocalDate reviewDate;

        private BigDecimal overallRating;

        private Integer attendanceRating;
        private Integer productivityRating;
        private Integer qualityRating;
        private Integer teamworkRating;

        private String strengths;
        private String areasForImprovement;
        private String goals;
        private String comments;

        private String status;
    }

    // =========================================
    // Emergency Contacts
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyContactDetails {

        private String contactName;
        private String relationship;

        private String primaryPhone;
        private String secondaryPhone;

        private String email;
        private String address;

        private Boolean primary;
    }

    // =========================================
    // Asset Details
    // =========================================

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetDetails {

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
    }
}