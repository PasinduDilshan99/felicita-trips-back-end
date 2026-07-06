package com.felicita.model.request.tour.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourScheduleDataRequest {
    private String name;
    private String duration;
    private Long tourId;
    private Long tourTypeId;
    private Long tourCategoryId;
    private Date fromDate;
    private Date toDate;
    private Long seasonId;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
