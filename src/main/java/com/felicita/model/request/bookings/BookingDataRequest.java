package com.felicita.model.request.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDataRequest {
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private String bookingReference;
    private Double discountAmount;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
    private LocalDate bookingFrom;
    private LocalDate bookingTo;
    private Integer pageSize;
    private Integer pageNumber;
    private Long bookingStatusId;
    private Long tourId;
    private Long packageId;
    private Long assignTo;
    private String sortBy;
    private String sortDirection;
}
