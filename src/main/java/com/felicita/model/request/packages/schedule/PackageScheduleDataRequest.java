package com.felicita.model.request.packages.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageScheduleDataRequest {
    private String name;
    private Long packageId;
    private Long tourScheduleId;
    private Long tourId;
    private Date startDate;
    private Date endDate;
    private String status;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
