package com.felicita.model.response.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamsForTourRequestResponse {
    private Double minPrice;
    private Double maxPrice;
    private List<Integer> durations;
    private List<String> locations;
}
