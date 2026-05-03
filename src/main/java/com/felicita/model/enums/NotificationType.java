package com.felicita.model.enums;

public enum NotificationType {

    DESTINATION_CREATED("Triggered when a new destination is created"),
    DESTINATION_UPDATED("Triggered when a destination is updated"),
    DESTINATION_TERMINATED("Triggered when a destination is terminated or deactivated"),

    DESTINATION_CATEGORY_CREATED("Triggered when a new destination category is created"),
    DESTINATION_CATEGORY_UPDATED("Triggered when a destination category is updated"),
    DESTINATION_CATEGORY_TERMINATED("Triggered when a destination category is terminated or deactivated"),

    TRENDING_DESTINATION_CREATED("Triggered when a destination is added to trending destinations"),
    TRENDING_DESTINATION_TERMINATED("Triggered when a destination is removed from trending destinations"),

    PRIVILEGE_CREATED("Triggered when a new privilege is created"),
    PRIVILEGE_UPDATED("Triggered when a privilege is updated"),
    PRIVILEGE_TERMINATED("Triggered when a privilege is terminated or deactivated"),

    ROLE_CREATED("Triggered when a new role is created"),
    ROLE_UPDATED("Triggered when a role is updated"),
    ROLE_TERMINATED("Triggered when a role is terminated or deactivated"),

    EMPLOYEE_CREATED(""),

    ACTIVITY_CREATED(""),
    ACTIVITY_UPDATED(""),
    ACTIVITY_TERMINATED(""),

    TOUR_CREATED(""),
    TOUR_UPDATED(""),
    TOUR_TERMINATED("");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}