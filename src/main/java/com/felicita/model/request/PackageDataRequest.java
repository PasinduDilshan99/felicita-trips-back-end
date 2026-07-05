package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDataRequest {
    private String name;
    private Long minPrice;
    private Long maxPrice;
    private String duration;
    private String packageType;
    private String location;
    private int minGroupSize;
    private int maxGroupSize;
    private Date fromDate;
    private Date toDate;
    private int pageSize;
    private int pageNumber;
    private String sortBy;
    private String sortDirection;
}
