package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourDataRequest {
    private String name;
    private Double minPrice;
    private Double maxPrice;
    private Integer duration;
    private String tourType;
    private String tourCategory;
    private String season;
    private String location;
    private int pageSize;
    private int pageNumber;
}
