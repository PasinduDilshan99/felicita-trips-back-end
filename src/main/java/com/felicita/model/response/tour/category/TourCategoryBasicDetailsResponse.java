package com.felicita.model.response.tour.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourCategoryBasicDetailsResponse {

    private Long categoryId;
    private String categoryName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private List<TourCategoryImageResponse> images;

}