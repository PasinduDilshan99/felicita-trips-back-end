package com.felicita.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDestinationResponseDto {

    private Long destinationId;
    private String destinationName;
    private String destinationDescription;
    private String location;
    private Double ratings;
    private String destinationStatus;
    private Boolean primary;
}