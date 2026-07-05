package com.felicita.model.enums;

public enum DocumentTypes {

    PASSPORT("Passport"),
    NATIONAL_ID("National ID Card"),
    DRIVING_LICENSE("Driving License"),
    VISA("Visa"),
    BIRTH_CERTIFICATE("Birth Certificate"),
    MARRIAGE_CERTIFICATE("Marriage Certificate"),
    RESIDENCE_PERMIT("Residence Permit"),
    STUDENT_ID("Student ID"),
    OTHER("Other");

    private final String displayName;

    DocumentTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}