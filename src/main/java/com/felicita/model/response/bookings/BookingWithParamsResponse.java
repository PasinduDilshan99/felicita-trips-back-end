package com.felicita.model.response.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingWithParamsResponse {
    private Integer bookingCount;
    private List<BookingsBasicDetails> bookingsBasicDetails;
}
