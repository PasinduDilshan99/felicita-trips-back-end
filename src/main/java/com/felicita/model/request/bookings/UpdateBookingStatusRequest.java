package com.felicita.model.request.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingStatusRequest {
    private Long bookingId;
    private String bookingStatus;
}
