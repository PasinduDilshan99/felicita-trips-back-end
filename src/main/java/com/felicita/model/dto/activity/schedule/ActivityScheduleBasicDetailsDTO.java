package com.felicita.model.dto.activity.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityScheduleBasicDetailsDTO {
    private Long activityScheduleId;
    private String activityScheduleName;
    private Long activityId;
    private Date assumeStartDate;
    private Date assumeEndDate;
    private Double durationHoursStart;
    private Double durationHoursEnd;
    private String specialNotes;
    private String description;
    private Long packageScheduleId;
    private Long tourScheduleId;
    private String status;
    private Long createdBy;
    private String createdByName;
    private Date createdAt;
    private Date updatedAt;
    private String updatedByName;
    private Long updatedBy;
}
