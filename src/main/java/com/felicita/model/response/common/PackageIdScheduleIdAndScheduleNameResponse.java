package com.felicita.model.response.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageIdScheduleIdAndScheduleNameResponse {
    private Long packageId;
    private Long scheduleId;
    private String scheduleName;
}
