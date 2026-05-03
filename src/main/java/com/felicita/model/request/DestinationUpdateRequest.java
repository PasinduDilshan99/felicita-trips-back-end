package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DestinationUpdateRequest {
    private Long destinationId;
    private String name;
    private String description;
    private String status;
    private List<Long> removedestinationCategoriesIdList;
    private List<Long> adddestinationCategoriesIdList;
    private String location;
    private Double latitude;
    private Double longitude;
    private Double extraPrice;
    private String extraPriceNote;
    private List<Long> removeImages;
    private List<DestinationInsertRequest.Image> newImages;
    private List<Long> removeActivities;
    private List<DestinationUpdateRequest.Activity> newActivities;

    @Data
    public static class Image {
        private String name;
        private String description;
        private String imageUrl;
        private String status;
    }

    @Data
    public static class Activity{
        private String name;
        private String description;
        private List<Long> addActivityCategoriesId;
        private List<Long> removeActivityCategoriesId;
        private Double durationHover;
        private LocalTime availableFrom;
        private LocalTime availableTo;
        private Double priceLocal;
        private Double priceForeigners;
        private Integer minParticipate;
        private Integer maxParticipate;
        private Long seasonId;
        private String status;
        private List<DestinationUpdateRequest.Image> activityImages;
    }
}
