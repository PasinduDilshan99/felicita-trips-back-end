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
public class DestinationCategoryUpdateComparisonResult {

    private Long categoryId;

    private List<FieldChange> fieldChanges;
    private List<ImageChange> imageChanges;
    private List<DestinationChange> destinationChanges;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FieldChange {

        private String fieldName;
        private String oldValue;
        private String newValue;
        private Boolean changed;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageChange {

        private Long imageId;
        private String changeType; // ADD | UPDATE | REMOVE

        private String oldValue;
        private String newValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DestinationChange {

        private Long destinationId;
        private String changeType; // ADD | REMOVE
        private String destinationName;
    }
}