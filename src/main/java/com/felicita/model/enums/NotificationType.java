package com.felicita.model.enums;

public enum NotificationType {

    DESTINATION_CREATED("Triggered when a new destination is created"),
    DESTINATION_UPDATED("Triggered when a destination is updated"),
    DESTINATION_TERMINATED("Triggered when a destination is terminated or deactivated"),
    DESTINATION_CATEGORY_CREATED("Triggered when a new destination category is created"),
    DESTINATION_CATEGORY_UPDATED("Triggered when a destination category is updated"),
    DESTINATION_CATEGORY_TERMINATED("Triggered when a destination category is terminated or deactivated");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}