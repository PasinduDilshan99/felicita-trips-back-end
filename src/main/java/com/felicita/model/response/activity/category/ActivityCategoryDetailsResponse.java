package com.felicita.model.response.activity.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityCategoryDetailsResponse {

    private Long categoryId;
    private String categoryName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private Date createdAt;
    private Long createdBy;
    private String createdByName;

    private Date updatedAt;
    private Long updatedBy;
    private String updatedByName;

    private Date terminatedAt;
    private Long terminatedBy;

    private List<CategoryImage> images;
    private List<Activity> primaryActivities;
    private List<Activity> otherActivities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryImage {

        private Long imageId;
        private String name;
        private String description;
        private String imageUrl;
        private String status;
        private Date createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Activity {

        private Long activityId;
        private String activityName;
    }
}