package com.felicita.model.response.packages;

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
public class ParamsForPackageRequestResponse {
    private Double minPrice;
    private Double maxPrice;
    private List<Integer> durations;
    private List<String> locations;
    private Integer minGroupSize;
    private Integer maxGroupSize;
    private Date fromDate;
    private Date toDate;
}
