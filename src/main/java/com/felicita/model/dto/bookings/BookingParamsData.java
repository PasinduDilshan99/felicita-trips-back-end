package com.felicita.model.dto.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingParamsData {

    private Long minPrice;
    private Long maxPrice;
    private Double minDiscountAmount;
    private Double maxDiscountAmount;
    private LocalDate minBookingDate;
    private LocalDate maxBookingDate;
    private LocalDate minTravelStartDate;
    private LocalDate maxTravelStartDate;
}