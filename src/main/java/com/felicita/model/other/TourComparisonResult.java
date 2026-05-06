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
public class TourComparisonResult {

    // Basic details changes
    private List<FieldChange> basicDetailsChanges;

    // Tour Types changes
    private List<Long> tourTypeIdsToAdd;
    private List<Long> tourTypeIdsToRemove;
    private List<TourTypeChange> tourTypesToUpdate;

    // Tour Categories changes
    private List<Long> tourCategoryIdsToAdd;
    private List<Long> tourCategoryIdsToRemove;
    private List<TourCategoryChange> tourCategoriesToUpdate;

    // Destinations changes
    private List<TourDestinationChange> destinationsToAdd;
    private List<Long> destinationIdsToRemove;
    private List<TourDestinationChange> destinationsToUpdate;

    // Images changes
    private List<TourImageChange> imagesToAdd;
    private List<Long> imageIdsToRemove;
    private List<TourImageChange> imagesToUpdate;

    // Inclusions changes
    private List<TourInclusionChange> inclusionsToAdd;
    private List<Long> inclusionIdsToRemove;
    private List<TourInclusionChange> inclusionsToUpdate;

    // Exclusions changes
    private List<TourExclusionChange> exclusionsToAdd;
    private List<Long> exclusionIdsToRemove;
    private List<TourExclusionChange> exclusionsToUpdate;

    // Conditions changes
    private List<TourConditionChange> conditionsToAdd;
    private List<Long> conditionIdsToRemove;
    private List<TourConditionChange> conditionsToUpdate;

    // Travel Tips changes
    private List<TourTravelTipChange> travelTipsToAdd;
    private List<Long> travelTipIdsToRemove;
    private List<TourTravelTipChange> travelTipsToUpdate;

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
    public static class TourTypeChange {
        private Long tourTypeId;
        private Boolean isPrimary;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourCategoryChange {
        private Long tourCategoryId;
        private Boolean isPrimary;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourDestinationChange {
        private Long tourDestinationId;
        private Long destinationId;
        private Long activityId;
        private Integer dayNumber;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourImageChange {
        private Long imageId;
        private String imageName;
        private String imageDescription;
        private String imageUrl;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourInclusionChange {
        private Long inclusionId;
        private String inclusionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourExclusionChange {
        private Long exclusionId;
        private String exclusionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourConditionChange {
        private Long conditionId;
        private String conditionText;
        private Integer displayOrder;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourTravelTipChange {
        private Long travelTipId;
        private String tipTitle;
        private String tipDescription;
        private Integer displayOrder;
        private String status;
    }
}