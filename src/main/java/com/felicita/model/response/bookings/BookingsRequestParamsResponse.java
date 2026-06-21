package com.felicita.model.response.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsRequestParamsResponse {
    private Long minPrice;
    private Long maxPrice;
    private Double minDiscountAmount;
    private Double maxDiscountAmount;
    private LocalDate minBookingDate;
    private LocalDate maxBookingDate;
    private LocalDate minTravelStartDate;
    private LocalDate maxTravelStartDate;
    private List<IdAndName> bookingStatuses;
    private List<IdAndName> tours;
    private List<IdAndName> packages;
    private List<IdAndName> assignEmployees;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class IdAndName{
        private Long id;
        private String name;
    }

}
