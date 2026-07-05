package com.felicita.model.request.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingActivityHistoryInsertRequest {
    private Long bookingId;
    private String activityType;
    private String description;
}
