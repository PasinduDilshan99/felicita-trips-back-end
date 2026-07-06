package com.felicita.model.response.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHisotryWithParamsResponse {
    private Integer bookingHistoryCount;
    private List<BookingHistoryBasicDetailsResponse> bookingHistoryBasicDetailsResponses;
}
