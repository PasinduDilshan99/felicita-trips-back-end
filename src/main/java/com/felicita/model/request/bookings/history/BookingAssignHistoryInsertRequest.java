package com.felicita.model.request.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAssignHistoryInsertRequest {
    private Long bookingId;
    private Long previousAssignEmployee;
    private Long newAssignEmployee;
}
