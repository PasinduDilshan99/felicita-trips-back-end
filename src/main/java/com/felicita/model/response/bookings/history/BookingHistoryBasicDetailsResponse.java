package com.felicita.model.response.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHistoryBasicDetailsResponse {

    private Long bookingId;
    private String bookingReference;

    private String customerName;

    private String tourName;
    private String packageName;
    private Integer totalPersons;

    private LocalDate bookingDate;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;

    private BigDecimal finalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;
    private BigDecimal refundAmount;

    private String bookingStatus;
    private String assignedEmployee;

    private HistorySummary history;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HistorySummary {

        private String lastActivity;
        private String lastActivityDescription;

        private String lastUpdatedBy;
        private LocalDateTime lastUpdatedAt;

        private Integer totalActivities;
        private Integer totalStatusChanges;
        private Integer totalAssignmentChanges;
        private Integer totalPaymentUpdates;
    }
}