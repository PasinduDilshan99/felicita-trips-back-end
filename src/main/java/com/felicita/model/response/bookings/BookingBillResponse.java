package com.felicita.model.response.bookings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingBillResponse {

    private Integer bookingId;
    private String bookingReference;
    private LocalDate bookingDate;

    private Customer customer;
    private TourDetails tour;
    private PackageDetails packageDetails;
    private BillingSummary billingSummary;

    private List<Participant> participants;
    private List<PriceItem> priceBreakdown;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private Integer userId;
        private String fullName;
        private String email;
        private String mobileNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TourDetails {

        private Integer tourId;
        private String tourName;
        private Integer duration;
        private String startLocation;
        private String endLocation;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
        private Integer totalPersons;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackageDetails {
        private Integer packageId;
        private String packageName;
        private String scheduleName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {
        private String firstName;
        private String lastName;
        private String passportNumber;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceItem {
        private String itemType;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingSummary {

        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal insuranceAmount;
        private BigDecimal finalAmount;

        private BigDecimal paidAmount;
        private BigDecimal dueAmount;
    }

    // Add this inner class to BookingBillResponse if needed
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingBasicInfo {
        private Integer bookingId;
        private String bookingReference;
        private LocalDate bookingDate;
    }
}