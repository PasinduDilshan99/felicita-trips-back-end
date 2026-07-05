package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SeasonUpdateComparisonResult {

    @Builder.Default
    private List<FieldChange> fieldChanges = new ArrayList<>();

    @Builder.Default
    private List<String> changes = new ArrayList<>();

    @Builder.Default
    private boolean hasChanges = false;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    // Month range validation
    private boolean isMonthRangeValid;
    private Integer monthsSpan;

    // Temperature validation
    private boolean isTemperatureValid;

    // Associated items changes
    @Builder.Default
    private List<Long> activitiesToAdd = new ArrayList<>();

    @Builder.Default
    private List<Long> activitiesToRemove = new ArrayList<>();

    @Builder.Default
    private List<Long> toursToAdd = new ArrayList<>();

    @Builder.Default
    private List<Long> toursToRemove = new ArrayList<>();

    // Image changes
    @Builder.Default
    private List<ImageChange> imagesToAdd = new ArrayList<>();

    @Builder.Default
    private List<Long> imagesToRemove = new ArrayList<>();

    @Builder.Default
    private List<ImageUpdateChange> imagesToUpdate = new ArrayList<>();

    private Integer oldStatus;
    private Integer newStatus;
    private String changedBy;
    private Long changedByUserId;
    private String changeTimestamp;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldChange {
        private String fieldName;
        private Object oldValue;
        private Object newValue;
        private String fieldLabel;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageChange {
        private String name;
        private String description;
        private String imageUrl;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageUpdateChange {
        private Long imageId;
        private String oldName;
        private String newName;
        private String oldDescription;
        private String newDescription;
        private String oldImageUrl;
        private String newImageUrl;
        private String oldStatus;
        private String newStatus;
    }
}