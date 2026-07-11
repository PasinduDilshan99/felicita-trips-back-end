package com.felicita.model.enums;

public enum HeroSectionTypes {

    HOME("hero_section"),
    ABOUT_US("about_us_hero_section"),
    ACTIVITY("activity_hero_section"),
    BLOG("blog_hero_section"),
    CONTACT_US("contact_us_hero_section"),
    DESTINATION("destination_hero_section"),
    FAQ("faq_hero_section"),
    PACKAGE("package_hero_section"),
    SEASONS("seasons_hero_section"),
    TOUR("tour_hero_section"),
    VEHICLE("vehicle_hero_section"),
    VEHICLE_SPECIFICATION("vehicle_specification_hero_section"),
    VEHICLE_TYPES("vehicle_types_hero_section");

    private final String tableName;

    HeroSectionTypes(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}