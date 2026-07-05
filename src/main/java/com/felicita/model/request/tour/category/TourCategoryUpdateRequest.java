package com.felicita.model.request.tour.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourCategoryUpdateRequest {

    private Long categoryId;

    private String categoryName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private List<Long> addTourIds;
    private List<Long> removeTourIds;


    private List<TourCategoryImageInsertRequest> addImages;
    private List<TourCategoryImageUpdateRequest> updateImages;
    private List<Long> removeImageIds;

}