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
public class TourTypeStatisticsResponse {

    private Summary summary;
    private List<TypeDistribution> typeDistribution;
    private List<TypeBookingPerformance> typeBookingPerformance;
    private List<TypeRatingOverview> typeRatingOverview;
    private List<TypeParticipationImpact> typeParticipationImpact;
    private List<TypePrimarySecondaryUsage> typePrimarySecondaryUsage;

    // =========================
    // 1. SUMMARY KPI
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalTypes;
        private Integer activeTypes;
        private Double averageRating;
        private Integer totalBookings;
    }

    // =========================
    // 2. TYPE DISTRIBUTION
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeDistribution {

        private String typeName;
        private Integer totalTours;
    }

    // =========================
    // 3. BOOKING PERFORMANCE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeBookingPerformance {

        private Long typeId;
        private String typeName;
        private Integer totalBookings;
    }

    // =========================
    // 4. RATING OVERVIEW
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeRatingOverview {

        private Long typeId;
        private String typeName;
        private Double averageRating;
        private Integer totalReviews;
    }

    // =========================
    // 5. PARTICIPATION IMPACT
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeParticipationImpact {

        private Long typeId;
        private String typeName;
        private Integer totalParticipants;
    }

    // =========================
    // 6. PRIMARY VS SECONDARY USAGE
    // =========================
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypePrimarySecondaryUsage {

        private String typeName;
        private Integer primaryUsage;
        private Integer secondaryUsage;
    }
}