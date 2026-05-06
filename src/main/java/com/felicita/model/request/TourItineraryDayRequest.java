package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourItineraryDayRequest {
    private Integer dayNumber;
    private List<TourItineraryDestinationRequest> destinations;
}