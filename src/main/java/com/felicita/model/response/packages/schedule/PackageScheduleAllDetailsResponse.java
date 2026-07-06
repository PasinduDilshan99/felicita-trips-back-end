package com.felicita.model.response.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleAllDetailsResponse {

    // =====================================================
    // PACKAGE SCHEDULE DETAILS
    // =====================================================

    private Long packageScheduleId;
    private String packageScheduleName;

    private String assumeStartDate;
    private String assumeEndDate;

    private Integer durationStart;
    private Integer durationEnd;

    private String specialNote;
    private String description;

    private String scheduleStatus;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    // =====================================================
    // PACKAGE DETAILS
    // =====================================================

    private Long packageId;
    private String packageName;
    private String packageDescription;

    private BigDecimal totalPrice;
    private BigDecimal discountPercentage;
    private BigDecimal pricePerPerson;

    private Integer minPersonCount;
    private Integer maxPersonCount;

    private String color;
    private String hoverColor;

    private String packageStatus;

    private Timestamp packageCreatedAt;
    private Timestamp packageUpdatedAt;

    // =====================================================
    // PACKAGE TYPE DETAILS
    // =====================================================

    private Long packageTypeId;
    private String packageTypeName;
    private String packageTypeDescription;

    // =====================================================
    // TOUR DETAILS
    // =====================================================

    private Long tourId;
    private String tourName;
    private String tourDescription;

    private Integer tourDuration;

    private String startLocation;
    private String endLocation;

    private String season;

    private String tourStatus;

    // =====================================================
    // TOUR SCHEDULE DETAILS
    // =====================================================

    private Long tourScheduleId;
    private String tourScheduleName;

    // =====================================================
    // PACKAGE FEATURES
    // =====================================================

    private List<PackageFeatureDetails> features;

    // =====================================================
    // PACKAGE DAY ACCOMMODATIONS
    // =====================================================

    private List<PackageDayAccommodationDetails> accommodations;

    // =====================================================
    // INNER CLASSES
    // =====================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageFeatureDetails {

        private Long featureId;

        private String name;
        private String value;

        private String description;

        private String specialNote;

        private String color;
        private String hoverColor;

        private String status;

        private Timestamp createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageDayAccommodationDetails {

        private Long accommodationId;

        private Integer dayNumber;

        // ==========================================
        // MEALS
        // ==========================================

        private Boolean breakfast;
        private String breakfastDescription;

        private Boolean lunch;
        private String lunchDescription;

        private Boolean dinner;
        private String dinnerDescription;

        private Boolean morningTea;
        private String morningTeaDescription;

        private Boolean eveningTea;
        private String eveningTeaDescription;

        private Boolean snacks;
        private String snackNote;

        // ==========================================
        // HOTEL / TRANSPORT
        // ==========================================

        private Long hotelId;
        private String hotelName;

        private Long transportId;
        private String transportName;

        // ==========================================
        // PRICE DETAILS
        // ==========================================

        private Integer localPrice;
        private Integer price;
        private Integer discount;
        private Integer serviceCharge;
        private Integer tax;
        private Integer extraCharge;

        private String extraChargeNote;

        private Integer transportCost;

        // ==========================================
        // EXTRA
        // ==========================================

        private String otherNotes;

        private String status;

        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}