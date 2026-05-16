package com.felicita.model.response.tour.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleResponse {

    private Long tourScheduleId;
    private String tourScheduleName;

    private String assumeStartDate;
    private String assumeEndDate;

    private Integer durationStart;
    private Integer durationEnd;

    private String specialNote;
    private String description;

    private String scheduleStatus;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Long tourId;
    private String tourName;

    private Integer tourDuration;

    private String startLocation;
    private String endLocation;

    private String season;

    private String tourStatus;

    private List<TourCategoryDetails> categories;

    private List<TourTypeDetails> types;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourCategoryDetails {

        private Long categoryId;
        private String categoryName;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TourTypeDetails {

        private Long typeId;
        private String typeName;

    }
}