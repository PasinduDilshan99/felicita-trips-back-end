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
public class ActivityCategoriesStatisticsResponse {

    private List<CategoryActivityCount> categoryActivityCounts;

    private List<CategoryParticipationPerformance> categoryParticipationPerformances;

    private List<CategoryRatingOverview> categoryRatingOverviews;

    private List<CategoryDistribution> categoryDistributions;

    private List<CategoryPrimarySecondaryUsage> categoryPrimarySecondaryUsages;

    private Summary summary;

    // 1. Category Wise Activity Count
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryActivityCount {

        private Long categoryId;
        private String categoryName;
        private Integer totalActivities;
    }

    // 2. Category Participation Performance
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryParticipationPerformance {

        private Long categoryId;
        private String categoryName;
        private Integer totalParticipants;
    }

    // 3. Category Rating Overview
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

    // 4. Category Distribution
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryDistribution {

        private String categoryName;
        private Integer activityCount;
    }

    // 5. Primary vs Secondary Usage
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryPrimarySecondaryUsage {

        private String categoryName;
        private Integer primaryCount;
        private Integer secondaryCount;
    }

    // Summary KPIs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalCategories;
        private Integer totalActivities;
        private String mostUsedCategory;
        private Double overallAverageRating;
    }
}