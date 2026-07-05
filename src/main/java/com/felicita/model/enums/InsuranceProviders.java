package com.felicita.model.enums;

public enum InsuranceProviders {

    AIA("AIA Insurance"),
    ALLIANZ("Allianz Insurance"),
    SRI_LANKA_INSURANCE("Sri Lanka Insurance"),
    CEYLINCO("Ceylinco Insurance"),
    HNB_GENERAL("HNB General Insurance"),
    UNION_ASSURANCE("Union Assurance"),
    FAIRFIRST("Fairfirst Insurance"),
    CONTINENTAL("Continental Insurance");

    private final String displayName;

    InsuranceProviders(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}