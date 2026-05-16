package com.felicita.model.response.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PacakgeScheduleBasicDetailsResponse {

    private Long packageScheduleId;

    private String packageScheduleName;

    private Long packageId;
    private String packageName;

    private Long tourScheduleId;
    private String tourScheduleName;

    private Date assumeStartDate;
    private Date assumeEndDate;

    private Integer durationStart;
    private Integer durationEnd;

    private String specialNote;
    private String description;

    private String status;

    private Long createdBy;
    private Date createdAt;

    private Long updatedBy;
    private Date updatedAt;
}