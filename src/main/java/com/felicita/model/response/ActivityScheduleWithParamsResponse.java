package com.felicita.model.response;

import com.felicita.model.dto.ActivityScheduleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityScheduleWithParamsResponse {
    private int activityCount;
    private List<ActivityScheduleResponseDto> activityScheduleResponseDtos;
}
