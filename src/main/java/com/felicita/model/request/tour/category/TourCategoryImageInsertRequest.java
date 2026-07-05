package com.felicita.model.request.tour.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TourCategoryImageInsertRequest {
    private String name;
    private String description;
    private String imageUrl;
    private String status;
}
