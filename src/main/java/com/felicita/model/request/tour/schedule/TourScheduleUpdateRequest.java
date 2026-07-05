package com.felicita.model.request.tour.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleUpdateRequest {
    private Long tourScheduleId;
    private String tourScheduleName;
    private Long tourId;
    private Date assumeStartDate;
    private Date assumeEndDate;
    private Double durationHoursStart;
    private Double durationHoursEnd;
    private String specialNotes;
    private String description;
    private String status;
}
