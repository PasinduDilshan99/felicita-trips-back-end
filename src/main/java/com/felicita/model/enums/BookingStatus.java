package com.felicita.model.enums;

import lombok.Getter;

@Getter
public enum BookingStatus {

    NEW_INQUIRY("New booking inquiry received"),
    PENDING("Booking is pending review or action"),
    CONTACTED("Customer has been contacted"),
    QUOTATION_SENT("Quotation has been sent to the customer"),
    NEGOTIATION("Booking is under negotiation"),
    CONFIRMED("Booking has been confirmed"),
    PAYMENT_PENDING("Awaiting customer payment"),
    BOOKED("Booking has been successfully booked"),
    COMPLETED("Booking has been completed"),
    CANCELLED("Booking has been cancelled"),
    REJECTED("Booking has been rejected"),
    EXPIRED("Booking inquiry or quotation has expired");

    private final String description;

    BookingStatus(String description) {
        this.description = description;
    }
}