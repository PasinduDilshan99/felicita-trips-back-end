package com.felicita.model.request.activity.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityCategoryInsertRequest {

    private String categoryName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;
    private List<Long> activityIds;
    private List<ActivityCategoryImageRequest> images;
}
