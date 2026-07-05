package com.felicita.model.enums;

public enum RoomTypes {

    SINGLE("Single Room"),
    DOUBLE("Double Room"),
    TWIN("Twin Room"),
    TRIPLE("Triple Room"),
    QUAD("Quad Room"),
    DELUXE("Deluxe Room"),
    SUITE("Suite Room"),
    FAMILY("Family Room"),
    PRESIDENTIAL("Presidential Suite");

    private final String displayName;

    RoomTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}