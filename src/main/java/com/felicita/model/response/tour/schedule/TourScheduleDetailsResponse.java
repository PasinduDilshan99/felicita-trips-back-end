package com.felicita.model.response.tour.schedule;

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
public class TourScheduleDetailsResponse {

    private Long tourScheduleId;
    private String tourScheduleName;

    private String assumeStartDate;
    private String assumeEndDate;

    private Integer durationStart;
    private Integer durationEnd;

    private String specialNote;
    private String description;

    private String scheduleStatus;

    private Timestamp createdAt;
    private Timestamp updatedAt;


    private Long tourId;
    private String tourName;
    private String tourDescription;

    private Integer tourDuration;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String startLocation;
    private String endLocation;

    private Long seasonId;
    private String season;

    private String tourStatus;

    private String assignMessage;

    private Timestamp tourCreatedAt;
    private Timestamp tourUpdatedAt;

    // =====================================================
    // TOUR CATEGORIES
    // =====================================================

    private List<TourCategoryDetails> categories;

    // =====================================================
    // TOUR TYPES
    // =====================================================

    private List<TourTypeDetails> types;

    // =====================================================
    // TOUR IMAGES
    // =====================================================

    private List<TourImageDetails> images;

    // =====================================================
    // TOUR DAY ACCOMMODATIONS
    // =====================================================

    private List<TourDayAccommodationDetails> accommodations;

    // =====================================================
    // INNER CLASSES
    // =====================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourCategoryDetails {

        private Long categoryId;
        private String categoryName;
        private String description;

        private Boolean primaryCategory;

        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourTypeDetails {

        private Long typeId;
        private String typeName;
        private String description;

        private Boolean primaryType;

        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourImageDetails {

        private Long imageId;

        private String name;
        private String description;

        private String imageUrl;

        private String status;

        private Timestamp createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourDayAccommodationDetails {

        private Long accommodationId;

        private Integer day;

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
        // EXTRA
        // ==========================================

        private String otherNotes;

        private Timestamp createdAt;
        private Timestamp updatedAt;
    }
}