package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityScheduleResponseDto {

    private Long activityId;
    private Long scheduleId;
    private Long destinationId;
    private String destinationName;
    private String activityName;
    private String activityScheduleName;
    private String description;
    private List<ActivityCategoryDto> activityCategoryDtos;
    private BigDecimal durationHours;
    private Time availableFrom;
    private Time availableTo;
    private BigDecimal priceLocal;
    private BigDecimal priceForeigners;
    private Integer minParticipate;
    private Integer maxParticipate;
    private Long seasonId;
    private String season;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private String scheduleAssumeStartDate;
    private String scheduleAssumeEndDate;
    private BigDecimal scheduleDurationHoursStart;
    private BigDecimal scheduleDurationHoursEnd;
    private String scheduleSpecialNote;
    private String scheduleDescription;
    private String scheduleStatus;

    private List<ActivityImageDto> images;
}
