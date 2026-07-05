package com.felicita.model.response.seasons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeasonAllDetailsResponse {
    private Long id;
    private String name;
    private String standardName;
    private String localName;
    private Integer startMonth;
    private Integer endMonth;
    private String monsoonType;
    private String weatherSummary;
    private Integer temperatureMin;
    private Integer temperatureMax;
    private String rainfallPattern;
    private Boolean isPeak;
    private Integer displayOrder;
    private String description;
    private Integer status;

    private LocalDateTime createdAt;
    private Integer createdBy;
    private LocalDateTime updatedAt;
    private Integer updatedBy;

    private List<SeasonImageResponse> seasonImages;
    private List<SeasonActivity> activities;
    private List<SeasonTour> tours;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonActivity{
        private Long activityId;
        private String activityName;
        private String activityDescription;
        private String activityStatus;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SeasonTour{
        private Long tourId;
        private String tourName;
        private String tourDescription;
        private String tourStatus;
    }

}
