package com.felicita.model.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroSectionComparisonResult {

    @Builder.Default
    private List<FieldChange> fieldChanges = new ArrayList<>();

    @Builder.Default
    private List<String> changes = new ArrayList<>();

    @Builder.Default
    private boolean hasChanges = false;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    private Long oldStatusId;
    private String oldStatusName;
    private Long newStatusId;
    private String newStatusName;
    private Integer oldOrder;
    private Integer newOrder;

    private String changedBy;
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
}