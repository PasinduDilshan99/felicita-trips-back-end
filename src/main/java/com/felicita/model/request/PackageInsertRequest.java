package com.felicita.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageInsertRequest {

    private Long packageType;
    private Long tourId;
    private String name;
    private String description;
    private BigDecimal totalPrice;
    private BigDecimal discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private String status;
    private String hoverColor;
    private Integer minPersonCount;
    private Integer maxPersonCount;
    private BigDecimal pricePerPerson;

    private List<PackageImageInsertRequest> images;
    private List<PackageFeaturesInsertRequest> addFeatures;
    private List<PackageDayAccommodationInsertRequest> dayAccommodations;
    private List<PackageInclusionInsertRequest> inclusions;
    private List<PackageExclusionInsertRequest> exclusions;
    private List<PackageConditionInsertRequest> conditions;
    private List<PackageTravelTipInsertRequest> travelTips;

}
