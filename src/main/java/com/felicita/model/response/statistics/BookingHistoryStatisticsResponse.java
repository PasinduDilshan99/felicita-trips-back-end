package com.felicita.model.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHistoryStatisticsResponse {

    private Summary summary;

    private List<BookingGrowthTrend> bookingGrowthTrends;

    private List<RevenueGrowthTrend> revenueGrowthTrends;

    private List<BookingStatusHistory> bookingStatusHistories;

    private List<CancellationTrend> cancellationTrends;

    private List<HistoricalTopTour> historicalTopTours;

    private List<CustomerReturnStatistics> customerReturnStatistics;

    private List<PeakBookingPeriod> peakBookingPeriods;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalBookings;

        private BigDecimal totalRevenue;

        private LocalDate firstBookingDate;

        private LocalDate latestBookingDate;

        private BigDecimal averageMonthlyBookings;

        private BigDecimal averageMonthlyRevenue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingGrowthTrend {

        private Integer year;

        private Integer month;

        private Long totalBookings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueGrowthTrend {

        private Integer year;

        private Integer month;

        private BigDecimal totalRevenue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingStatusHistory {

        private Integer year;

        private Integer month;

        private Long bookingStatusId;

        private String bookingStatusName;

        private Long totalBookings;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CancellationTrend {

        private Integer year;

        private Integer month;

        private Long totalCancelledBookings;

        private BigDecimal cancellationRate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HistoricalTopTour {

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
    public static class CustomerReturnStatistics {

        private String customerType;

        private Long totalCustomers;

        private BigDecimal percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PeakBookingPeriod {

        private Integer month;

        private String monthName;

        private Long totalBookings;
    }
}