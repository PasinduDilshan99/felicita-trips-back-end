package com.felicita.model.response;

import com.felicita.model.dto.ActivityCategoryDto;
import com.felicita.model.dto.ActivityImageDto;
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
public class ActivityScheduleDetailsResponse {

    // =====================================================
    // ACTIVITY SCHEDULE DETAILS
    // =====================================================

    private Long activityScheduleId;
    private String activityScheduleName;
    private String scheduleAssumeStartDate;
    private String scheduleAssumeEndDate;
    private BigDecimal scheduleDurationHoursStart;
    private BigDecimal scheduleDurationHoursEnd;
    private String scheduleSpecialNote;
    private String scheduleDescription;
    private String scheduleStatus;
    private Timestamp scheduleCreatedAt;
    private Timestamp scheduleUpdatedAt;

    // =====================================================
    // ACTIVITY DETAILS
    // =====================================================

    private Long activityId;
    private String activityName;
    private String activityDescription;
    private BigDecimal durationHours;
    private Time availableFrom;
    private Time availableTo;
    private BigDecimal priceLocal;
    private BigDecimal priceForeigners;
    private Integer minParticipate;
    private Integer maxParticipate;
    private Long seasonId;
    private String season;
    private String activityStatus;
    private Timestamp activityCreatedAt;
    private Timestamp activityUpdatedAt;

    // =====================================================
    // DESTINATION DETAILS
    // =====================================================

    private Long destinationId;
    private String destinationName;

    // =====================================================
    // TOUR DETAILS
    // =====================================================

    private Long tourId;
    private String tourName;
    private String tourDescription;
    private Integer tourDuration;
    private String startLocation;
    private String endLocation;
    private String tourStatus;

    // =====================================================
    // TOUR SCHEDULE DETAILS
    // =====================================================

    private Long tourScheduleId;
    private String tourScheduleName;
    private String tourScheduleStartDate;
    private String tourScheduleEndDate;
    private Integer tourScheduleDurationStart;
    private Integer tourScheduleDurationEnd;
    private String tourScheduleStatus;

    // =====================================================
    // PACKAGE DETAILS
    // =====================================================

    private Long packageId;
    private String packageName;
    private String packageDescription;
    private BigDecimal totalPrice;
    private BigDecimal discountPercentage;
    private BigDecimal pricePerPerson;
    private Integer minPersonCount;
    private Integer maxPersonCount;
    private String packageStatus;

    // =====================================================
    // PACKAGE SCHEDULE DETAILS
    // =====================================================

    private Long packageScheduleId;
    private String packageScheduleName;
    private String packageScheduleStartDate;
    private String packageScheduleEndDate;
    private Integer packageScheduleDurationStart;
    private Integer packageScheduleDurationEnd;
    private String packageScheduleStatus;

    // =====================================================
    // EXTRA DETAILS
    // =====================================================

    private List<ActivityCategoryDto> activityCategoryDtos;
    private List<ActivityImageDto> images;
}