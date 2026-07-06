package com.felicita.model.response.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ActivityIdScheduleIdAndScheduleNameResponse {
    private Long activityId;
    private Long scheduleId;
    private String scheduleName;
}
