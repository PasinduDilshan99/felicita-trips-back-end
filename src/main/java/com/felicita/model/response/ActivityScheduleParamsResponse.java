package com.felicita.model.response;

import com.felicita.model.response.common.*;
import com.felicita.model.response.common.ActivityIdAndNameResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityScheduleParamsResponse {
    private List<String> durations;
    private List<ActivityIdAndNameResponse> activityIdAndNameResponses;
    private List<DestinationIdAndNameResponse> destinationIdAndNameResponses;
    private List<TourScheduleIdAndNameResponse> tourScheduleIdAndNameResponses;
    private List<PackageScheduleIdAndNameResponse> packageScheduleIdAndNameResponses;
    private List<SeasonIdAndNameResponse> seasonIdAndNameResponses;
    private List<SortByResponse> sortByResponses;

}
