package com.felicita.model.response.tour.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourTypeBasicDetailsResponse {

    private Long typeId;
    private String typeName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private List<TourTypeImageResponse> images;

}