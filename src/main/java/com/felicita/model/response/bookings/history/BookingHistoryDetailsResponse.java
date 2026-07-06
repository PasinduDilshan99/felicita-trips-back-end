package com.felicita.model.response.bookings.history;

import com.felicita.model.response.bookings.BookingsBasicDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHistoryDetailsResponse {

    private BookingsBasicDetails bookingsBasicDetails;

    private List<BookingActivityHistory> bookingActivityHistories;

    private List<BookingStatusHistory> bookingStatusHistories;

    private List<BookingAssignmentHistory> bookingAssignmentHistories;

    private List<BookingPaymentHistory> bookingPaymentHistories;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingActivityHistory {

        private String activityType;

        private String description;

        private String updatedBy;

        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingStatusHistory {

        private String previousStatus;

        private String newStatus;

        private String updatedBy;

        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingAssignmentHistory {

        private String previousEmployee;

        private String newEmployee;

        private String updatedBy;

        private LocalDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingPaymentHistory {

        private BigDecimal previousPaidAmount;

        private BigDecimal newPaidAmount;

        private BigDecimal previousDueAmount;

        private BigDecimal newDueAmount;

        private BigDecimal previousRefundAmount;

        private BigDecimal newRefundAmount;

        private String paymentReference;

        private String remarks;

        private String updatedBy;

        private LocalDateTime updatedAt;
    }
}