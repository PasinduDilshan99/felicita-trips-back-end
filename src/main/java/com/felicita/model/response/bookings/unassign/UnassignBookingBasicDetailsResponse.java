package com.felicita.model.response.bookings.unassign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnassignBookingBasicDetailsResponse {

    private Booking booking;
    private Customer customer;
    private Tour tour;
    private PackageDetails packageDetails;
    private Schedule schedule;
    private Financial financial;
    private Assignment assignment;
    private Status status;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Booking {
        private Long bookingId;
        private String bookingReference;
        private LocalDate bookingDate;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
        private Integer totalPersons;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNumber;
        private String nic;
        private String passportNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tour {
        private Long tourId;
        private String tourName;
        private String description;
        private Integer duration;
        private String startLocation;
        private String endLocation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageDetails {
        private Long packageId;
        private String packageName;
        private BigDecimal totalPrice;
        private BigDecimal pricePerPerson;
        private Integer minPersonCount;
        private Integer maxPersonCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Schedule {
        private Long packageScheduleId;
        private String scheduleName;
        private LocalDate assumeStartDate;
        private LocalDate assumeEndDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Financial {
        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal insuranceAmount;
        private BigDecimal finalAmount;
        private BigDecimal paidAmount;
        private BigDecimal dueAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Assignment {
        private Long assignedTo;
        private String assignedUser;
        private String assignMessage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private Integer bookingStatusId;
        private String bookingStatus;
    }
}