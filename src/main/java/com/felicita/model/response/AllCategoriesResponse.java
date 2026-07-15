package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllCategoriesResponse {

    private List<ActivityCategory> activityCategoryList;
    private List<DestinationCategory> destinationCategoryList; // Fixed typo
    private List<TourCategory> tourCategoryList;
    private List<PackageCategory> packageCategoryList;
    private List<TourType> tourTypeList;
    private List<Seasons> seasonsList;
    private List<Status> statusList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActivityCategory {
        private Long activityCategoryId;
        private String activityCategoryName;
        private String activityCategoryDescription;
        private String activityCategoryColor;
        private String activityCategoryHoverColor;
        private List<Images> activityCategoryImages; // Added images list
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DestinationCategory {
        private Long destinationCategoryId;
        private String destinationCategoryName;
        private String destinationCategoryDescription;
        private String destinationCategoryColor;
        private String destinationCategoryHoverColor;
        private List<Images> destinationCategoryImages; // Added images list
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourCategory {
        private Long tourCategoryId;
        private String tourCategoryName;
        private String tourCategoryDescription;
        private String tourCategoryColor;
        private String tourCategoryHoverColor;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PackageCategory {
        private Long packageCategoryId;
        private String packageCategoryName;
        private String packageCategoryDescription;
        private String packageCategoryColor;
        private String packageCategoryHoverColor;
        private List<Images> packageCategoryImages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Seasons {
        private Long seasonId;
        private String seasonName;
        private String seasonStandardName;
        private String seasonDescription;
        private Integer startMonth;
        private Integer endMonth;
        private Boolean isPeak;
        private List<Images> seasonImages;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourType {
        private Long tourTypeId;
        private String tourTypeName;
        private String tourTypeDescription;
        private String tourTypeColor;
        private String tourTypeHoverColor;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Images {
        private Long imageId;
        private String imageUrl;
        private String imageName;
        private String imageDescription;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Status {
        private Long statusId;
        private String statusName;
        private String statusDescription;
    }
}
