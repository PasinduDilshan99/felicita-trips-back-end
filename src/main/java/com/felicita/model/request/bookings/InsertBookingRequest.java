package com.felicita.model.request.bookings;

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
public class InsertBookingRequest {

    private Long tourId;
    private Long packageId;
    private Long packageScheduleId;

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

    private Long bookingStatusId;

    private String specialRequirements;
    private String dietaryRestrictions;

    // Assignment
    private Long assignTo;
    private String assignMessage;


    // Child Collections
    private List<Participant> participants;
    private List<Accommodation> accommodations;
    private List<Transportation> transportations;
    private List<Activity> activities;
    private List<BookingDocuments> documents;
    private BookingInsurance bookingInsurance;
    private List<BookingNote> bookingNotes;
    private List<BookingPriceBreakDown> priceBreakDowns;
    private BookingInvoice bookingInvoice;

    // ======================================================
    // Participant
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Participant {

        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;

        private Long genderId;
        private String passportNumber;
        private Long nationalityCountryId;

        private String email;
        private String mobileNumber;

        private String emergencyContactName;
        private String emergencyContactPhone;
        private String emergencyContactRelationship;

        private String medicalConditions;
        private String allergies;

        private Boolean specialAssistanceRequired;
        private String assistanceDetails;

        private Integer roomSharingWith;
    }

    // ======================================================
    // Accommodation
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Accommodation {

        private LocalDate checkInDate;
        private LocalDate checkOutDate;

        private String hotelId;
        private String roomType;
        private String roomNumber;
        private String confirmationNumber;
    }

    // ======================================================
    // Transportation
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Transportation {

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
    }

    // ======================================================
    // Activities
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Activity {

        private Long activityId;
        private Long activityScheduleId;

        private LocalDate activityDate;
        private LocalTime startTime;
        private LocalTime endTime;

        private Integer numberOfParticipants;

        private BigDecimal pricePerPerson;
        private BigDecimal totalPrice;

        private Long statusId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingDocuments {
        private String documentName;
        private String documentType;
        private String documentUrl;
        private String fileSize;
        private String mimiType;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingInsurance{
        private String insuranceProvider;
        private String policyNumber;
        private String coverageType;
        private String coverageDetails;
        private Double premiumAmount;
        private LocalDate policyStartDate;
        private LocalDate policyEndDate;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingNote{
        private String noteType;
        private String noteText;
        private Boolean isImportant;
        private LocalDate followUpDate;
        private Boolean followUpComplete;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingPriceBreakDown{
        private String itemType;
        private String itemName;
        private String itemDescription;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookingInvoice{
        private String invoiceNumber;
        private LocalDate invoiceDate;
        private LocalDate dueDate;
        private Double subTotal;
        private Double taxAmount;
        private Double totalAmount;
        private Double discountAmount;
        private Double insuranceAmount;
        private Double amountPaid;
        private Double balanceDue;
        private String billingFullName;
        private String billingAddress;
        private String billingEmail;
        private String billingPhone;
        private String status;
    }

}