package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationDataRequest {
    private String name;
    private Long minPrice;
    private Long maxPrice;
    private String duration;
    private String destinationCategory;
    private String season;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
