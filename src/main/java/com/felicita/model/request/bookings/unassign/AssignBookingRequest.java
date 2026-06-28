package com.felicita.model.request.bookings.unassign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignBookingRequest {
    private Long bookingId;
    private Long assignTo;
    private String assignUsername;
    private String assignMessage;
}
