package com.felicita.model.request.activity.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ActivityCategoryImageRequest {

    private String name;
    private String description;
    private String imageUrl;
    private String status;
}