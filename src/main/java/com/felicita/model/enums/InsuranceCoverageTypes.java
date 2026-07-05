package com.felicita.model.enums;

public enum InsuranceCoverageTypes {

    BASIC("Basic Coverage"),
    STANDARD("Standard Coverage"),
    PREMIUM("Premium Coverage"),
    COMPREHENSIVE("Comprehensive Coverage"),
    THIRD_PARTY("Third Party Only"),
    MEDICAL_ONLY("Medical Only"),
    TRAVEL("Travel Insurance Coverage"),
    VEHICLE("Vehicle Insurance Coverage");

    private final String displayName;

    InsuranceCoverageTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}