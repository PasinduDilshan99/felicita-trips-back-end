package com.felicita.model.request.bookings.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingHistoryDataRequest {
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private String bookingReference;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
    private LocalDate bookingFrom;
    private LocalDate bookingTo;
    private Long tourId;
    private Long packageId;
    private Long bookingStatusId;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
    private String sortDirection;
}
