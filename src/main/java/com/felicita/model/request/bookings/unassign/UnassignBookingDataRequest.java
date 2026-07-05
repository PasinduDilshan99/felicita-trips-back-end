package com.felicita.model.request.bookings.unassign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnassignBookingDataRequest {

    private String name;
    private String bookingReference;
    private Long bookingStatusId;
    private String customerName;
    private String email;
    private String mobileNumber;
    private Long tourId;
    private Long packageId;
    private Long packageScheduleId;
    private LocalDate bookingDateFrom;
    private LocalDate bookingDateTo;
    private LocalDate travelStartDateFrom;
    private LocalDate travelStartDateTo;
    private Long assignTo;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}