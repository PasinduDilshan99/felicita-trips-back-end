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
public class TourUpdateRequest {
    private Long tourId;
    private TourBasicDetails tourBasicDetails;

    private List<Long> addTourTypes;
    private List<Long> removeTourTypes;
    private List<TourTypeUpdateRequest> updateTourTypes;

    private List<Long> addTourCategories;
    private List<Long> removeTourCategories;
    private List<TourCategoryUpdateRequest> updateTourCategories;

    private List<TourItineraryDayRequest> itinerary;
    private List<Long> removeDestinations;
    private List<Long> removeActivities;
    private List<TourDestinationUpdateRequest> updateDestinations;

    private List<TourImageInsertRequest> addImages;
    private List<Long> removeImages;
    private List<TourImageUpdateRequest> updateImages;

    private List<TourInclusionInsertRequest> addInclusions;
    private List<Long> removeInclusions;
    private List<TourInclusionUpdateRequest> updateInclusions;

    private List<TourExclusionInsertRequest> addExclusions;
    private List<Long> removeExclusions;
    private List<TourExclusionUpdateRequest> updateExclusions;

    private List<TourConditionInsertRequest> addConditions;
    private List<Long> removeConditions;
    private List<TourConditionUpdateRequest> updateConditions;

    private List<TourTravelTipInsertRequest> addTravelTips;
    private List<Long> removeTravelTips;
    private List<TourTravelTipUpdateRequest> updateTravelTips;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourBasicDetails {
        private String tourName;
        private String tourDescription;
        private Integer duration;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String startLocation;
        private String endLocation;
        private Long season;
        private String status;
        private Long assignTo;
        private String assignMessage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourCategoryUpdateRequest {
        private Long tourCategoryId;
        private Boolean isPrimary;
        private String status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourTypeUpdateRequest {
        private Long tourTypeId;
        private Boolean isPrimary;
        private String status;
    }
}
