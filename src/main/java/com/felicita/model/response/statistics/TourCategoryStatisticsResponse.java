package com.felicita.model.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourCategoryStatisticsResponse {

    private Summary summary;
    private List<CategoryDistribution> categoryDistribution;
    private List<CategoryBookingPerformance> categoryBookingPerformance;
    private List<CategoryRatingOverview> categoryRatingOverview;
    private List<CategoryPrimarySecondaryUsage> categoryPrimarySecondaryUsage;
    private List<CategoryParticipationImpact> categoryParticipationImpact;

    // =========================
    // 1. SUMMARY KPI
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalCategories;
        private Integer activeCategories;
        private Double averageRating;
        private Integer totalBookings;
    }

    // =========================
    // 2. CATEGORY DISTRIBUTION
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryDistribution {

        private String categoryName;
        private Integer totalTours;
    }

    // =========================
    // 3. CATEGORY BOOKING PERFORMANCE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryBookingPerformance {

        private Long categoryId;
        private String categoryName;
        private Integer totalBookings;
    }

    // =========================
    // 4. CATEGORY RATING OVERVIEW
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryRatingOverview {

        private Long categoryId;
        private String categoryName;
        private Double averageRating;
        private Integer totalReviews;
    }

    // =========================
    // 5. PRIMARY VS SECONDARY USAGE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryPrimarySecondaryUsage {

        private String categoryName;
        private Integer primaryUsage;
        private Integer secondaryUsage;
    }

    // =========================
    // 6. PARTICIPATION IMPACT
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryParticipationImpact {

        private Long categoryId;
        private String categoryName;
        private Integer totalParticipants;
    }
}