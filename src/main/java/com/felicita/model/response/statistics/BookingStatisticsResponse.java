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
public class BookingStatisticsResponse {

    private Summary summary;

    private List<MonthlyBookingTrend> monthlyBookingTrends;

    private List<MonthlyRevenueTrend> monthlyRevenueTrends;

    private List<BookingStatusDistribution> bookingStatusDistributions;

    private List<BookingFunnel> bookingFunnels;

    private List<TopTour> topTours;

    private List<PopularActivity> popularActivities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalBookings;

        private BigDecimal totalRevenue;

        private Long activeBookings;

        private Long cancelledBookings;

        private Long totalTravellers;

        private BigDecimal averageBookingValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyBookingTrend {

        private Integer year;

        private Integer month;

        private Long totalBookings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyRevenueTrend {

        private Integer year;

        private Integer month;

        private BigDecimal totalRevenue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingStatusDistribution {

        private Long bookingStatusId;

        private String bookingStatusName;

        private Long totalBookings;

        private BigDecimal percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingFunnel {

        private Integer stepOrder;

        private String bookingStatusName;

        private Long totalBookings;

        private BigDecimal conversionPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TopTour {

        private Long tourId;

        private String tourName;

        private Long totalBookings;

        private Long totalParticipants;

        private BigDecimal totalRevenue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PopularActivity {

        private Long activityId;

        private String activityName;

        private Long totalBookings;

        private Long totalParticipants;

        private BigDecimal totalRevenue;
    }
}