package com.felicita.model.enums;

public enum NotificationType {

    // =========================
    // DESTINATION
    // =========================
    DESTINATION_CREATED("Triggered when a new destination is created"),
    DESTINATION_UPDATED("Triggered when a destination is updated"),
    DESTINATION_TERMINATED("Triggered when a destination is terminated or deactivated"),

    // =========================
    // DESTINATION CATEGORY
    // =========================
    DESTINATION_CATEGORY_CREATED("Triggered when a new destination category is created"),
    DESTINATION_CATEGORY_UPDATED("Triggered when a destination category is updated"),
    DESTINATION_CATEGORY_TERMINATED("Triggered when a destination category is terminated or deactivated"),

    // =========================
    // TRENDING DESTINATION
    // =========================
    TRENDING_DESTINATION_CREATED("Triggered when a destination is added to trending destinations"),
    TRENDING_DESTINATION_TERMINATED("Triggered when a destination is removed from trending destinations"),

    // =========================
    // PRIVILEGE
    // =========================
    PRIVILEGE_CREATED("Triggered when a new privilege is created"),
    PRIVILEGE_UPDATED("Triggered when a privilege is updated"),
    PRIVILEGE_TERMINATED("Triggered when a privilege is terminated or deactivated"),

    // =========================
    // ROLE
    // =========================
    ROLE_CREATED("Triggered when a new role is created"),
    ROLE_UPDATED("Triggered when a role is updated"),
    ROLE_TERMINATED("Triggered when a role is terminated or deactivated"),

    // =========================
    // EMPLOYEE
    // =========================
    EMPLOYEE_CREATED("Triggered when a new employee is created"),
    EMPLOYEE_UPDATED("Triggered when an employee is updated"),
    EMPLOYEE_TERMINATED("Triggered when an employee is terminated or deactivated"),

    // =========================
    // ACTIVITY
    // =========================
    ACTIVITY_CREATED("Triggered when a new activity is created"),
    ACTIVITY_UPDATED("Triggered when an activity is updated"),
    ACTIVITY_TERMINATED("Triggered when an activity is terminated or deactivated"),

    ACTIVITY_CATEGORY_TERMINATED(""),
    ACTIVITY_CATEGORY_CREATED(""),
    ACTIVITY_CATEGORY_UPDATED(""),

    // =========================
    // ACTIVITY SCHEDULE
    // =========================
    ACTIVITY_SCHEDULE_CREATED("Triggered when a new activity schedule is created"),
    ACTIVITY_SCHEDULE_UPDATED("Triggered when an activity schedule is updated"),
    ACTIVITY_SCHEDULE_TERMINATED("Triggered when an activity schedule is terminated or deactivated"),

    // =========================
    // TOUR
    // =========================
    TOUR_CREATED("Triggered when a new tour is created"),
    TOUR_UPDATED("Triggered when a tour is updated"),
    TOUR_TERMINATED("Triggered when a tour is terminated or deactivated"),

    TOUR_CATEGORY_TERMINATED(""),
    TOUR_CATEGORY_CREATED(""),
    TOUR_CATEGORY_UPDATED(""),

    TOUR_TYPE_CREATED(""),
    TOUR_TYPE_UPDATED(""),
    TOUR_TYPE_TERMINATED(""),
    // =========================
    // TOUR SCHEDULE
    // =========================
    TOUR_SCHEDULE_CREATED("Triggered when a new tour schedule is created"),
    TOUR_SCHEDULE_UPDATED("Triggered when a tour schedule is updated"),
    TOUR_SCHEDULE_TERMINATED("Triggered when a tour schedule is terminated or deactivated"),

    // =========================
    // PACKAGE
    // =========================
    PACKAGE_CREATED("Triggered when a new package is created"),
    PACKAGE_UPDATED("Triggered when a package is updated"),
    PACKAGE_TERMINATED("Triggered when a package is terminated or deactivated"),

    // =========================
    // PACKAGE SCHEDULE
    // =========================
    PACKAGE_SCHEDULE_CREATED("Triggered when a new package schedule is created"),
    PACKAGE_SCHEDULE_UPDATED("Triggered when a package schedule is updated"),
    PACKAGE_SCHEDULE_TERMINATED("Triggered when a package schedule is terminated or deactivated");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}