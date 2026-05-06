package com.felicita.model.response;

import com.felicita.model.dto.HotelsNamesAndIdsDto;
import com.felicita.model.dto.VehicleNumberIdTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPackageParamResponse {
    private List<HotelsNamesAndIdsDto> hotelsNamesAndIdsDtos;
    private List<VehicleNumberIdTypeDto> vehicleNumberIdTypeDtos;
    private List<String> inclusions;
    private List<String> exclusions;
    private List<String> conditions;
    private List<TravelTips> travelTips;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TravelTips{
        private String title;
        private String description;
    }
}
