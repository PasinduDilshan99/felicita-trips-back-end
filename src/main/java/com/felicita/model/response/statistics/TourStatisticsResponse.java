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
public class TourStatisticsResponse {

    private List<TourPopularity> tourPopularity;
    private List<BookingStatusDistribution> bookingStatusDistribution;
    private List<CategoryPerformance> categoryPerformance;
    private List<TypeDistribution> typeDistribution;
    private List<LocationDistribution> locationDistribution;

    private Summary summary;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourPopularity {

        private Long tourId;
        private String tourName;
        private Integer totalBookings;
    }

    // 2. Booking Status Distribution
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingStatusDistribution {

        private String statusName;
        private Integer totalCount;
    }

    // 3. Category Performance
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryPerformance {

        private Long categoryId;
        private String categoryName;
        private Integer totalTours;
    }

    // 4. Type Distribution
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TypeDistribution {

        private Long typeId;
        private String typeName;
        private Integer totalTours;
    }

    // 5. Location Distribution
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LocationDistribution {

        private String startLocation;
        private Integer totalTours;
    }

    // Summary KPIs
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Integer totalTours;
        private Integer totalBookings;
        private Integer pendingBookings;
        private Double averageRating;
    }
}