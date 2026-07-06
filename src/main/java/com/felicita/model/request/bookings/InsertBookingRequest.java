package com.felicita.model.request.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertBookingRequest {

    private Long customerId;
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
    private List<BookingItinerary> bookingItineraries;
    private List<BookingNote> bookingNotes;
    private List<BookingPriceBreakDown> priceBreakDowns;
    private BookingInvoice bookingInvoice;

    // ======================================================
    // Participant
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
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
        private Long status;
    }

    // ======================================================
    // Accommodation
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Accommodation {

        private LocalDate checkInDate;
        private LocalDate checkOutDate;

        private String hotelId;
        private String roomType;
        private String roomNumber;
        private String confirmationNumber;
        private Long status;
    }

    // ======================================================
    // Transportation
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Transportation {

        private String transportType;
        private Long vehicleId;
        private LocalDate departureDate;
        private LocalTime departureTime;

        private LocalDate arrivalDate;
        private LocalTime arrivalTime;

        private String departureLocation;
        private String arrivalLocation;

        private String carrierName;
        private String referenceNumber;
        private String seatNumbers;
        private Long status;
    }

    // ======================================================
    // Activities
    // ======================================================

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class Activity {

        private Long activityId;
        private Long activityScheduleId;

        private LocalDate activityDate;
        private LocalTime startTime;
        private LocalTime endTime;

        private Integer numberOfParticipants;

        private BigDecimal pricePerPerson;
        private BigDecimal totalPrice;

        private Long status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingDocuments {
        private String documentName;
        private String documentType;
        private String documentUrl;
        private Double fileSize;
        private String mimiType;
        private Long status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingInsurance{
        private String insuranceProvider;
        private String policyNumber;
        private String coverageType;
        private String coverageDetails;
        private Double premiumAmount;
        private LocalDate policyStartDate;
        private LocalDate policyEndDate;
        private Long status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingItinerary{
        private Integer dayNumber;
        private LocalDate itineraryDate;
        private String title;
        private String description;
        private LocalTime startTime;
        private LocalTime endTime;
        private String location;
        private String includedMeals;
        private Long status;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingNote{
        private String noteType;
        private String noteText;
        private Boolean isImportant;
        private LocalDate followUpDate;
        private Boolean followUpComplete;
        private Long status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingPriceBreakDown{
        private String itemType;
        private String itemName;
        private String itemDescription;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
        private Long status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class BookingInvoice{
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
        private Long status;
    }

}