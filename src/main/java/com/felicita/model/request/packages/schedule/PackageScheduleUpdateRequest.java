package com.felicita.model.request.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleUpdateRequest {

    private Long packageScheduleId;

    private String packageScheduleName;

    private Long packageId;

    private Date assumeStartDate;
    private Date assumeEndDate;

    private Integer durationStart;
    private Integer durationEnd;

    private String specialNote;

    private String description;

    private String status;

    private Long tourScheduleId;
}