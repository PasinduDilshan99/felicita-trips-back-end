package com.felicita.model.response.tour.schedule;

import com.felicita.model.response.common.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleParamsResponse {
    private List<String> durations;
    private List<TourIdAndNameResponse> tourIdAndNameResponses;
    private List<SeasonIdAndNameResponse> seasonIdAndNameResponses;
    private List<SortByResponse> sortByResponses;
}
