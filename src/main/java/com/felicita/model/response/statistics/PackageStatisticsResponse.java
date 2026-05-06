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
public class PackageStatisticsResponse {

    private Summary summary;
    private List<PackagePopularity> packagePopularities;
    private List<PackageRatingOverview> packageRatingOverviews;
    private List<PackagePriceDistribution> packagePriceDistributions;
    private List<PackageCapacityUtilization> packageCapacityUtilizations;
    private List<PackageTypeDistribution> packageTypeDistributions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalPackages;
        private Long activePackages;
        private BigDecimal averagePackageRating;
        private Long totalParticipants;
        private BigDecimal averagePackagePrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackagePopularity {

        private Long packageId;
        private String packageName;
        private Integer totalSchedules;
        private Integer totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageRatingOverview {

        private Long packageId;
        private String packageName;
        private BigDecimal averageRating;
        private Integer totalReviews;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackagePriceDistribution {

        private Long packageId;
        private String packageName;
        private BigDecimal totalPrice;
        private BigDecimal pricePerPerson;
        private Integer totalParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageCapacityUtilization {

        private Long packageId;
        private String packageName;
        private Integer minPersonCount;
        private Integer maxPersonCount;
        private BigDecimal averageParticipants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageTypeDistribution {

        private String packageTypeName;
        private Integer totalPackages;
    }
}