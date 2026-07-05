package com.felicita.model.enums;

public enum NoteTypes {

    GENERAL("General Note"),
    CUSTOMER_REQUEST("Customer Request"),
    INTERNAL("Internal Note"),
    SPECIAL_INSTRUCTION("Special Instruction"),
    PAYMENT_NOTE("Payment Note"),
    BOOKING_NOTE("Booking Note"),
    SYSTEM_NOTE("System Generated Note"),
    WARNING("Warning"),
    IMPORTANT("Important");

    private final String displayName;

    NoteTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}