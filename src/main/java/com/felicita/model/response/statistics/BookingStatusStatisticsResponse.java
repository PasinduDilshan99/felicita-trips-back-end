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
public class BookingStatusStatisticsResponse {

    private Summary summary;

    private List<StatusDistribution> statusDistributions;

    private List<StatusFunnel> statusFunnels;

    private List<StatusTrend> statusTrends;

    private List<DropOffStatistics> dropOffStatistics;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Summary {

        private Long totalStatuses;

        private Long activeStatuses;

        private String mostUsedStatus;

        private Long mostUsedStatusCount;

        private BigDecimal inquiryToBookedPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusDistribution {

        private Long bookingStatusId;

        private String bookingStatusName;

        private Long totalBookings;

        private BigDecimal percentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusFunnel {

        private Integer stepOrder;

        private String bookingStatusName;

        private Long totalBookings;

        private BigDecimal conversionPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusTrend {

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
    public static class DropOffStatistics {

        private String bookingStatusName;

        private Long totalBookings;

        private BigDecimal percentage;
    }
}