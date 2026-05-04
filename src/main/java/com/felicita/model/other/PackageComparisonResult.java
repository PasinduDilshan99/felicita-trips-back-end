package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageComparisonResult {

    // Basic details changes
    private List<FieldChange> basicDetailsChanges;

    // Images changes
    private List<Long> imageIdsToRemove;
    private List<PackageImageChange> imagesToAdd;
    private List<PackageImageChange> imagesToUpdate;

    // Features changes
    private List<PackageFeatureChange> featuresToAdd;
    private List<Long> featureIdsToRemove;
    private List<PackageFeatureChange> featuresToUpdate;

    // Day Accommodations changes
    private List<PackageDayAccommodationChange> dayAccommodationsToAdd;
    private List<Long> dayAccommodationIdsToRemove;
    private List<PackageDayAccommodationChange> dayAccommodationsToUpdate;

    // Inclusions changes
    private List<PackageInclusionChange> inclusionsToAdd;
    private List<Long> inclusionIdsToRemove;
    private List<PackageInclusionChange> inclusionsToUpdate;

    // Exclusions changes
    private List<PackageExclusionChange> exclusionsToAdd;
    private List<Long> exclusionIdsToRemove;
    private List<PackageExclusionChange> exclusionsToUpdate;

    // Conditions changes
    private List<PackageConditionChange> conditionsToAdd;
    private List<Long> conditionIdsToRemove;
    private List<PackageConditionChange> conditionsToUpdate;

    // Travel Tips changes
    private List<PackageTravelTipChange> travelTipsToAdd;
    private List<Long> travelTipIdsToRemove;
    private List<PackageTravelTipChange> travelTipsToUpdate;

    // Summary
    private boolean hasChanges;
    private String summary;

    // Inner classes for change details
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldChange {
        private String fieldName;
        private Object oldValue;
        private Object newValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageImageChange {
        private Long imageId;
        private String name;
        private String description;
        private String status;
        private String imageUrl;
        private String color;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageFeatureChange {
        private Long featureId;
        private String featureName;
        private String featureValue;
        private String featureDescription;
        private String status;
        private String color;
        private String hoverColor;
        private String specialNote;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageDayAccommodationChange {
        private Long packageDayAccommodationId;
        private Integer dayNumber;
        private Boolean breakfast;
        private String breakfastDescription;
        private Boolean lunch;
        private String lunchDescription;
        private Boolean dinner;
        private String dinnerDescription;
        private Boolean morningTea;
        private String morningTeaDescription;
        private Boolean eveningTea;
        private String eveningTeaDescription;
        private Boolean snacks;
        private String snackNote;
        private Long hotelId;
        private Long transportId;
        private String otherNotes;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageInclusionChange {
        private Long inclusionId;
        private String inclusionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageExclusionChange {
        private Long exclusionId;
        private String exclusionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageConditionChange {
        private Long conditionId;
        private String conditionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageTravelTipChange {
        private Long travelTipId;
        private String tipTitle;
        private String tipDescription;
        private Integer displayOrder;
        private String status;
    }
}