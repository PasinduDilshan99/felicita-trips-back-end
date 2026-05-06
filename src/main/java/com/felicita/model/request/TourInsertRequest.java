package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourInsertRequest {
    private String name;
    private String description;
    private List<Long> tourTypes;
    private List<Long> tourCategories;
    private Integer duration;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String startLocation;
    private String endLocation;
    private Long season;
    private String status;
    private Long assignTo;
    private String assignMessage;

    private List<TourImageInsertRequest> images;

    private List<TourItineraryDayRequest> itinerary;

    private List<TourInclusionInsertRequest> inclusions;
    private List<TourExclusionInsertRequest> exclusions;
    private List<TourConditionInsertRequest> conditions;
    private List<TourTravelTipInsertRequest> travelTips;

}
