package com.felicita.model.request.tour.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourTypeUpdateRequest {

    private Long typeId;

    private String typeName;
    private String description;
    private String color;
    private String hoverColor;
    private String status;

    private List<Long> addTourIds;
    private List<Long> removeTourIds;


    private List<TourTypeImageInsertRequest> addImages;
    private List<TourTypeImageUpdateRequest> updateImages;
    private List<Long> removeImageIds;

}