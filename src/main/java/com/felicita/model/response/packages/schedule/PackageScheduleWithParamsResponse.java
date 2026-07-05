package com.felicita.model.response.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PackageScheduleWithParamsResponse {
    private int packageScheduleCount;
    private List<PackageScheduleResponse> packageScheduleResponses;
}
