package com.felicita.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityBasicDetailsResponse {

    private Long activityId;
    private Long destinationId;

    private String name;
    private String description;

    private BigDecimal durationHours;

    private LocalTime availableFrom;
    private LocalTime availableTo;

    private BigDecimal priceLocal;
    private BigDecimal priceForeigners;

    private Integer minParticipate;
    private Integer maxParticipate;

    private String season;
    private Long seasonId;

    private Long statusId;

    private List<Category> categories;
    private List<Image> images;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Category {
        private Long categoryId;
        private String categoryName;
        private Boolean isPrimary;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Image {
        private Long imageId;
        private String name;
        private String description;
        private String imageUrl;
    }
}