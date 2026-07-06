package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingComparisonResult {

    @Builder.Default
    private List<FieldChange> fieldChanges = new ArrayList<>();

    @Builder.Default
    private List<String> changes = new ArrayList<>();

    @Builder.Default
    private boolean hasChanges = false;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    // Financial validation
    private boolean isFinancialCalculationValid;
    private BigDecimal calculatedFinalAmount;
    private BigDecimal differenceAmount;

    // Date validation
    private boolean isDateRangeValid;
    private Integer daysBetweenTravelDates;

    // Participants changes
    @Builder.Default
    private List<ParticipantChange> participantsToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> participantsToRemove = new ArrayList<>();
    @Builder.Default
    private List<ParticipantUpdateChange> participantsToUpdate = new ArrayList<>();

    // Accommodations changes
    @Builder.Default
    private List<AccommodationChange> accommodationsToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> accommodationsToRemove = new ArrayList<>();
    @Builder.Default
    private List<AccommodationUpdateChange> accommodationsToUpdate = new ArrayList<>();

    // Transportations changes
    @Builder.Default
    private List<TransportationChange> transportationsToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> transportationsToRemove = new ArrayList<>();
    @Builder.Default
    private List<TransportationUpdateChange> transportationsToUpdate = new ArrayList<>();

    // Activities changes
    @Builder.Default
    private List<ActivityChange> activitiesToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> activitiesToRemove = new ArrayList<>();
    @Builder.Default
    private List<ActivityUpdateChange> activitiesToUpdate = new ArrayList<>();

    // Documents changes
    @Builder.Default
    private List<DocumentChange> documentsToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> documentsToRemove = new ArrayList<>();
    @Builder.Default
    private List<DocumentUpdateChange> documentsToUpdate = new ArrayList<>();

    // Insurance changes
    private InsuranceChange insuranceToAdd;
    private Long insuranceToRemove;
    private InsuranceUpdateChange insuranceToUpdate;

    // Itineraries changes
    @Builder.Default
    private List<ItineraryChange> itinerariesToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> itinerariesToRemove = new ArrayList<>();
    @Builder.Default
    private List<ItineraryUpdateChange> itinerariesToUpdate = new ArrayList<>();

    // Notes changes
    @Builder.Default
    private List<NoteChange> notesToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> notesToRemove = new ArrayList<>();
    @Builder.Default
    private List<NoteUpdateChange> notesToUpdate = new ArrayList<>();

    // Price breakdown changes
    @Builder.Default
    private List<PriceBreakDownChange> priceBreakDownsToAdd = new ArrayList<>();
    @Builder.Default
    private List<Long> priceBreakDownsToRemove = new ArrayList<>();
    @Builder.Default
    private List<PriceBreakDownUpdateChange> priceBreakDownsToUpdate = new ArrayList<>();

    // Invoice changes
    private InvoiceChange invoiceToAdd;
    private Long invoiceToRemove;
    private InvoiceUpdateChange invoiceToUpdate;

    // Status tracking
    private Long oldBookingStatusId;
    private Long newBookingStatusId;
    private String oldBookingStatusName;
    private String newBookingStatusName;

    private String changedBy;
    private Long changedByUserId;
    private String changeTimestamp;

    // Inner classes for all change types
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldChange {
        private String fieldName;
        private Object oldValue;
        private Object newValue;
        private String fieldLabel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ParticipantChange {
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String email;
        private String mobileNumber;
        private String passportNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ParticipantUpdateChange extends ParticipantChange {
        private Long participantId;
        private String oldFirstName;
        private String oldLastName;
        private LocalDate oldDateOfBirth;
        private String oldEmail;
        private String oldMobileNumber;
        private String oldPassportNumber;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class AccommodationChange {
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
    @SuperBuilder
    public static class AccommodationUpdateChange extends AccommodationChange {
        private Long accommodationId;
        private String oldHotelName;
        private String oldRoomType;
        private String oldRoomNumber;
        private String oldConfirmationNumber;
        private LocalDate oldCheckInDate;
        private LocalDate oldCheckOutDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class TransportationChange {
        private String transportType;
        private String carrierName;
        private String referenceNumber;
        private String seatNumbers;
        private LocalDate departureDate;
        private LocalTime departureTime;
        private LocalDate arrivalDate;
        private LocalTime arrivalTime;
        private String departureLocation;
        private String arrivalLocation;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class TransportationUpdateChange extends TransportationChange {
        private Long transportationId;
        private String oldTransportType;
        private String oldCarrierName;
        private String oldReferenceNumber;
        private String oldSeatNumbers;
        private LocalDate oldDepartureDate;
        private LocalTime oldDepartureTime;
        private LocalDate oldArrivalDate;
        private LocalTime oldArrivalTime;
        private String oldDepartureLocation;
        private String oldArrivalLocation;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ActivityChange {
        private Long activityId;
        private String activityName;
        private LocalDate activityDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer numberOfParticipants;
        private BigDecimal pricePerPerson;
        private BigDecimal totalPrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ActivityUpdateChange extends ActivityChange {
        private Long bookingActivityId;
        private Long oldActivityId;
        private String oldActivityName;
        private LocalDate oldActivityDate;
        private LocalTime oldStartTime;
        private LocalTime oldEndTime;
        private Integer oldNumberOfParticipants;
        private BigDecimal oldPricePerPerson;
        private BigDecimal oldTotalPrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DocumentChange {
        private String documentName;
        private String documentType;
        private String documentUrl;
        private Double fileSize;
        private String mimeType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class DocumentUpdateChange extends DocumentChange {
        private Long documentId;
        private String oldDocumentName;
        private String oldDocumentType;
        private String oldDocumentUrl;
        private Double oldFileSize;
        private String oldMimeType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class InsuranceChange {
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
    @SuperBuilder
    public static class InsuranceUpdateChange extends InsuranceChange {
        private Long insuranceId;
        private String oldInsuranceProvider;
        private String oldPolicyNumber;
        private String oldCoverageType;
        private String oldCoverageDetails;
        private Double oldPremiumAmount;
        private LocalDate oldPolicyStartDate;
        private LocalDate oldPolicyEndDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ItineraryChange {
        private Integer dayNumber;
        private LocalDate itineraryDate;
        private String title;
        private String description;
        private LocalTime startTime;
        private LocalTime endTime;
        private String location;
        private String includedMeals;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ItineraryUpdateChange extends ItineraryChange {
        private Long itineraryId;
        private Integer oldDayNumber;
        private LocalDate oldItineraryDate;
        private String oldTitle;
        private String oldDescription;
        private LocalTime oldStartTime;
        private LocalTime oldEndTime;
        private String oldLocation;
        private String oldIncludedMeals;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class NoteChange {
        private String noteType;
        private String noteText;
        private Boolean isImportant;
        private LocalDate followUpDate;
        private Boolean followUpComplete;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class NoteUpdateChange extends NoteChange {
        private Long noteId;
        private String oldNoteType;
        private String oldNoteText;
        private Boolean oldIsImportant;
        private LocalDate oldFollowUpDate;
        private Boolean oldFollowUpComplete;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class PriceBreakDownChange {
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
    @SuperBuilder
    public static class PriceBreakDownUpdateChange extends PriceBreakDownChange {
        private Long priceBreakDownId;
        private String oldItemType;
        private String oldItemName;
        private String oldItemDescription;
        private Integer oldQuantity;
        private Double oldUnitPrice;
        private Double oldTotalPrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class InvoiceChange {
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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class InvoiceUpdateChange extends InvoiceChange {
        private Long invoiceId;
        private LocalDate oldDueDate;
        private Double oldSubTotal;
        private Double oldTaxAmount;
        private Double oldTotalAmount;
        private Double oldDiscountAmount;
        private Double oldInsuranceAmount;
        private Double oldAmountPaid;
        private Double oldBalanceDue;
        private String oldBillingFullName;
        private String oldBillingAddress;
        private String oldBillingEmail;
        private String oldBillingPhone;
    }
}