package com.felicita.model.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTypeStatisticsResponse {

    private Summary summary;
    private List<TypeDistribution> typeDistributions;
    private List<TypeRevenuePerformance> typeRevenuePerformances;
    private List<TypeParticipationImpact> typeParticipationImpacts;
    private List<TypePrimarySecondaryUsage> typePrimarySecondaryUsages;
    private List<TypeBookingPerformance> typeBookingPerformances;
    private List<TypeRatingOverview> typeRatingOverviews;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalPackageTypes;
        private Long mostUsedTypeCount;
        private String mostPopularTypeName;
        private BigDecimal highestRatedTypeRating;
        private BigDecimal highestRevenueTypeValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeDistribution {

        private Long typeId;
        private String typeName;
        private Long totalPackages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeBookingPerformance {

        private Long typeId;
        private String typeName;
        private Long totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeRatingOverview {

        private Long typeId;
        private String typeName;
        private BigDecimal averageRating;
        private Long totalReviews;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeParticipationImpact {

        private String typeName;
        private String month;
        private Long totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypePrimarySecondaryUsage {

        private Long typeId;
        private String typeName;
        private Long primaryCount;
        private Long secondaryCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeRevenuePerformance {

        private Long typeId;
        private String typeName;
        private BigDecimal totalRevenue;
        private BigDecimal averagePackagePrice;
    }
}