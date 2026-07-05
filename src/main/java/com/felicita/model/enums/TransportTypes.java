package com.felicita.model.enums;

public enum TransportTypes {

    AIRPLANE("Airplane"),
    TRAIN("Train"),
    BUS("Bus"),
    CAR("Car"),
    VAN("Van"),
    TAXI("Taxi"),
    BOAT("Boat"),
    HELICOPTER("Helicopter"),
    MOTORBIKE("Motorbike");

    private final String displayName;

    TransportTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}