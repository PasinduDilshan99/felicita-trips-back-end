package com.felicita.model.request.seasons;

import lombok.Data;

import java.util.List;

@Data
public class SeasonUpdateRequest {

    private Long id;
    private String name;
    private String standardName;
    private String localName;
    private Integer startMonth;
    private Integer endMonth;
    private String monsoonType;
    private String weatherSummary;
    private Integer temperatureMin;
    private Integer temperatureMax;
    private String rainfallPattern;
    private Boolean isPeak;
    private Integer displayOrder;
    private String description;
    private String status;

    private List<SeasonImageInsertRequest> imageInsertRequests;
    private List<SeasonImageUpdateRequest> imageUpdateRequests;
    private List<Long> imageRemoveRequests;

    private List<Long> insertActivitiesIds;
    private List<Long> removeActivitiesIds;

    private List<Long> insertTourIds;
    private List<Long> removeTourIds;
}