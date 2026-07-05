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
public class AssignBookingUpdateComparisonResult {

    @Builder.Default
    private List<FieldChange> fieldChanges = new ArrayList<>();

    @Builder.Default
    private List<String> changes = new ArrayList<>();

    @Builder.Default
    private boolean hasChanges = false;

    @Builder.Default
    private List<String> warnings = new ArrayList<>();

    private Long oldAssignedEmployeeId;
    private String oldAssignedEmployeeName;
    private Long newAssignedEmployeeId;
    private String newAssignedEmployeeName;
    private String oldAssignMessage;
    private String newAssignMessage;

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