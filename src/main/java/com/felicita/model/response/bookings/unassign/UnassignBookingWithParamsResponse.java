package com.felicita.model.response.bookings.unassign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnassignBookingWithParamsResponse {
    private Integer unassignBookingCount;
    private List<UnassignBookingBasicDetailsResponse> unassignBookingBasicDetailsResponses;
}
