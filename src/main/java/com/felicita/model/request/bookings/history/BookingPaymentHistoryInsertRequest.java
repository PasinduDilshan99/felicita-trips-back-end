package com.felicita.model.request.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingPaymentHistoryInsertRequest {
    private Long bookingId;
    private Double previousPaidAmount;
    private Double newPaidAmount;
    private Double previousDueAmount;
    private Double newDueAmount;
    private Double previousRefundAmount;
    private Double newRefundAmount;
    private String paymentReference;
    private String remarks;
}
