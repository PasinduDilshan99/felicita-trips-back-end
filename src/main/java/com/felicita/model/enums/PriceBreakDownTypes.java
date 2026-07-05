package com.felicita.model.enums;

public enum PriceBreakDownTypes {

    ROOM_CHARGE("Room Charge"),
    TAX("Tax"),
    SERVICE_CHARGE("Service Charge"),
    DISCOUNT("Discount"),
    ADDITIONAL_SERVICE("Additional Service"),
    MEAL_CHARGE("Meal Charge"),
    TRANSPORT_CHARGE("Transport Charge"),
    INSURANCE_CHARGE("Insurance Charge"),
    OTHER("Other Charges");

    private final String displayName;

    PriceBreakDownTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}