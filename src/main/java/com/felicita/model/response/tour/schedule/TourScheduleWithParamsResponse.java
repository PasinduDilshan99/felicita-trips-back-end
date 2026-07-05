package com.felicita.model.response.tour.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleWithParamsResponse {
    private int tourScheduleCount;
    private List<TourScheduleResponse> tourScheduleResponses;
}
