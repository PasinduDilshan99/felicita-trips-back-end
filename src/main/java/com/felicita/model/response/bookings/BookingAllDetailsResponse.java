package com.felicita.model.response.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAllDetailsResponse {

    private BookingInformation bookingInformation;

    private CustomerInformation customerInformation;

    private TourInformation tourInformation;

    private PackageInformation packageInformation;

    private BookingStatusInformation bookingStatusInformation;

    private AssignmentInformation assignmentInformation;

    private CancellationInformation cancellationInformation;

    private List<ParticipantInformation> participants;

    private List<AccommodationInformation> accommodations;

    private List<TransportationInformation> transportations;

    private List<ActivityInformation> activities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingInformation {

        private Long bookingId;
        private String bookingReference;

        private LocalDate bookingDate;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;

        private Integer totalPersons;

        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal insuranceAmount;
        private BigDecimal finalAmount;

        private Boolean insuranceRequired;

        private String specialRequirements;
        private String dietaryRestrictions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CustomerInformation {

        private Long userId;

        private String username;

        private String firstName;
        private String lastName;

        private String fullName;

        private String email;

        private String mobileNumber;

        private String passportNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourInformation {

        private Long tourId;

        private String tourName;

        private String tourDescription;

        private Integer duration;

        private String startLocation;

        private String endLocation;

        private Double latitude;

        private Double longitude;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageInformation {

        private Long packageId;

        private String packageName;

        private String packageDescription;

        private BigDecimal packageTotalPrice;

        private BigDecimal pricePerPerson;

        private BigDecimal discountPercentage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingStatusInformation {

        private Long bookingStatusId;

        private String bookingStatusName;

        private String bookingStatusDescription;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AssignmentInformation {

        private Long employeeId;

        private Long employeeUserId;

        private String employeeCode;

        private String employeeName;

        private String departmentName;

        private String designationName;

        private String assignMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CancellationInformation {

        private LocalDate cancellationDate;

        private String cancellationReason;

        private String cancellationNotes;

        private BigDecimal refundAmount;

        private String refundStatus;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ParticipantInformation {

        private Long participantId;

        private String firstName;

        private String lastName;

        private String fullName;

        private LocalDate dateOfBirth;

        private String gender;

        private String nationality;

        private String passportNumber;

        private String email;

        private String mobileNumber;

        private String emergencyContactName;

        private String emergencyContactPhone;

        private String emergencyContactRelationship;

        private String medicalConditions;

        private String allergies;

        private Boolean specialAssistanceRequired;

        private String assistanceDetails;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccommodationInformation {

        private Long accommodationId;

        private String hotelName;

        private String roomType;

        private String roomNumber;

        private String confirmationNumber;

        private LocalDate checkInDate;

        private LocalDate checkOutDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransportationInformation {

        private Long transportationId;

        private String transportType;

        private LocalDate departureDate;

        private LocalTime departureTime;

        private LocalDate arrivalDate;

        private LocalTime arrivalTime;

        private String departureLocation;

        private String arrivalLocation;

        private String carrierName;

        private String referenceNumber;

        private String seatNumbers;

        private String vehicleNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityInformation {

        private Long bookingActivityId;

        private Long activityId;

        private String activityName;

        private LocalDate activityDate;

        private LocalTime startTime;

        private LocalTime endTime;

        private Integer numberOfParticipants;

        private BigDecimal pricePerPerson;

        private BigDecimal totalPrice;

        private String status;
    }
}