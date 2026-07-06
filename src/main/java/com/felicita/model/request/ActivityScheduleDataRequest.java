package com.felicita.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityScheduleDataRequest {
    private String name;
    private String duration;
    private Long activityId;
    private Long destinationId;
    private Long packageScheduleId;
    private Long tourScheduleId;
    private Long activityCategoryId;
    private Date fromDate;
    private Date toDate;
    private Long seasonId;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
