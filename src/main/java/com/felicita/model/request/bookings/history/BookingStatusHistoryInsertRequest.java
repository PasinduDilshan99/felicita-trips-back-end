package com.felicita.model.request.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatusHistoryInsertRequest {
    private Long bookingId;
    private Long previousStatus;
    private Long newStatus;
}
