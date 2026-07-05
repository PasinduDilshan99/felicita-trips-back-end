package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourScheduleResponseDto {
    private int scheduleId;
    private String scheduleName;
    private Date assumeStartDate;
    private Date assumeEndDate;
    private Integer durationStart;
    private Integer durationEnd;
    private String specialNote;
    private String scheduleDescription;
}
