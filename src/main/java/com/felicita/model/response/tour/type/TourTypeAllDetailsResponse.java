package com.felicita.model.response.tour.type;

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
public class TourTypeAllDetailsResponse {

    private Long typeId;
    private String typeName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private Date createdAt;
    private Long createdBy;
    private String createdByName;

    private Date updatedAt;
    private Long updatedBy;
    private String updatedByName;

    private Date terminatedAt;
    private Long terminatedBy;

    private Integer totalTours;

    private List<TourTypeImageResponse> images;

    private List<TourBasicDetails> tours;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourBasicDetails {

        private Long tourId;
        private String tourName;
        private String description;

        private Integer duration;

        private Double latitude;
        private Double longitude;

        private String startLocation;
        private String endLocation;

        private String season;
        private String status;

        private Boolean primaryType;
    }
}