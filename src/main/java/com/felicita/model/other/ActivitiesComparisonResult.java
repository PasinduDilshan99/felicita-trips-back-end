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
public class ActivitiesComparisonResult {

    // Basic field changes
    private List<FieldChange> basicFieldChanges;

    // Category changes
    private List<Long> categoryIdsToRemove;
    private List<CategoryChange> categoriesToAdd;
    private List<CategoryChange> categoriesToUpdate;

    // Image changes
    private List<Long> imageIdsToRemove;
    private List<ImageChange> imagesToAdd;
    private List<ImageChange> imagesToUpdate;

    // Requirement changes
    private List<Long> requirementIdsToRemove;
    private List<RequirementChange> requirementsToAdd;
    private List<RequirementChange> requirementsToUpdate;

    // Summary
    private boolean hasChanges;
    private String summary;

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
    public static class CategoryChange {
        private Long categoryId;
        private Boolean isPrimary;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageChange {
        private Long imageId;
        private String name;
        private String description;
        private String imageUrl;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RequirementChange {
        private Long requirementId;
        private String name;
        private String value;
        private String description;
        private String color;
        private String status;
    }
}