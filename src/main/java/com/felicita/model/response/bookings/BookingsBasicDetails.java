package com.felicita.model.response.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsBasicDetails {

    private Long bookingId;
    private String bookingReference;
    private LocalDate bookingDate;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;

    private Long userId;
    private String username;
    private String customerName;
    private String email;
    private String mobileNumber;

    private Long tourId;
    private String tourName;
    private Integer tourDuration;
    private String startLocation;
    private String endLocation;

    private Long packageId;
    private String packageName;

    private Integer totalPersons;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal insuranceAmount;
    private BigDecimal finalAmount;

    private Boolean insuranceRequired;

    private Long bookingStatusId;
    private String bookingStatusName;

    private Long assignedEmployeeId;
    private String assignedEmployeeName;
    private String assignMessage;

    private LocalDate cancellationDate;
    private BigDecimal refundAmount;
    private String specialRequirements;
    private String dietaryRestrictions;

}