package com.felicita.model.request.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingRequest {

    private Long bookingId;
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
    private Long assignTo;
    private String assignMessage;

    private List<InsertBookingRequest.Participant> addParticipants;
    private List<Long> removeParticipants;
    private List<UpdateParticipant> updateParticipants;

    private List<InsertBookingRequest.Accommodation> addAccommodations;
    private List<Long> removeAccommodations;
    private List<UpdateAccommodation> updateAccommodations;

    private List<InsertBookingRequest.Transportation> addTransportations;
    private List<Long> removeTransportations;
    private List<UpdateTransportation> updateTransportations;

    private List<InsertBookingRequest.Activity> addActivities;
    private List<Long> removeActivities;
    private List<UpdateActivity> updateActivities;

    private List<InsertBookingRequest.BookingDocuments> addDocuments;
    private List<Long> removeDocuments;
    private List<UpdateBookingDocuments> updateDocuments;

    private InsertBookingRequest.BookingInsurance addBookingInsurance;
    private Long removeBookingInsurance;
    private UpdateBookingInsurance updateBookingInsurance;

    private List<InsertBookingRequest.BookingItinerary> addBookingItineraries;
    private List<Long> removeBookingItineraries;
    private List<UpdateBookingItinerary> updateBookingItineraries;

    private List<InsertBookingRequest.BookingNote> addBookingNotes;
    private List<Long> removeBookingNotes;
    private List<UpdateBookingNote> updateBookingNotes;

    private List<InsertBookingRequest.BookingPriceBreakDown> addPriceBreakDowns;
    private List<Long> removePriceBreakDowns;
    private List<UpdateBookingPriceBreakDown> updatePriceBreakDowns;

    private InsertBookingRequest.BookingInvoice addBookingInvoice;
    private Long removeBookingInvoice;
    private UpdateBookingInvoice updateBookingInvoice;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateParticipant extends InsertBookingRequest.Participant {
        private Long participantId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateAccommodation extends InsertBookingRequest.Accommodation {
        private Long accommodationId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateTransportation extends InsertBookingRequest.Transportation {
        private Long transportationId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateActivity extends InsertBookingRequest.Activity {
        private Long bookingActivityId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingDocuments extends InsertBookingRequest.BookingDocuments {
        private Long documentId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingInsurance extends InsertBookingRequest.BookingInsurance {
        private Long insuranceId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingItinerary extends InsertBookingRequest.BookingItinerary{
        private Long itineraryId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingNote extends InsertBookingRequest.BookingNote{
        private Long noteId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingPriceBreakDown extends InsertBookingRequest.BookingPriceBreakDown {
        private Long priceBreakDownId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateBookingInvoice extends InsertBookingRequest.BookingInvoice{
        private Long invoiceId;
    }

}
