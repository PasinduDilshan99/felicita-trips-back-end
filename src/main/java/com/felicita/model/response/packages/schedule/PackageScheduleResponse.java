package com.felicita.model.response.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleResponse {
    private Long packageScheduleId;
    private String packageScheduleName;
    private Long packageId;
    private String packageName;
    private Date startDate;
    private Date endDate;
    private Integer durationStart;
    private Integer durationEnd;
    private String status;
    private String specialNote;
    private String description;
    private Long tourScheduleId;
    private String tourScheduleName;
}
