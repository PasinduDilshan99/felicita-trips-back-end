package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourResponseDto {
    private Long tourId;
    private String tourName;
    private String tourDescription;
    private Integer duration;
    private Double latitude;
    private Double longitude;
    private String startLocation;
    private String endLocation;
    private Double tourStartingPrice;
    private List<TourTypeDto> tourTypeDtos;
    private List<TourCategoryDto> tourCategoryDto;
    private String seasonName;
    private String seasonDescription;
    private String statusName;

    private Boolean wish;

    private List<TourScheduleResponseDto> schedules;
    private List<TourImageResponseDto> images;
}
