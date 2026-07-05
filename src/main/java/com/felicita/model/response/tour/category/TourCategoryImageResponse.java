package com.felicita.model.response.tour.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourCategoryImageResponse {

    private Long imageId;
    private String name;
    private String description;
    private String imageUrl;
    private String status;

}