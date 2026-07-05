package com.felicita.model.response.packages.schedule;

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
public class PackageScheduleParamsResponse {
    private List<TourIdAndNameResponse> tourIdAndNameResponses;
    private List<PackageIdAndNamesResponse> packageIdAndNamesResponses;
    private List<TourScheduleIdAndNameResponse> tourScheduleIdAndNameResponses;
    private List<SortByResponse> sortByResponses;
}
